package com.zslin.admin.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.annotations.Token;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.tools.ConfigTools;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.basic.tools.NormalTools;
import com.zslin.basic.tools.TokenTools;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.client.tools.ClientFileTools;
import com.zslin.client.tools.ClientJsonTools;
import com.zslin.web.model.Prize;
import com.zslin.web.service.IPrizeService;
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
 * Created by 钟述林 393156105@qq.com on 2017/1/25 23:44.
 */
@Controller
@RequestMapping(value = "admin/prize")
@AdminAuth(name = "礼物管理", psn = "应用管理", orderNum = 11, porderNum = 1, pentity = 0, icon = "fa fa-diamond")
public class AdminPrizeController {

    @Autowired
    private IPrizeService prizeService;

    @Autowired
    private ConfigTools configTools;

    @Autowired
    private ClientFileTools clientFileTools;

    private static final String PATH_PRE = "prize";

    @GetMapping(value = "list")
    @AdminAuth(name = "礼物管理", type = "1", orderNum = 1, icon = "fa fa-diamond")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<Prize> datas = prizeService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page));
        model.addAttribute("datas", datas);
        return "admin/prize/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name = "添加礼物信息", orderNum = 2, icon="fa fa-plus")
    @RequestMapping(value="add", method= RequestMethod.GET)
    public String add(Model model, HttpServletRequest request) {
        model.addAttribute("prize", new Prize());
        return "admin/prize/add";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="add", method=RequestMethod.POST)
    public String add(Model model, Prize prize, HttpServletRequest request, @RequestParam("file")MultipartFile[] files) {
        if(TokenTools.isNoRepeat(request)) { //不是重复提交
            if(files!=null && files.length>=1) {
                BufferedOutputStream bw = null;
                try {
                    String fileName = files[0].getOriginalFilename();
                    if(fileName!=null && !"".equalsIgnoreCase(fileName.trim()) && NormalTools.isImageFile(fileName)) {
                        File outFile = new File(configTools.getUploadPath(PATH_PRE) + File.separator + UUID.randomUUID().toString()+ NormalTools.getFileType(fileName));
                        prize.setPicPath(outFile.getAbsolutePath().replace(configTools.getUploadPath(), File.separator));
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
            prizeService.save(prize);
            send2Client(prize, "update");
        }
        return "redirect:/admin/prize/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name="修改礼物信息", orderNum=3, icon = "fa fa-pencil")
    @RequestMapping(value="update/{id}", method=RequestMethod.GET)
    public String update(Model model, @PathVariable Integer id, HttpServletRequest request) {
        Prize p = prizeService.findOne(id);
        model.addAttribute("prize", p);
        return "admin/prize/update";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="update/{id}", method=RequestMethod.POST)
    public String update(Model model, @PathVariable Integer id, Prize prize, HttpServletRequest request, @RequestParam("file")MultipartFile[] files) {
//		Boolean isRepeat = (Boolean) request.getAttribute("isRepeat");
        if(TokenTools.isNoRepeat(request)) {
            Prize p = prizeService.findOne(id);
            MyBeanUtils.copyProperties(prize, p);
            if(files!=null && files.length>=1) {
                BufferedOutputStream bw = null;
                try {
                    String fileName = files[0].getOriginalFilename();
                    if(fileName!=null && !"".equalsIgnoreCase(fileName.trim()) && NormalTools.isImageFile(fileName)) {

                        File oldFile = new File(configTools.getUploadPath()+p.getPicPath());
                        if(oldFile.exists()) {oldFile.delete();}

                        File outFile = new File(configTools.getUploadPath(PATH_PRE) + File.separator + UUID.randomUUID().toString()+ NormalTools.getFileType(fileName));
                        p.setPicPath(outFile.getAbsolutePath().replace(configTools.getUploadPath(), File.separator));
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
            prizeService.save(p);
            send2Client(p, "update");
        }
        return "redirect:/admin/prize/list";
    }

    @AdminAuth(name="删除礼物", orderNum=4, icon = "fa fa-remove")
    @RequestMapping(value="delete/{id}", method=RequestMethod.POST)
    public @ResponseBody
    String delete(@PathVariable Integer id) {
        try {
            Prize p = prizeService.findOne(id);
            File oldFile = new File(configTools.getUploadPath()+p.getPicPath());
            if(oldFile.exists()) {oldFile.delete();}

            prizeService.delete(p);
            send2Client(p, "delete");
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }

    private void send2Client(Prize p, String action) {
        String type = p.getType();
        if("2".equals(type) || "3".equals(type) || "4".equals(type)) { //只有是抵价券、就餐券才需要
            String json = ClientJsonTools.buildDataJson(ClientJsonTools.buildPrize(p, action));
            clientFileTools.setChangeContext(json, true);
        }
    }
}