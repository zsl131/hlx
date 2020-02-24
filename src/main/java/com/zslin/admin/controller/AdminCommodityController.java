package com.zslin.admin.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.annotations.Token;
import com.zslin.basic.exception.SystemException;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.ConfigTools;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.basic.tools.NormalTools;
import com.zslin.basic.tools.TokenTools;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.client.tools.ClientFileTools;
import com.zslin.client.tools.ClientJsonTools;
import com.zslin.web.model.Commodity;
import com.zslin.web.service.ICommodityService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by 钟述林 393156105@qq.com on 2017/4/1 9:55.
 */
@Controller
@RequestMapping(value = "admin/commodity")
@AdminAuth(name = "商品管理", psn = "多店管理", orderNum = 6, porderNum = 1, pentity = 0, icon = "fa fa-podcast")
public class AdminCommodityController {

    @Autowired
    private ICommodityService commodityService;

    private static final String PATH_PRE = "commodity";

    @Autowired
    private ConfigTools configTools;

    @Autowired
    private ClientFileTools clientFileTools;



    @GetMapping(value = "list")
    @AdminAuth(name = "商品管理", orderNum = 1, type = "1", icon = "fa fa-list")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<Commodity> datas = commodityService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id_d")));
        model.addAttribute("datas", datas);
        return "admin/commodity/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name = "添加商品", orderNum = 2, icon="fa fa-plus")
    @RequestMapping(value="add", method= RequestMethod.GET)
    public String add(Model model, HttpServletRequest request) {
        model.addAttribute("commodity", new Commodity());
        return "admin/commodity/add";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="add", method=RequestMethod.POST)
    public String add(Model model, Commodity commodity, HttpServletRequest request, @RequestParam("file")MultipartFile[] files) {
        if(TokenTools.isNoRepeat(request)) { //不是重复提交
            String no = commodity.getNo();
            if(no==null || "".equalsIgnoreCase(no.trim())) {
                throw new SystemException("商品编号不能为空");
            }
            Commodity c = commodityService.findByNo(no);
            if(c!=null) {
                throw new SystemException("商品编号【"+no+"】已经存在");
            }
            commodity.setType("3"); //只能添加外卖单品
            if(files!=null && files.length>=1) {
                BufferedOutputStream bw = null;
                try {
                    String fileName = files[0].getOriginalFilename();
                    if(fileName!=null && !"".equalsIgnoreCase(fileName.trim()) && NormalTools.isImageFile(fileName)) {
                        File outFile = new File(configTools.getFilePath(PATH_PRE) + File.separator + UUID.randomUUID().toString()+ NormalTools.getFileType(fileName));
                        commodity.setPicPath(outFile.getAbsolutePath().replace(configTools.getFilePath(), File.separator));
                        FileUtils.copyInputStreamToFile(files[0].getInputStream(), outFile);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if(bw!=null) {bw.close();}
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            commodityService.save(commodity);
            send2Client(commodity, "update");
        }
        return "redirect:/admin/commodity/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name="修改商品", orderNum=3, icon = "fa fa-pencil")
    @RequestMapping(value="update/{id}", method=RequestMethod.GET)
    public String update(Model model, @PathVariable Integer id, HttpServletRequest request) {
        Commodity c = commodityService.findOne(id);
        model.addAttribute("commodity", c);
        return "admin/commodity/update";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="update/{id}", method=RequestMethod.POST)
    public String update(Model model, @PathVariable Integer id, Commodity commodity, HttpServletRequest request, @RequestParam("file")MultipartFile[] files) {
        if(TokenTools.isNoRepeat(request)) {
            Commodity c = commodityService.findOne(id);
            String type = c.getType();
//            if("3".equalsIgnoreCase(type)) { //只能修改外卖单品
            MyBeanUtils.copyProperties(commodity, c, new String[]{"id", "no", "type"});

            if (files != null && files.length >= 1) {
                BufferedOutputStream bw = null;
                try {
                    String fileName = files[0].getOriginalFilename();
                    if (fileName != null && !"".equalsIgnoreCase(fileName.trim()) && NormalTools.isImageFile(fileName)) {

                        File oldFile = new File(configTools.getFilePath() + c.getPicPath());
                        if (oldFile.exists()) {
                            oldFile.delete();
                        }

                        File outFile = new File(configTools.getFilePath(PATH_PRE) + File.separator + UUID.randomUUID().toString() + NormalTools.getFileType(fileName));
                        c.setPicPath(outFile.getAbsolutePath().replace(configTools.getFilePath(), File.separator));
                        FileUtils.copyInputStreamToFile(files[0].getInputStream(), outFile);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (bw != null) {
                            bw.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            commodityService.save(c);
            send2Client(c, "update");
//            }
        }
        return "redirect:/admin/commodity/list";
    }

    @AdminAuth(name="删除商品", orderNum=4, icon = "fa fa-remove")
    @RequestMapping(value="delete/{id}", method=RequestMethod.POST)
    public @ResponseBody
    String delete(@PathVariable Integer id) {
        try {
            Commodity c = commodityService.findOne(id);
            if(!"3".equalsIgnoreCase(c.getType())) { //只能删除外卖单品
                return "1";
            }
            File oldFile = new File(configTools.getFilePath()+c.getPicPath());
            if(oldFile.exists()) {oldFile.delete();}

            send2Client(c, "delete");
            commodityService.delete(id);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }

    public void send2Client(Commodity commodity, String action) {
        String content = ClientJsonTools.buildDataJson(ClientJsonTools.buildCommodity(commodity, action));
        clientFileTools.setChangeContext(content, true);
    }
}
