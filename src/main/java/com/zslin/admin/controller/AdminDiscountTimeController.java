package com.zslin.admin.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.annotations.Token;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.ConfigTools;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.basic.tools.TokenTools;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.client.tools.ClientFileTools;
import com.zslin.client.tools.ClientJsonTools;
import com.zslin.web.model.DiscountTime;
import com.zslin.web.service.IDiscountTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 钟述林 393156105@qq.com on 2017/4/1 9:55.
 */
@Controller
@RequestMapping(value = "admin/discountTime")
@AdminAuth(name = "时段折扣管理", psn = "应用管理", orderNum = 6, porderNum = 1, pentity = 0, icon = "fa fa-clock-o")
public class AdminDiscountTimeController {

    @Autowired
    private IDiscountTimeService discountTimeService;

    @Autowired
    private ConfigTools configTools;

    @Autowired
    private ClientFileTools clientFileTools;

    @GetMapping(value = "list")
    @AdminAuth(name = "时段折扣管理", orderNum = 1, type = "1", icon = "fa fa-clock-o")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<DiscountTime> datas = discountTimeService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id_d")));
        model.addAttribute("datas", datas);
        return "admin/discountTime/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name = "添加时段折扣规则", orderNum = 2, icon="fa fa-plus")
    @RequestMapping(value="add", method= RequestMethod.GET)
    public String add(Model model, HttpServletRequest request) {
        model.addAttribute("discountTime", new DiscountTime());
        return "admin/discountTime/add";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="add", method=RequestMethod.POST)
    public String add(Model model, DiscountTime discountTime, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) { //不是重复提交
            try {
                String startClock = discountTime.getStartClock().replaceAll(":", "").replaceAll("：", "");
                discountTime.setStartTime(Integer.parseInt(startClock));

                String endClock = discountTime.getEndClock().replaceAll(":", "").replaceAll("：", "");
                discountTime.setEndTime(Integer.parseInt(endClock));
            } catch (Exception e) {
                e.printStackTrace();
            }
            discountTimeService.save(discountTime);
            send2Client(discountTime, "update");
        }
        return "redirect:/admin/discountTime/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name="修改时段折扣规则", orderNum=3, icon = "fa fa-pencil")
    @RequestMapping(value="update/{id}", method=RequestMethod.GET)
    public String update(Model model, @PathVariable Integer id, HttpServletRequest request) {
        DiscountTime d = discountTimeService.findOne(id);
        model.addAttribute("discountTime", d);
        return "admin/discountTime/update";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="update/{id}", method=RequestMethod.POST)
    public String update(Model model, @PathVariable Integer id, DiscountTime discountTime, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) {
            DiscountTime d = discountTimeService.findOne(id);
            MyBeanUtils.copyProperties(discountTime, d, new String[]{"id"});
            try {
                String startClock = discountTime.getStartClock().replaceAll(":", "").replaceAll("：", "");
                d.setStartTime(Integer.parseInt(startClock));

                String endClock = discountTime.getEndClock().replaceAll(":", "").replaceAll("：", "");
                d.setEndTime(Integer.parseInt(endClock));
            } catch (Exception e) {
                e.printStackTrace();
            }
            discountTimeService.save(d);
            send2Client(d, "update");
        }
        return "redirect:/admin/discountTime/list";
    }

    public void send2Client(DiscountTime discountTime, String action) {
        String content = ClientJsonTools.buildDataJson(ClientJsonTools.buildDiscountTime(discountTime, action));
        clientFileTools.setChangeContext(content, true);
    }
}
