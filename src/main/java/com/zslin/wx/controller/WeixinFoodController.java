package com.zslin.wx.controller;

import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.web.model.Food;
import com.zslin.web.service.ICategoryService;
import com.zslin.web.service.IFoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/28 16:45.
 */
@Controller
@RequestMapping(value = "weixin/food")
public class WeixinFoodController {

    @Autowired
    private IFoodService foodService;

    @Autowired
    private ICategoryService categoryService;

    @GetMapping(value = "index")
    public String index(Model model, Integer page, HttpServletRequest request) {
        model.addAttribute("cateList", categoryService.findByOrder());
        Page<Food> datas = foodService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("orderNo")));
        model.addAttribute("datas", datas);
        return "weixin/food/index";
    }
}
