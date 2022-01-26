package com.zslin.wx.controller;

import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.repository.SpecificationOperator;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.basic.tools.NormalTools;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.business.dao.IBusinessDetailDao;
import com.zslin.business.model.BusinessDetail;
import com.zslin.finance.dao.IFinanceDetailDao;
import com.zslin.finance.dao.IFinancePersonalDao;
import com.zslin.finance.model.FinanceDetail;
import com.zslin.finance.model.FinancePersonal;
import com.zslin.multi.dao.IStoreDao;
import com.zslin.multi.model.Store;
import com.zslin.web.dto.IncomeDto;
import com.zslin.web.model.Account;
import com.zslin.web.model.Income;
import com.zslin.web.service.IAccountService;
import com.zslin.web.service.IIncomeService;
import com.zslin.weixin.annotation.HasTemplateMessage;
import com.zslin.wx.tools.SessionTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "wx/business")
@HasTemplateMessage
public class WeixinBusinessController {

    @Autowired
    private IAccountService accountService;

    @Autowired
    private IFinancePersonalDao financePersonalDao;

    @Autowired
    private IFinanceDetailDao financeDetailDao;

    @Autowired
    private IStoreDao storeDao;

    @Autowired
    private IBusinessDetailDao businessDetailDao;

    @Autowired
    private IIncomeService incomeService;

    @GetMapping(value = "show")
    public String show(Integer id, Model model, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);

