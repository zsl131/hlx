package com.zslin.wx.controller;

import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.repository.SpecificationOperator;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.basic.tools.NormalTools;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.client.tools.ClientFileTools;
import com.zslin.finance.dao.IFinancePersonalDao;
import com.zslin.finance.model.FinancePersonal;
import com.zslin.multi.dao.IStoreDao;
import com.zslin.multi.model.Store;
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
import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    private IFinancePersonalDao financePersonalDao;

    @Autowired
    private IStoreDao storeDao;

    @GetMapping(value = "list")
    public String list(Model model, Integer page, String storeSn, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        Account a = accountService.findByOpenid(openid);
        if(a!=null && AccountTools.isPartner(a.getType())) { //只有股东才可以看
            String month = request.getParameter("filter_comeMonth");
            storeSn = (storeSn ==null || "".equals(storeSn.trim()))? ClientFileTools.HLX_SN:storeSn;
            Page<Income> datas = incomeService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request,
                    new SpecificationOperator("storeSn", "eq", storeSn)),
                    SimplePageBuilder.generate(page, 33, SimpleSortBuilder.generateSort("comeDay_d")));
            month = (month==null||"".equalsIgnoreCase(month) || month.indexOf("-")<0)?NormalTools.curDate("yyyyMM"):month.substring(month.indexOf("-")+1, month.length());

            Double avg = incomeService.average(storeSn, month);
            Integer moreThan = incomeService.moreThan(storeSn, month, 25000d);
            Double totalMoney = incomeService.totalMoney(storeSn, month);

            model.addAttribute("avg", avg==null?0:avg);
            model.addAttribute("moreThan", moreThan==null?0:moreThan);
            model.addAttribute("totalMoney", totalMoney==null?0:totalMoney);
            model.addAttribute("datas", datas);
            model.addAttribute("month", month);
            model.addAttribute("storeName", ClientFileTools.BUILD_STORE_NAME(storeSn)); //店铺名称
            model.addAttribute("storeSn", storeSn);
            model.addAttribute("storeList", storeDao.findByStatus("1"));
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
            Double avg = incomeService.average(income.getStoreSn(), month);
            Integer moreThan = incomeService.moreThan(income.getStoreSn(), month, 20000d);
            Double totalMoney = incomeService.totalMoney(income.getStoreSn(), month);
            model.addAttribute("avg", avg==null?0:avg);
            model.addAttribute("moreThan", moreThan==null?0:moreThan);
            model.addAttribute("totalMoney", totalMoney==null?0:totalMoney);
        }
        return "weixin/income/show";
    }

    @GetMapping(value = "add")
    public String add(Model model, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        FinancePersonal personal = financePersonalDao.findByOpenid(openid);
        model.addAttribute("income", new Income());
        List<Store> storeList = null;
        if(personal==null || personal.getStoreSn()==null || "".equals(personal.getStoreSn())) {
            storeList = storeDao.findByStatus("1");
        } else {
            storeList = new ArrayList<>();
            Store s = new Store();
            s.setName(personal.getStoreName());
            s.setSn(personal.getStoreSn());
            storeList.add(s);
        }
        model.addAttribute("storeList", storeList);
        model.addAttribute("personal", personal);

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
        income.setCreateTime(NormalTools.curDatetime());

        try {
            String openid = SessionTools.getOpenid(request);
            Account account = accountService.findByOpenid(openid);
            income.setOpenid(openid);
            income.setNickname(account.getNickname());
        } catch (Exception e) {
//            e.printStackTrace();
        }

        incomeService.save(income);

//        incomeNoticeTools.notice(income); //通知
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
        in.setTotalMoney(bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
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
