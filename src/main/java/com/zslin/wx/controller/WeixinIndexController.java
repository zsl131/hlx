package com.zslin.wx.controller;

import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.repository.SimpleSpecificationBuilder;
import com.zslin.basic.tools.DateTools;
import com.zslin.client.service.IOrdersService;
import com.zslin.web.service.*;
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
    private IBuffetOrderService buffetOrderService;

    @Autowired
    private IFeedbackService feedbackService;

    @Autowired
    private ICommentService commentService;

    @Autowired
    private IPriceService priceService;

    @Autowired
    private IBuffetOrderDetailService buffetOrderDetailService;

    @GetMapping(value = "index")
    public String index(Model model, String storeSn, HttpServletRequest request) {
        storeSn = (storeSn==null||"".equals(storeSn.trim()))?"hlx":storeSn;
        model.addAttribute("categoryList", categoryService.findByOrder(storeSn)); //分类
        model.addAttribute("galleryList", galleryService.findShow()); //微信画廊
        model.addAttribute("ordersList", buffetOrderService.findAll(SimplePageBuilder.generate(0, 6, SimpleSortBuilder.generateSort("id_d")))); //最新订单
        model.addAttribute("ordersCount", buffetOrderService.queryCount());
        model.addAttribute("ordersCountYestoday", buffetOrderDetailService.queryCount(DateTools.plusDay(-1, "yyyy-MM-dd")));
        model.addAttribute("ordersCountToday", buffetOrderDetailService.queryCount(DateTools.plusDay(0, "yyyy-MM-dd")));
        SimpleSpecificationBuilder builder = new SimpleSpecificationBuilder("status", "eq", "1");
//        model.addAttribute("feedbackList", feedbackService.findAll(builder.generate(), SimplePageBuilder.generate(0, 6, SimpleSortBuilder.generateSort("id_d")))); //最新反馈
        model.addAttribute("commentList", commentService.findAll(builder.generate(), SimplePageBuilder.generate(0, 6, SimpleSortBuilder.generateSort("id_d")))); //最新点评
        model.addAttribute("price", priceService.loadOne());
        return "weixin/index/index";
    }
}
