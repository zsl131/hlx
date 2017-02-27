package com.zslin.admin.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.annotations.Token;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.tools.ConfigTools;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.basic.tools.NormalTools;
import com.zslin.basic.tools.TokenTools;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.web.model.Category;
import com.zslin.web.model.Food;
import com.zslin.web.service.ICategoryService;
import com.zslin.web.service.IFoodService;
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
import java.util.List;
import java.util.UUID;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/25 23:37.
 */
@Controller
@RequestMapping(value = "admin/food")
@AdminAuth(name = "食品管理", psn = "应用管理", orderNum = 9, porderNum = 1, pentity = 0, icon = "fa fa-cutlery")
public class AdminFoodController {

    @Autowired
    private IFoodService foodService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private ConfigTools configTools;

    private static final String PATH_PRE = "food/";

    @GetMapping(value = "list")
    @AdminAuth(name = "食品管理", type = "1", orderNum = 1, icon = "fa fa-cutlery")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<Food> datas = foodService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page));
        model.addAttribute("datas", datas);
        List<Category> cateList = categoryService.findAll();
        model.addAttribute("cateList", cateList);
        return "admin/food/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name = "添加食品", orderNum = 2, icon="fa fa-plus")
    @RequestMapping(value="add", method= RequestMethod.GET)
    public String add(Model model, HttpServletRequest request) {
        model.addAttribute("food", new Food());
        List<Category> cateList = categoryService.findAll();
        model.addAttribute("cateList", cateList);
        return "admin/food/add";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="add", method=RequestMethod.POST)
    public String add(Model model, Food food, HttpServletRequest request, @RequestParam("file")MultipartFile[] files) {
        if(TokenTools.isNoRepeat(request)) { //不是重复提交
            if(files!=null && files.length>=1) {
                BufferedOutputStream bw = null;
                try {
                    String fileName = files[0].getOriginalFilename();
                    if(fileName!=null && !"".equalsIgnoreCase(fileName.trim()) && NormalTools.isImageFile(fileName)) {
                        File outFile = new File(configTools.getUploadPath(PATH_PRE) + "/" + UUID.randomUUID().toString()+ NormalTools.getFileType(fileName));
                        food.setPicPath(outFile.getAbsolutePath().replace(configTools.getUploadPath(), "\\"));
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
            foodService.save(food);
        }
        return "redirect:/admin/food/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name="修改食品信息", orderNum=3, icon = "fa fa-pencil")
    @RequestMapping(value="update/{id}", method=RequestMethod.GET)
    public String update(Model model, @PathVariable Integer id, HttpServletRequest request) {
        Food f = foodService.findOne(id);
        model.addAttribute("food", f);
        List<Category> cateList = categoryService.findAll();
        model.addAttribute("cateList", cateList);
        return "admin/food/update";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="update/{id}", method=RequestMethod.POST)
    public String update(Model model, @PathVariable Integer id, Food food, HttpServletRequest request, @RequestParam("file")MultipartFile[] files) {
        if(TokenTools.isNoRepeat(request)) {
            Food f = foodService.findOne(id);
            MyBeanUtils.copyProperties(food, f, new String[]{"commentCount", "goodCount"});

            if(files!=null && files.length>=1) {
                BufferedOutputStream bw = null;
                try {
                    String fileName = files[0].getOriginalFilename();
                    if(fileName!=null && !"".equalsIgnoreCase(fileName.trim()) && NormalTools.isImageFile(fileName)) {

                        File oldFile = new File(configTools.getUploadPath()+f.getPicPath());
                        if(oldFile.exists()) {oldFile.delete();}

                        File outFile = new File(configTools.getUploadPath(PATH_PRE) + "/" + UUID.randomUUID().toString()+ NormalTools.getFileType(fileName));
                        f.setPicPath(outFile.getAbsolutePath().replace(configTools.getUploadPath(), "\\"));
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

            foodService.save(f);
        }
        return "redirect:/admin/food/list";
    }

    @AdminAuth(name="删除食品", orderNum=4, icon = "fa fa-remove")
    @RequestMapping(value="delete/{id}", method=RequestMethod.POST)
    public @ResponseBody
    String delete(@PathVariable Integer id) {
        try {
            Food f = foodService.findOne(id);
            File oldFile = new File(configTools.getUploadPath()+f.getPicPath());
            if(oldFile.exists()) {oldFile.delete();}

            foodService.delete(f);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }
}