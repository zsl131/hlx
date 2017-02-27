package com.zslin.basic.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.annotations.Token;
import com.zslin.basic.model.Menu;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.repository.SimpleSpecification;
import com.zslin.basic.repository.SimpleSpecificationBuilder;
import com.zslin.basic.service.IMenuService;
import com.zslin.basic.tools.AuthTools;
import com.zslin.basic.tools.TokenTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 菜单管理Controller
 * @author zsl-pc 20160511
 *
 */
@Controller
@RequestMapping(value="admin/menu")
@AdminAuth(name = "菜单管理", psn="权限管理", orderNum = 1, pentity=0, porderNum=2)
public class MenuController {

    @Autowired
    private IMenuService menuService;

    @Autowired
    private AuthTools authTools;

    /** 列表 */
    @AdminAuth(name = "菜单管理", orderNum = 1, icon="fa fa-bars", type="1")
    @RequestMapping(value="list", method= RequestMethod.GET)
    public String list(Model model, Integer pid, Integer page, HttpServletRequest request) {
        String treeJson = queryTreeJson("1");
        Page<Menu> datas ;
        if(pid==null || pid<=0) {
            datas = menuService.findAll(new SimpleSpecificationBuilder()
                            .add("pid", SimpleSpecification.IS_NULL, "")
                            .add("type", SimpleSpecification.EQUAL, "1").generate(),
                    SimplePageBuilder.generate(page, 15, SimpleSortBuilder.generateSort("orderNum")));
        } else {
            datas = menuService.findAll(new SimpleSpecificationBuilder<>()
                    .add("pid", SimpleSpecification.EQUAL, pid)
                    .add("type", SimpleSpecification.EQUAL, "1").generate(),
                    SimplePageBuilder.generate(page, 15, SimpleSortBuilder.generateSort("orderNum")));
        }
        model.addAttribute("treeJson", treeJson);
        model.addAttribute("datas", datas);
        return "admin/basic/menu/list";
    }

    @AdminAuth(name="重构菜单", orderNum=3)
    @RequestMapping(value="rebuildMenus", method=RequestMethod.POST)
    public @ResponseBody String rebuildMenus(Model model, HttpServletRequest request) {
        authTools.buildSystemMenu();
        return "1";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name="修改菜单", orderNum=3)
    @RequestMapping(value="update/{id}", method=RequestMethod.GET)
    public String update(Model model, @PathVariable Integer id, HttpServletRequest request) {
        Menu m = menuService.findOne(id);
        model.addAttribute("menu", m);
        return "admin/basic/menu/update";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="update/{id}", method=RequestMethod.POST)
    public String update(Model model, @PathVariable Integer id, Menu menu, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) {
            Menu m = menuService.findOne(id);
            m.setIcon(menu.getIcon());
            menuService.save(m);
        }
        return "redirect:/admin/menu/list";
    }

    @RequestMapping("updateSort")
    @AdminAuth(name="菜单排序", orderNum=4)
    public @ResponseBody String updateSort(Integer[] ids) {
        try {
            int index = 1;
            for(Integer id : ids) {
                menuService.updateByHql("update Menu m set m.orderNum="+(index++)+" WHERE m.id="+id);
            }
        } catch (Exception e) {
            return "0";
        }
        return "1";
    }

    /**
     * 获取菜单数据，以JSON数据返回
     * @param type 菜单类型，1：导航菜单；2：权限菜单；其他：全部
     * @return
     */
    public String queryTreeJson(String type) {
        return buildTreeJson(null, type);
    }

    /**
     * 生成JSON数据
     * @param pid 父Id
     * @param type 类型
     * @return
     */
    private String buildTreeJson(Integer pid, String type) {
        List<Menu> plist = this.listByPid(pid, type);
        if(plist!=null && plist.size()>0) {
            Integer index = 0;
            StringBuffer sb = new StringBuffer();
            if(pid!=null && pid>0) {sb.append(",nodes:");}
            sb.append("[");
            for(Menu pm : plist) {
                index++;
                sb.append("{text:'<span title=").append(pm.getId()).append(">").append(pm.getName()).append("</span>', ")
                        .append("href:'javascript:targetHref(").append(pm.getId()).append(")'");

                //递归获取数据
                sb.append(buildTreeJson(pm.getId(), type));

                sb.append("}");
                if(index<plist.size()) {sb.append(",");}
            }
            sb.append("]");
            return sb.toString();
        } else {
            return "";
        }
    }

    /**
     * 通过父Id，及菜单类型获取菜单数据
     * @param pid 父Id,如果为空则获取最高层的菜单
     * @param type 菜单类型，1：导航菜单；2：权限菜单
     * @return
     */
    public List<Menu> listByPid(Integer pid, String type) {
        String hql = "FROM Menu m WHERE 1=1 ";
        if(pid!=null && pid>0) {hql += " AND m.pid="+pid;}
        else {hql += " AND m.pid is null ";}
        if(type!=null && ("1".equals(type) || "2".equals(type))) {
            hql += " AND m.type='"+type+"'";
        }
        hql += " ORDER BY m.orderNum ASC";
//        return em.createQuery(hql).getResultList();
        return menuService.listByHql(hql);
    }
}