package com.zslin.admin.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.annotations.Token;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.repository.SimpleSpecificationBuilder;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.basic.tools.TokenTools;
import com.zslin.web.model.WxMenu;
import com.zslin.web.service.IWxMenuService;
import com.zslin.wx.tools.AccessTokenTools;
import com.zslin.wx.tools.JsonTools;
import com.zslin.wx.tools.WeixinUtil;
import com.zslin.wx.tools.WxConfig;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/26 0:10.
 */
@Controller
@RequestMapping(value = "admin/wxMenu")
@AdminAuth(name = "微信菜单管理", psn = "微信管理", orderNum = 7, porderNum = 1, pentity = 0, icon = "fa fa-list")
public class AdminWxMenuController {

    @Autowired
    private IWxMenuService wxMenuService;

    @Autowired
    private AccessTokenTools accessTokenTools;

    @Autowired
    private WxConfig wxConfig;

    @GetMapping(value = "list")
    @AdminAuth(name = "微信菜单管理", type = "1", orderNum = 1, icon = "fa fa-list")
    public String list(Model model, Integer pid, HttpServletRequest request) {
        /*Page<WxMenu> datas = wxMenuService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page));*/

        if(pid!=null && pid>0) { //获取子菜单
            SimpleSpecificationBuilder b = new SimpleSpecificationBuilder();
            b.add("pid", "eq", pid);
            List<WxMenu> subList = wxMenuService.findAll(b.generate(), SimpleSortBuilder.generateSort("orderNo"));
            model.addAttribute("datas", subList);
            model.addAttribute("parent", wxMenuService.findOne(pid)); //获取父对象
        } else {
            SimpleSpecificationBuilder builder = new SimpleSpecificationBuilder();
            builder.add("pid", "isnull", "", "or").add("pid", "le", 0, "or");
            List<WxMenu> rootList = wxMenuService.findAll(builder.generate(),
                    SimpleSortBuilder.generateSort("orderNo"));

            model.addAttribute("datas", rootList);
        }
//        model.addAttribute("datas", datas);
        return "admin/wxMenu/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name = "添加微信菜单", orderNum = 2, icon="fa fa-plus")
    @RequestMapping(value="add", method= RequestMethod.GET)
    public String add(Model model, Integer pid, HttpServletRequest request) {
        WxMenu m = new WxMenu();
        if(pid!=null && pid>0) {
            model.addAttribute("parent", wxMenuService.findOne(pid)); //获取父对象
        }
        model.addAttribute("wxMenu", m);
        return "admin/wxMenu/add";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="add", method=RequestMethod.POST)
    public String add(Model model, Integer pid, WxMenu wxMenu, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) { //不是重复提交
            wxMenuService.save(wxMenu);
        }
        return "redirect:/admin/wxMenu/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name="修改微信菜单", orderNum=3, icon = "fa fa-pencil")
    @RequestMapping(value="update/{id}", method=RequestMethod.GET)
    public String update(Model model, @PathVariable Integer id, HttpServletRequest request) {
        WxMenu m = wxMenuService.findOne(id);
        model.addAttribute("wxMenu", m);
        return "admin/wxMenu/update";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="update/{id}", method=RequestMethod.POST)
    public String update(Model model, @PathVariable Integer id, WxMenu wxMenu, HttpServletRequest request) {
//		Boolean isRepeat = (Boolean) request.getAttribute("isRepeat");
        if(TokenTools.isNoRepeat(request)) {
            WxMenu m = wxMenuService.findOne(id);
            MyBeanUtils.copyProperties(wxMenu, m, new String[]{"id"});
            wxMenuService.save(m);
        }
        return "redirect:/admin/wxMenu/list";
    }

    @AdminAuth(name="删除微信菜单", orderNum=4, icon = "fa fa-remove")
    @RequestMapping(value="delete/{id}", method=RequestMethod.POST)
    public @ResponseBody
    String delete(@PathVariable Integer id) {
        try {
            wxMenuService.delete(id);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }

    @AdminAuth(name="生成微信菜单", orderNum=4, icon = "fa fa-remove")
    @RequestMapping(value="gen", method=RequestMethod.GET)
    public
    String gen() {
        try {
            String json = createMenuJson();
            String url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+accessTokenTools.getAccessToken();
            JSONObject jsonObj = WeixinUtil.httpRequest(url, "POST", json);
            String code = JsonTools.getJsonParam(jsonObj.toString(), "errcode");
            if("0".equals(code)) {
                return "redirect:/admin/wxMenu/list";
            } else {
                return "redirect:/admin/wxMenu/list";
            }
        } catch (Exception e) {
            return "0";
        }
    }

    private String createMenuJson() {
        StringBuffer sb = new StringBuffer("{\"button\":[");
        List<WxMenu> parents = wxMenuService.findParent();
        int temp = 0;
        for(WxMenu p : parents) {
            sb.append(createSinglMenuJson(p));
            temp++;
            if(temp<parents.size()) {sb.append(",");}
        }
        sb.append("]}");
        return sb.toString();
    }

    private String createSinglMenuJson(WxMenu menu) {
        StringBuffer sb = new StringBuffer("{");
        List<WxMenu> suns = wxMenuService.findByPid(menu.getId());
        sb.append("\"name\":\"").append(menu.getName()).append("\"");
        if(suns==null || suns.size()<=0) {
            //无子菜单
            sb.append(createMenu(menu));
        } else {
            sb.append(",\"sub_button\":[");
            int temp = 0;
            for(WxMenu sun : suns) {
                sb.append("{");
                sb.append("\"name\":\"").append(sun.getName()).append("\"");
                sb.append(createMenu(sun));
                sb.append("}");
                temp ++;
                if(temp<suns.size()) {sb.append(",");}
            }
            sb.append("]");
        }
        sb.append("}");
        return sb.toString();
    }

    private String createMenu(WxMenu menu) {
        String url = menu.getUrl();
        if(!url.toLowerCase().startsWith("http://") && !url.toLowerCase().startsWith("https://")) { //如果不是http链接
            if(!url.startsWith("/")) {url = "/"+url;}
            url = wxConfig.getConfig().getUrl()+url;
        }
        StringBuffer sb = new StringBuffer();
        sb.append(",\"type\":\"view\"");
        sb.append(",\"url\":\"").append(url).append("\"");
        return sb.toString();
    }
}
