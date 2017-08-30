package com.zslin.admin.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.annotations.Token;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.basic.tools.NormalTools;
import com.zslin.basic.tools.TokenTools;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.web.model.Income;
import com.zslin.web.service.IIncomeService;
import com.zslin.wx.tools.IncomeNoticeTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * Created by 钟述林 393156105@qq.com on 2017/8/30 9:15.
 */
@Controller
@RequestMapping(value = "admin/income")
@AdminAuth(name = "收入管理", psn = "财务管理", orderNum = 6, porderNum = 1, pentity = 0, icon = "fa fa-cny")
public class AdminIncomeController {

    @Autowired
    private IIncomeService incomeService;

    @Autowired
    private IncomeNoticeTools incomeNoticeTools;

    @GetMapping(value = "list")
    @AdminAuth(name = "收入管理", orderNum = 1, type = "1", icon = "fa fa-cny")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<Income> datas = incomeService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("comeDay_d")));
        model.addAttribute("datas", datas);
        return "admin/income/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name = "添加收入", orderNum = 2, icon="fa fa-plus")
    @RequestMapping(value="add", method= RequestMethod.GET)
    public String add(Model model, HttpServletRequest request) {
        model.addAttribute("income", new Income());
        return "admin/income/add";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="add", method=RequestMethod.POST)
    public String add(Model model, Income income, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) { //不是重复提交
//            income.setTotalMoney(income.getCash()+income.getAlipay()+income.getFfan()+income.getMarket()+income.getMeituan()+income.getMember()+income.getOther()+income.getWeixin());
            Float totalMoney = income.getCash()+income.getAlipay()+income.getFfan()+income.getMarket()+income.getMeituan()+income.getMember()+income.getOther()+income.getWeixin();
            BigDecimal bg = new BigDecimal(totalMoney);
            income.setTotalMoney(bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            String comeDay = income.getComeDay();
            comeDay = comeDay==null||"".equalsIgnoreCase(comeDay)? NormalTools.curDate("yyyyMMdd"):comeDay;
            String comeYear = comeDay.substring(0, 4);
            String comeMonth = comeDay.substring(0, 6);
            income.setComeDay(comeDay);
            income.setComeMonth(comeMonth);
            income.setComeYear(comeYear);
            income.setCreateDay(NormalTools.curDate("yyyy-MM-dd"));
            incomeService.save(income);

            incomeNoticeTools.notice(income); //通知
        }
        return "redirect:/admin/income/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name="修改收入", orderNum=3, icon = "fa fa-pencil")
    @RequestMapping(value="update/{id}", method=RequestMethod.GET)
    public String update(Model model, @PathVariable Integer id, HttpServletRequest request) {
        Income in = incomeService.findOne(id);
        model.addAttribute("income", in);
        return "admin/income/update";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="update/{id}", method=RequestMethod.POST)
    public String update(Model model, @PathVariable Integer id, Income income, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) {
            Income in = incomeService.findOne(id);
            MyBeanUtils.copyProperties(income, in, new String[]{"id", "totalMoney"});
//            in.setTotalMoney(income.getCash()+income.getAlipay()+income.getFfan()+income.getMarket()+income.getMeituan()+income.getMember()+income.getOther()+income.getWeixin());
            Float totalMoney = income.getCash()+income.getAlipay()+income.getFfan()+income.getMarket()+income.getMeituan()+income.getMember()+income.getOther()+income.getWeixin();
            BigDecimal bg = new BigDecimal(totalMoney);
            income.setTotalMoney(bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            String comeDay = income.getComeDay();
            comeDay = comeDay==null||"".equalsIgnoreCase(comeDay)? NormalTools.curDate("yyyyMMdd"):comeDay;
            String comeYear = comeDay.substring(0, 4);
            String comeMonth = comeDay.substring(0, 6);
            in.setComeDay(comeDay);
            in.setComeMonth(comeMonth);
            in.setComeYear(comeYear);
            incomeService.save(in);

            incomeNoticeTools.notice(in); //通知
        }
        return "redirect:/admin/income/list";
    }
}
