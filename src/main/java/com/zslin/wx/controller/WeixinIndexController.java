package com.zslin.wx.controller;

import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.repository.SimpleSpecificationBuilder;
import com.zslin.client.service.IOrdersService;
import com.zslin.web.service.ICategoryService;
import com.zslin.web.service.ICommentService;
import com.zslin.web.service.IFeedbackService;
import com.zslin.web.service.IGalleryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/24 15:07.
 */
@Controller
@RequestMapping(value = "weixin")
public class WeixinIndexController {

    @Autowired
    private IGalleryService galleryService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IOrdersService ordersService;

    @Autowired
    private IFeedbackService feedbackService;

    @Autowired
    private ICommentService commentService;

    @GetMapping(value = "index")
    public String index(Model model, HttpServletRequest request) {

        model.addAttribute("categoryList", categoryService.findByOrder()); //分类
        model.addAttribute("galleryList", galleryService.findShow()); //微信画廊
        model.addAttribute("ordersList", ordersService.findAll(SimplePageBuilder.generate(0, 6, SimpleSortBuilder.generateSort("id_d")))); //最新订单
        SimpleSpecificationBuilder builder = new SimpleSpecificationBuilder("status", "eq", "1");
//        model.addAttribute("feedbackList", feedbackService.findAll(builder.generate(), SimplePageBuilder.generate(0, 6, SimpleSortBuilder.generateSort("id_d")))); //最新反馈
        model.addAttribute("commentList", commentService.findAll(builder.generate(), SimplePageBuilder.generate(0, 6, SimpleSortBuilder.generateSort("id_d")))); //最新点评
        return "weixin/index/index";
    }
}
