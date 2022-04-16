package com.zslin.wx.controller;

import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.repository.SpecificationOperator;
import com.zslin.basic.tools.DateTools;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.basic.tools.NormalTools;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.business.dao.IBusinessDetailDao;
import com.zslin.business.dto.BusinessDto;
import com.zslin.business.dto.BusinessMoneyDto;
import com.zslin.business.model.BusinessDetail;
import com.zslin.business.tools.BusinessTools;
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
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @Autowired
    private BusinessTools businessTools;

    @PostMapping(value = "saveDetail")
    public @ResponseBody
    String saveDetail(String storeSn, String month, Double preMonthMoney, String publishFlag, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);

        preMonthMoney = preMonthMoney==null?0:preMonthMoney; //上月结余
        FinancePersonal personal = financePersonalDao.findByOpenid(openid);

        if(!FinancePersonal.TYPE_BOSS.equals(personal.getType())) {
            return "-2"; //无权限
        }
        List<BusinessDto> businessDtoList = businessTools.buildDto(storeSn, month, preMonthMoney);
        BusinessMoneyDto moneyDto = businessTools.buildMoney(businessDtoList);
        BusinessDetail detail = businessDetailDao.findByStoreSnAndTargetMonth(storeSn, month);
        Store store = storeDao.findBySn(storeSn);
        if(detail==null) {
            detail = new BusinessDetail();
            detail.setCreateDay(NormalTools.curDate("yyyy-MM"));
            detail.setCreateTime(NormalTools.curDatetime());
            detail.setCreateLong(System.currentTimeMillis());
            detail.setStoreSn(store.getSn());
            detail.setTargetYear(month.substring(0, 4));
            detail.setTargetMonth(month);
        }
        detail.setPreMonthMoney(preMonthMoney);
        detail.setStoreName(store.getName());
        detail.setGotMoney(moneyDto.getGotMoney());
        detail.setPaidMoney(moneyDto.getPaidMoney());
        detail.setSurplusMoney(NormalTools.retain2Decimal(preMonthMoney+moneyDto.getGotMoney()-moneyDto.getPaidMoney()));
        detail.setStatus(publishFlag); //是否公开 0-不公布；1-公布
        businessDetailDao.save(detail);

        businessTools.rebuildBusinessDetailForLoop(storeSn, month); //当修改了某月数据后，之后的数据也相应变化

//        System.out.println(moneyDto);
//        System.out.println(detail);
//        System.out.println(businessDtoList);

        return "1";
    }

    /** 只供老板查询，用于公布前对账 */
    @GetMapping(value = "showByBoss")
    public String showByBoss(Model model, String storeSn, String month, Double preMonthMoney, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);

        //Account account = accountService.findByOpenid(openid);
        month = (month==null || "".equals(month))? NormalTools.preMonth():month;
        FinancePersonal personal = financePersonalDao.findByOpenid(openid);

        //if(!FinancePersonal.TYPE_BOSS.equals(personal.getType()))
        if(personal==null || !"1".equals(personal.getIsPartner())) {
            return "redirect:/wx/business/error?errCode=-2";
        }

        //上月明细，主要用于获取上月结余
        BusinessDetail detail = businessDetailDao.findByStoreSnAndTargetMonth(storeSn, month);

//        System.out.println("参数： "+preMonthMoney);
//        System.out.println("detail: "+detail);

        double lastSurplusMoney = (detail==null || detail.getPreMonthMoney()==null)?0d:detail.getPreMonthMoney();
//        System.out.println("===lastSurplusMoney==="+ lastSurplusMoney);
        preMonthMoney = preMonthMoney==null?lastSurplusMoney:preMonthMoney;
        if(detail==null && preMonthMoney==0) {
            BusinessDetail preDetail = businessDetailDao.findByStoreSnAndTargetMonth(storeSn, NormalTools.preMonthByMonth(month));
            if(preDetail!=null) {
                preMonthMoney = preDetail.getSurplusMoney(); //
            }
        }
        List<BusinessDto> businessDtoList = businessTools.buildDto(storeSn, month, preMonthMoney);

        model.addAttribute("moneyDto", businessTools.buildMoney(businessDtoList));
        model.addAttribute("businessDtoList", businessDtoList);
        model.addAttribute("detail", detail);
        model.addAttribute("personal", personal);
        model.addAttribute("month", month);
        model.addAttribute("storeSn", storeSn);
        model.addAttribute("preMonthMoney", preMonthMoney);
        model.addAttribute("noEndCount", financeDetailDao.findNoEndCount(storeSn, month));
        model.addAttribute("store", storeDao.findBySn(storeSn));

        return "weixin/business/showByBoss";
    }

    @GetMapping(value = "showNoEnd")
    public String showNoEnd(Model model, String storeSn, String month) {
        List<FinanceDetail> list = financeDetailDao.findNoEnd(storeSn, month);
        model.addAttribute("list", list);
        return "weixin/business/showNoEnd";
    }

    @GetMapping(value = "incomeDetail")
    public String incomeDetail(Model model, String storeSn, String day, HttpServletRequest rerquest) {
        List<Income> incomeList = incomeService.findByComeDay(storeSn, day);
        List<FinanceDetail> financeDetailList = financeDetailDao.findDetailByStoreSnAndTargetDay(storeSn, day);

        model.addAttribute("incomeList", incomeList);
        model.addAttribute("financeDetailList", financeDetailList);
        model.addAttribute("day", day);
        model.addAttribute("storeSn", storeSn);
        model.addAttribute("store", storeDao.findBySn(storeSn));
        return "weixin/business/incomeDetail";
    }

    @GetMapping(value = "show")
    public String show(Integer id, Model model, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);

        //Account account = accountService.findByOpenid(openid);
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
        Double monthSurplus = NormalTools.retain2Decimal(((incomeDto==null || incomeDto.getTotalMoney()==null)?0:incomeDto.getTotalMoney()) - payMoney); //本月结余
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
                        new SpecificationOperator("storeSn", "eq", storeSn),
                        FinancePersonal.TYPE_BOSS.equals(personal.getType())?null:
                        new SpecificationOperator("status", "eq", "1")),
                        SimplePageBuilder.generate(page, 33, SimpleSortBuilder.generateSort("targetMonth_d")));
                model.addAttribute("datas", datas);
            }
            model.addAttribute("storeList", storeList);
        } else {
            return "redirect:/wx/business/error?errCode=-1";
        }
        return "weixin/business/index";
    }

    @PostMapping(value = "publish")
    @ResponseBody
    public String publish(Integer id) {
        try {
            businessDetailDao.updateStatus("1", id);
            return "1";
        } catch (Exception e) {
            return e.getMessage();
        }
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
