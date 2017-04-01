package com.zslin.wx.controller;

import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.web.model.Article;
import com.zslin.web.service.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/4 0:37.
 */
@Controller
@RequestMapping(value = "weixin/article")
public class WeixinArticleController {

    @Autowired
    private IArticleService articleService;

    @GetMapping(value = "list")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<Article> datas = articleService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("createDate_d")));
        model.addAttribute("datas", datas);
        return "weixin/article/list";
    }

    @GetMapping(value = "detail")
    public String detail(Model model, Integer id, HttpServletRequest request) {
        Article article = articleService.findOne(id);
        model.addAttribute("article", article);
        return "weixin/article/detail";
    }
}
