package com.zslin.admin.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.annotations.Token;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.tools.ConfigTools;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.basic.tools.NormalTools;
import com.zslin.basic.tools.TokenTools;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.web.model.Gallery;
import com.zslin.web.service.IGalleryService;
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
 * Created by 钟述林 393156105@qq.com on 2017/1/25 23:11.
 */
@Controller
@RequestMapping(value = "admin/gallery")
@AdminAuth(name = "微信画廊管理", psn = "应用管理", orderNum = 6, porderNum = 1, pentity = 0, icon = "fa fa-image")
public class AdminGalleryController {

    @Autowired
    private IGalleryService galleryService;

    @Autowired
    private ConfigTools configTools;

    private static final String PATH_PRE = "gallery/";

    @GetMapping(value = "list")
    @AdminAuth(name = "微信画廊管理", orderNum = 1, type = "1", icon = "fa fa-image")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<Gallery> datas = galleryService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page));
        model.addAttribute("datas", datas);
        return "admin/gallery/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name = "添加微信画廊", orderNum = 2, icon="fa fa-plus")
    @RequestMapping(value="add", method= RequestMethod.GET)
    public String add(Model model, HttpServletRequest request) {
        model.addAttribute("gallery", new Gallery());
        return "admin/gallery/add";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="add", method=RequestMethod.POST)
    public String add(Model model, Gallery gallery, HttpServletRequest request, @RequestParam("file")MultipartFile[] files) {
        if(TokenTools.isNoRepeat(request)) { //不是重复提交
            if(files!=null && files.length>=1) {
                BufferedOutputStream bw = null;
                try {
                    String fileName = files[0].getOriginalFilename();
                    if(fileName!=null && !"".equalsIgnoreCase(fileName.trim()) && NormalTools.isImageFile(fileName)) {
                        File outFile = new File(configTools.getUploadPath(PATH_PRE) + "/" + UUID.randomUUID().toString()+ NormalTools.getFileType(fileName));
                        gallery.setPicPath(outFile.getAbsolutePath().replace(configTools.getUploadPath(), "\\"));
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
            galleryService.save(gallery);
        }
        return "redirect:/admin/gallery/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name="修改微信画廊", orderNum=3, icon = "fa fa-pencil")
    @RequestMapping(value="update/{id}", method=RequestMethod.GET)
    public String update(Model model, @PathVariable Integer id, HttpServletRequest request) {
        Gallery g = galleryService.findOne(id);
        model.addAttribute("gallery", g);
        return "admin/gallery/update";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="update/{id}", method=RequestMethod.POST)
    public String update(Model model, @PathVariable Integer id, Gallery gallery, HttpServletRequest request, @RequestParam("file")MultipartFile[] files) {
        if(TokenTools.isNoRepeat(request)) {
            Gallery g = galleryService.findOne(id);
            MyBeanUtils.copyProperties(gallery, g, new String[]{"id"});

            if(files!=null && files.length>=1) {
                BufferedOutputStream bw = null;
                try {
                    String fileName = files[0].getOriginalFilename();
                    if(fileName!=null && !"".equalsIgnoreCase(fileName.trim()) && NormalTools.isImageFile(fileName)) {

                        File oldFile = new File(configTools.getUploadPath()+g.getPicPath());
                        if(oldFile.exists()) {oldFile.delete();}

                        File outFile = new File(configTools.getUploadPath(PATH_PRE) + "/" + UUID.randomUUID().toString()+ NormalTools.getFileType(fileName));
                        g.setPicPath(outFile.getAbsolutePath().replace(configTools.getUploadPath(), "\\"));
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

            galleryService.save(g);
        }
        return "redirect:/admin/gallery/list";
    }

    @AdminAuth(name="删除微信画廊", orderNum=4, icon = "fa fa-remove")
    @RequestMapping(value="delete/{id}", method=RequestMethod.POST)
    public @ResponseBody
    String delete(@PathVariable Integer id) {
        try {

            Gallery g = galleryService.findOne(id);
            File oldFile = new File(configTools.getUploadPath()+g.getPicPath());
            if(oldFile.exists()) {oldFile.delete();}

            galleryService.delete(id);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }
}
