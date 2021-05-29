package com.zslin.admin.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.annotations.Token;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.*;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.web.model.Article;
import com.zslin.web.service.IArticleService;
import com.zslin.wx.tools.AccountTools;
import com.zslin.wx.tools.EventTools;
import com.zslin.wx.tools.WeixinXmlTools;
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
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/25 23:11.
 */
@Controller
@RequestMapping(value = "admin/article")
@AdminAuth(name = "文章管理", psn = "应用管理", orderNum = 6, porderNum = 1, pentity = 0, icon = "fa fa-file-word-o")
public class AdminArticleController {

    @Autowired
    private IArticleService articleService;

    @Autowired
    private ConfigTools configTools;

    @Autowired
    private AccountTools accountTools;

    @Autowired
    private EventTools eventTools;

    private static final String PATH_PRE = "article";

    @PostMapping(value = "sendArticle")
    public @ResponseBody String sendArticle(Integer id, String type) {
        Article art = articleService.findOne(id);
        List<String> openids ;
        if("-1".equals(type)) {
            openids = accountTools.getAllOpenids();
        } else {
            openids = accountTools.getOpenid(type);
        }

        String url = WeixinXmlTools.buildUrl("", art);
        //http://zthlx.zslin.com/weixin/article/detail?id=1
        //"/weixin/article/detail?id="+id
        eventTools.eventRemind(openids, art.getTitle(), "新公告推送", DateTools.date2Str(new Date()), art.getGuide(), url);
        return "1";
    }

    @GetMapping(value = "list")
    @AdminAuth(name = "文章管理", orderNum = 1, type = "1", icon = "fa fa-file-word-o")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<Article> datas = articleService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("orderNo")));
        model.addAttribute("datas", datas);
        return "admin/article/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name = "添加文章", orderNum = 2, icon="fa fa-plus")
    @RequestMapping(value="add", method= RequestMethod.GET)
    public String add(Model model, HttpServletRequest request) {
        model.addAttribute("article", new Article());
        return "admin/article/add";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="add", method=RequestMethod.POST)
    public String add(Model model, Article article, HttpServletRequest request, @RequestParam("file")MultipartFile[] files) {
        if(TokenTools.isNoRepeat(request)) { //不是重复提交
            if(files!=null && files.length>=1) {
                BufferedOutputStream bw = null;
                try {
                    String fileName = files[0].getOriginalFilename();
                    if(fileName!=null && !"".equalsIgnoreCase(fileName.trim()) && NormalTools.isImageFile(fileName)) {
                        File outFile = new File(configTools.getFilePath(PATH_PRE) + File.separator + UUID.randomUUID().toString()+ NormalTools.getFileType(fileName));
                        article.setPicPath(outFile.getAbsolutePath().replace(configTools.getFilePath(), File.separator));
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
            articleService.save(article);
        }
        return "redirect:/admin/article/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name="修改文章", orderNum=3, icon = "fa fa-pencil")
    @RequestMapping(value="update/{id}", method=RequestMethod.GET)
    public String update(Model model, @PathVariable Integer id, HttpServletRequest request) {
        Article a = articleService.findOne(id);
        model.addAttribute("article", a);
        return "admin/article/update";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="update/{id}", method=RequestMethod.POST)
    public String update(Model model, @PathVariable Integer id, Article article, HttpServletRequest request, @RequestParam("file")MultipartFile[] files) {
        if(TokenTools.isNoRepeat(request)) {
            Article a = articleService.findOne(id);
            MyBeanUtils.copyProperties(article, a, new String[]{"id"});

            if(files!=null && files.length>=1) {
                BufferedOutputStream bw = null;
                try {
                    String fileName = files[0].getOriginalFilename();
                    if(fileName!=null && !"".equalsIgnoreCase(fileName.trim()) && NormalTools.isImageFile(fileName)) {

                        File oldFile = new File(configTools.getFilePath()+a.getPicPath());
                        if(oldFile.exists()) {oldFile.delete();}

                        File outFile = new File(configTools.getFilePath(PATH_PRE) + File.separator + UUID.randomUUID().toString()+ NormalTools.getFileType(fileName));
                        a.setPicPath(outFile.getAbsolutePath().replace(configTools.getFilePath(), File.separator));
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

            articleService.save(a);
        }
        return "redirect:/admin/article/list";
    }

    @AdminAuth(name="删除文章", orderNum=4, icon = "fa fa-remove")
    @RequestMapping(value="delete/{id}", method=RequestMethod.POST)
    public @ResponseBody
    String delete(@PathVariable Integer id) {
        try {

            Article a = articleService.findOne(id);
            File oldFile = new File(configTools.getFilePath()+a.getPicPath());
            if(oldFile.exists()) {oldFile.delete();}

            articleService.delete(id);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }
}
