package com.zslin.admin.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.annotations.Token;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.ConfigTools;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.basic.tools.NormalTools;
import com.zslin.basic.tools.TokenTools;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.web.model.Category;
import com.zslin.web.service.ICategoryService;
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
@RequestMapping(value = "admin/category")
@AdminAuth(name = "分类管理", psn = "应用管理", orderNum = 6, porderNum = 1, pentity = 0, icon = "fa fa-tasks")
public class AdminCategoryController {

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private ConfigTools configTools;

    private static final String PATH_PRE = "category/";

    @GetMapping(value = "list")
    @AdminAuth(name = "分类管理", orderNum = 1, type = "1", icon = "fa fa-tasks")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<Category> datas = categoryService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("orderNo")));
        model.addAttribute("datas", datas);
        return "admin/category/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name = "添加分类", orderNum = 2, icon="fa fa-plus")
    @RequestMapping(value="add", method= RequestMethod.GET)
    public String add(Model model, HttpServletRequest request) {
        model.addAttribute("category", new Category());
        return "admin/category/add";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="add", method=RequestMethod.POST)
    public String add(Model model, Category category, HttpServletRequest request, @RequestParam("file")MultipartFile[] files) {
        if(TokenTools.isNoRepeat(request)) { //不是重复提交
            if(files!=null && files.length>=1) {
                BufferedOutputStream bw = null;
                try {
                    String fileName = files[0].getOriginalFilename();
                    if(fileName!=null && !"".equalsIgnoreCase(fileName.trim()) && NormalTools.isImageFile(fileName)) {
                        File outFile = new File(configTools.getUploadPath(PATH_PRE) + "/" + UUID.randomUUID().toString()+ NormalTools.getFileType(fileName));
                        category.setPicPath(outFile.getAbsolutePath().replace(configTools.getUploadPath(), "\\"));
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
            categoryService.save(category);
        }
        return "redirect:/admin/category/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name="修改分类", orderNum=3, icon = "fa fa-pencil")
    @RequestMapping(value="update/{id}", method=RequestMethod.GET)
    public String update(Model model, @PathVariable Integer id, HttpServletRequest request) {
        Category c = categoryService.findOne(id);
        model.addAttribute("category", c);
        return "admin/category/update";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="update/{id}", method=RequestMethod.POST)
    public String update(Model model, @PathVariable Integer id, Category category, HttpServletRequest request, @RequestParam("file")MultipartFile[] files) {
        if(TokenTools.isNoRepeat(request)) {
            Category c = categoryService.findOne(id);
            MyBeanUtils.copyProperties(category, c, new String[]{"id"});

            if(files!=null && files.length>=1) {
                BufferedOutputStream bw = null;
                try {
                    String fileName = files[0].getOriginalFilename();
                    if(fileName!=null && !"".equalsIgnoreCase(fileName.trim()) && NormalTools.isImageFile(fileName)) {

                        File oldFile = new File(configTools.getUploadPath()+c.getPicPath());
                        if(oldFile.exists()) {oldFile.delete();}

                        File outFile = new File(configTools.getUploadPath(PATH_PRE) + "/" + UUID.randomUUID().toString()+ NormalTools.getFileType(fileName));
                        c.setPicPath(outFile.getAbsolutePath().replace(configTools.getUploadPath(), "\\"));
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

            categoryService.save(c);
        }
        return "redirect:/admin/category/list";
    }

    @AdminAuth(name="删除分类", orderNum=4, icon = "fa fa-remove")
    @RequestMapping(value="delete/{id}", method=RequestMethod.POST)
    public @ResponseBody
    String delete(@PathVariable Integer id) {
        try {

            Category c = categoryService.findOne(id);
            File oldFile = new File(configTools.getUploadPath()+c.getPicPath());
            if(oldFile.exists()) {oldFile.delete();}

            categoryService.delete(id);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }
}