        Account account = accountService.findByOpenid(openid);
//        month = (month==null || "".equals(month))? NormalTools.preMonth():month;
        FinancePersonal personal = financePersonalDao.findByOpenid(openid);
        if(personal == null || !"1".equals(personal.getIsPartner())) {
            return "redirect:/wx/business/error?errCode=-1";
        }
        BusinessDetail bd = businessDetailDao.findOne(id);
        if(!personal.getPartStores().contains(bd.getStoreSn())) {
            return "redirect:/wx/business/error?errCode=-1";
        }
        model.addAttribute("detail", bd);
        String storeSn = bd.getStoreSn(); String month = bd.getTargetMonth();
        IncomeDto incomeDto = incomeService.queryByMonth(storeSn, month);
        List<Income> incomeList = incomeService.findByMonth(storeSn, month);
        List<FinanceDetail> financeDetailList = financeDetailDao.findDetailByStoreSn(storeSn, month);
        model.addAttribute("financeList", financeDetailList);
        model.addAttribute("incomeDto", incomeDto);
        model.addAttribute("incomeList", incomeList);
        model.addAttribute("personal", personal);
        return "weixin/business/show";
    }

    @GetMapping(value = "addDetail")
    public String addDetail(Model model, String storeSn, String month, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);

        Account account = accountService.findByOpenid(openid);
        month = (month==null || "".equals(month))? NormalTools.preMonth():month;
        FinancePersonal personal = financePersonalDao.findByOpenid(openid);
        if(!FinancePersonal.TYPE_BOSS.equals(personal.getType())) {
            return "redirect:/wx/business/error?errCode=-2";
        }

        String preMonth = NormalTools.preMonthByMonth(month);
        BusinessDetail preDetail = businessDetailDao.findByStoreSnAndTargetMonth(storeSn, preMonth); //上个月的经营数据

        BusinessDetail detail = businessDetailDao.findByStoreSnAndTargetMonth(storeSn, month);
        if(detail==null) {detail = new BusinessDetail();}
        model.addAttribute("curMonth", month);
        model.addAttribute("detail", detail);
        model.addAttribute("preDetail", preDetail);
        model.addAttribute("personal", personal);
        model.addAttribute("storeSn", storeSn);
        model.addAttribute("store", storeDao.findBySn(storeSn));
        IncomeDto incomeDto = incomeService.queryByMonth(storeSn, month);
        Double payMoney = NormalTools.retain2Decimal(financeDetailDao.findTotalMoney(storeSn, month));
        Double monthSurplus = NormalTools.retain2Decimal((incomeDto==null?0:incomeDto.getTotalMoney()) - payMoney); //本月结余
        model.addAttribute("payMoney", payMoney);
        model.addAttribute("incomeDto", incomeDto);
        model.addAttribute("monthSurplus", monthSurplus);

        return "weixin/business/addDetail";
    }

    //(surplusMoney=0.0, preMonthMoney=8522.0, accountMoney=0.0, settleMoney=120000.0)
    @PostMapping(value = "addDetail")
    public String addDetail(Model model, BusinessDetail detail) {

        double gotMoney = detail.getGotMoney();
        double paidMoney = detail.getPaidMoney();
        double curSurplus = gotMoney - paidMoney;
        if(curSurplus==0) {detail.setFlag("0");}
        else if(curSurplus>0) {detail.setFlag("1");}
        else if(curSurplus<0) {detail.setFlag("-1");}
        detail.setCreateDay(NormalTools.curDate());
        detail.setCreateLong(System.currentTimeMillis());
        detail.setCreateTime(NormalTools.curDatetime());
        detail.setSurplusMoney(curSurplus);
        detail.setTargetYear(detail.getTargetMonth().substring(0, 4));
        Store store = storeDao.findBySn(detail.getStoreSn());
        detail.setStoreName(store.getName());

        double accountMoney = NormalTools.retain2Decimal(gotMoney - paidMoney - detail.getSettleMoney() + detail.getPreMonthMoney());
//        System.out.println("================="+accountMoney);
        detail.setAccountMoney(accountMoney);

        BusinessDetail bd = businessDetailDao.findByStoreSnAndTargetMonth(detail.getStoreSn(), detail.getTargetMonth());
        if(bd==null) {
            bd = new BusinessDetail();
            bd.setCreateDay(NormalTools.curDate());
            bd.setCreateLong(System.currentTimeMillis());
            bd.setCreateTime(NormalTools.curDatetime());
        }
        MyBeanUtils.copyProperties(detail, bd);
//        System.out.println(bd);

        businessDetailDao.save(bd);

        return "redirect:/wx/business/index";
    }

    @GetMapping(value = "index")
    public String index(Model model, Integer page, String storeSn, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);

        Account account = accountService.findByOpenid(openid);
        FinancePersonal personal = financePersonalDao.findByOpenid(openid);

        model.addAttribute("account", account);
        model.addAttribute("personal", personal);
        if(personal!=null) {
            if(!"1".equals(personal.getIsPartner())) {
                //没有签名，则跳转到签名页面
                return "redirect:/wx/business/error?errCode=-1";
            }
            List<Store> storeList ;

            try {
                String storeSns = personal.getPartStores();
                storeList = storeDao.findBySns(storeSns.split(";"));
                if(storeList!=null && storeList.size()>0) {
                    storeSn = (storeSn==null || "".equals(storeSn.trim()))?storeList.get(0).getSn():storeSn;
                    model.addAttribute("curStoreSn", storeSn);
                }
            } catch (Exception e) {
                storeList = new ArrayList<>();
            }
            if(storeSn!=null && !"".equals(storeSn)) {
                Page<BusinessDetail> datas = businessDetailDao.findAll(ParamFilterUtil.getInstance().buildSearch(model, request,
                        new SpecificationOperator("storeSn", "eq", storeSn)),
                        SimplePageBuilder.generate(page, 33, SimpleSortBuilder.generateSort("targetMonth_d")));
                model.addAttribute("datas", datas);
            }
            model.addAttribute("storeList", storeList);
        } else {
            return "redirect:/wx/business/error?errCode=-1";
        }


        return "weixin/business/index";
    }

    @GetMapping(value = "error")
    public String error(Model model, String errCode, HttpServletRequest request) {
        String msg = "无权访问";
        if("-1".equals(errCode)) {
            msg = "没有访问权限";
        } else if("-2".equals(errCode)) {
            msg = "非权限用户";
        }
        model.addAttribute("msg", msg);
        return "weixin/business/error";
    }
}
