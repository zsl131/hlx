package com.zslin.wx.controller;

import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.basic.tools.NormalTools;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.web.model.Account;
import com.zslin.web.model.Income;
import com.zslin.web.service.IAccountService;
import com.zslin.web.service.IIncomeService;
import com.zslin.wx.tools.AccountTools;
import com.zslin.wx.tools.IncomeNoticeTools;
import com.zslin.wx.tools.SessionTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by 钟述林 393156105@qq.com on 2017/8/30 10:58.
 */
@Controller
@RequestMapping(value = "wx/income")
public class WeixinIncomeController {

    @Autowired
    private IIncomeService incomeService;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private IncomeNoticeTools incomeNoticeTools;

    @GetMapping(value = "list")
    public String list(Model model, Integer page, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        Account a = accountService.findByOpenid(openid);
        if(a!=null && AccountTools.isPartner(a.getType())) { //只有股东才可以看
            String month = request.getParameter("filter_comeMonth");
            Page<Income> datas = incomeService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                    SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("comeDay_d")));
            month = (month==null||"".equalsIgnoreCase(month) || month.indexOf("-")<0)?NormalTools.curDate("yyyyMM"):month.substring(month.indexOf("-")+1, month.length());
            Double avg = incomeService.average(month);
            Integer moreThan = incomeService.moreThan(month, 20000d);
            Double totalMoney = incomeService.totalMoney(month);
            model.addAttribute("avg", avg==null?0:avg);
            model.addAttribute("moreThan", moreThan==null?0:moreThan);
            model.addAttribute("totalMoney", totalMoney==null?0:totalMoney);
            model.addAttribute("datas", datas);
            model.addAttribute("month", month);
        }
        return "weixin/income/list";
    }

    @GetMapping(value = "show")
    public String show(Model model, Integer id, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        Account a = accountService.findByOpenid(openid);
        if(a!=null && AccountTools.isPartner(a.getType())) { //只有股东才可以看
            Income income = incomeService.findOne(id);
            model.addAttribute("income", income);
            String month = income.getComeMonth();
            Double avg = incomeService.average(month);
            Integer moreThan = incomeService.moreThan(month, 20000d);
            Double totalMoney = incomeService.totalMoney(month);
            model.addAttribute("avg", avg==null?0:avg);
            model.addAttribute("moreThan", moreThan==null?0:moreThan);
            model.addAttribute("totalMoney", totalMoney==null?0:totalMoney);
        }
        return "weixin/income/show";
    }

    @GetMapping(value = "add")
    public String add(Model model, HttpServletRequest request) {
        model.addAttribute("income", new Income());
        return "weixin/income/add";
    }

    @PostMapping(value="add")
    public String add(Model model, Income income, HttpServletRequest request) {
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
        return "redirect:/wx/income/list";
    }

    @GetMapping(value="update/{id}")
    public String update(Model model, @PathVariable Integer id, HttpServletRequest request) {
        Income in = incomeService.findOne(id);
        model.addAttribute("income", in);
        return "weixin/income/update";
    }

    @PostMapping(value="update/{id}")
    public String update(Model model, @PathVariable Integer id, Income income, HttpServletRequest request) {
        Income in = incomeService.findOne(id);
        MyBeanUtils.copyProperties(income, in, new String[]{"id", "totalMoney"});
//        in.setTotalMoney(income.getCash()+income.getAlipay()+income.getFfan()+income.getMarket()+income.getMeituan()+income.getMember()+income.getOther()+income.getWeixin());
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
        return "redirect:/wx/income/list";
    }
}
