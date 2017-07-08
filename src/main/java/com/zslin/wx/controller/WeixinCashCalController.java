package com.zslin.wx.controller;

import com.zslin.admin.dto.MyTicketDto;
import com.zslin.admin.dto.MyTimeDto;
import com.zslin.admin.tools.MyTicketTools;
import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.tools.NormalTools;
import com.zslin.web.model.Account;
import com.zslin.web.model.BuffetOrder;
import com.zslin.web.model.Prize;
import com.zslin.web.service.*;
import com.zslin.wx.tools.AccountTools;
import com.zslin.wx.tools.SessionTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by 钟述林 393156105@qq.com on 2017/6/24 14:16.
 */
@Controller
@RequestMapping(value = "wx/cashCal")
public class WeixinCashCalController {

    @Autowired
    private IBuffetOrderService buffetOrderService;

    @Autowired
    private IPrizeService prizeService;

    @Autowired
    private IBuffetOrderDetailService buffetOrderDetailService;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private IMemberChargeService memberChargeService;

    @GetMapping(value = "index")
    public String index(Model model, String day, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        Account a = accountService.findByOpenid(openid);

        if(a!=null && (AccountTools.ADMIN.equals(a.getType()) || AccountTools.PARTNER.equals(a.getType()))) {
            if(day==null) {
                day = NormalTools.curDate("yyyy-MM-dd"); //默认为当天
            } else {
                day = day.replace("eq-", "");
            }

            MyTimeDto mtd = new MyTimeDto(day);

            queryCount(day, model);
            queryTotalMoney(mtd, model);
            model.addAttribute("day", day);

            Float marketMoneyAM = buffetOrderService.queryTotalMoneyByPayType(mtd.getStartTimeAM(), mtd.getEndTimeAM(), "5");
            model.addAttribute("marketMoneyAM", marketMoneyAM==null?0:marketMoneyAM); //商场签单
            Float marketMoneyPM = buffetOrderService.queryTotalMoneyByPayType(mtd.getStartTimePM(), mtd.getEndTimePM(), "5");
            model.addAttribute("marketMoneyPM", marketMoneyPM==null?0:marketMoneyPM); //商场签单

            Float meituanMoneyAM = buffetOrderService.queryMoneyByMeiTuan(mtd.getStartTimeAM(), mtd.getEndTimeAM());
            model.addAttribute("meituanMoneyAM", meituanMoneyAM==null?0:meituanMoneyAM); //美团
            Float meituanMoneyPM = buffetOrderService.queryMoneyByMeiTuan(mtd.getStartTimePM(), mtd.getEndTimePM());
            model.addAttribute("meituanMoneyPM", meituanMoneyPM==null?0:meituanMoneyPM); //美团

            Float ticketMoneyAM = buffetOrderService.queryMoneyByTicket(mtd.getStartTimeAM(), mtd.getEndTimeAM());
            model.addAttribute("ticketMoneyAM", ticketMoneyAM==null?0:ticketMoneyAM); //卡券
            Float ticketMoneyPM = buffetOrderService.queryMoneyByTicket(mtd.getStartTimePM(), mtd.getEndTimePM());
            model.addAttribute("ticketMoneyPM", ticketMoneyPM==null?0:ticketMoneyPM); //卡券

            Float weixinMoneyAM = buffetOrderService.queryTotalMoneyByPayType(mtd.getStartTimeAM(), mtd.getEndTimeAM(), "3");
            model.addAttribute("weixinMoneyAM", weixinMoneyAM==null?0:weixinMoneyAM); //微信支付
            Float weixinMoneyPM = buffetOrderService.queryTotalMoneyByPayType(mtd.getStartTimePM(), mtd.getEndTimePM(), "3");
            model.addAttribute("weixinMoneyPM", weixinMoneyPM==null?0:weixinMoneyPM); //微信支付

            Float alipayMoneyAM = buffetOrderService.queryTotalMoneyByPayType(mtd.getStartTimeAM(), mtd.getEndTimeAM(), "4");
            model.addAttribute("alipayMoneyAM", alipayMoneyAM==null?0:alipayMoneyAM); //支付宝
            Float alipayMoneyPM = buffetOrderService.queryTotalMoneyByPayType(mtd.getStartTimePM(), mtd.getEndTimePM(), "4");
            model.addAttribute("alipayMoneyPM", alipayMoneyPM==null?0:alipayMoneyPM); //支付宝

            Float cashMoneyAM = buffetOrderService.queryTotalMoneyByPayType(mtd.getStartTimeAM(), mtd.getEndTimeAM(), "1");
            model.addAttribute("cashMoneyAM", cashMoneyAM==null?0:cashMoneyAM); //现金
            Float cashMoneyPM = buffetOrderService.queryTotalMoneyByPayType(mtd.getStartTimePM(), mtd.getEndTimePM(), "1");
            model.addAttribute("cashMoneyPM", cashMoneyPM==null?0:cashMoneyPM); //现金

            Float cardMoneyAM = buffetOrderService.queryTotalMoneyByPayType(mtd.getStartTimeAM(), mtd.getEndTimeAM(), "2");
            model.addAttribute("cardMoneyAM", cardMoneyAM==null?0:cardMoneyAM); //刷卡
            Float cardMoneyPM = buffetOrderService.queryTotalMoneyByPayType(mtd.getStartTimePM(), mtd.getEndTimePM(), "2");
            model.addAttribute("cardMoneyPM", cardMoneyPM==null?0:cardMoneyPM); //刷卡

            calTicket(mtd, model);
            calMeituan(mtd, model);
            buildMemberMoney(mtd, model);
            buildBond(mtd, model);
            buildBondMoney(mtd, model);
            calMemberCharge(model, day);
            return "weixin/cashCal/index";
        } else {
            return "redirect:/weixin/index";
        }
    }

    //会员充值统计
    private void calMemberCharge(Model model, String day) {
        Float mCash = memberChargeService.queryMoneyByPayType(day, "1"); //会员现金
        Float mWeixin = memberChargeService.queryMoneyByPayType(day, "3"); //会员微信
        Float mAlipay = memberChargeService.queryMoneyByPayType(day, "2"); //会员支付宝
        model.addAttribute("mCash", mCash==null?0:mCash);
        model.addAttribute("mWeixin", mWeixin==null?0:mWeixin);
        model.addAttribute("mAlipay", mAlipay==null?0:mAlipay);
    }

    private void buildBondMoney(MyTimeDto mtd, Model model) {
        Float bondMoneyAM = buffetOrderService.queryBondMoney(mtd.getStartTimeAM(), mtd.getEndTimeAM());
        model.addAttribute("bondMoneyAM", bondMoneyAM==null?0:bondMoneyAM); //押金
        Float bondMoneyPM = buffetOrderService.queryBondMoney(mtd.getStartTimePM(), mtd.getEndTimePM());
        model.addAttribute("bondMoneyPM", bondMoneyPM==null?0:bondMoneyPM); //押金

        Float unBackBondAM = buffetOrderService.queryBondByStatus(mtd.getStartTimeAM(), mtd.getEndTimeAM(), "2"); //未退押金
        Float surplusBondAM = buffetOrderService.queryBondByStatus(mtd.getStartTimeAM(), mtd.getEndTimeAM(), "5"); //已扣押金
        model.addAttribute("unBackBondAM", unBackBondAM==null?0:unBackBondAM);
        model.addAttribute("surplusBondAM", surplusBondAM==null?0:surplusBondAM);

        Float unBackBondPM = buffetOrderService.queryBondByStatus(mtd.getStartTimePM(), mtd.getEndTimePM(), "2"); //未退押金
        Float surplusBondPM = buffetOrderService.queryBondByStatus(mtd.getStartTimePM(), mtd.getEndTimePM(), "5"); //已扣押金
        model.addAttribute("unBackBondPM", unBackBondPM==null?0:unBackBondPM);
        model.addAttribute("surplusBondPM", surplusBondPM==null?0:surplusBondPM);
    }

    private void calMeituan(MyTimeDto mtd, Model model) {
        List<BuffetOrder> list = buffetOrderService.findByMeiTuan(mtd.getStartTimeAM(), mtd.getEndTimeAM()); //上午
        model.addAttribute("meituanAmountAM", buildMeituanAmount(list));

        List<BuffetOrder> listPM = buffetOrderService.findByMeiTuan(mtd.getStartTimePM(), mtd.getEndTimePM()); //下午
        model.addAttribute("meituanAmountPM", buildMeituanAmount(listPM));
    }

    private Integer buildMeituanAmount(List<BuffetOrder> list) {
        Integer res = 0;
        for(BuffetOrder order : list) {
            String datas = order.getDiscountReason();
            String [] array = datas.split(",");
            for(String d : array) {
                if(d!=null && d.length()==12) {
                    res ++;
                }
            }
        }
        return res;
    }

    private void calTicket(MyTimeDto mtd, Model model) {
        List<BuffetOrder> list = buffetOrderService.findByTicket(mtd.getStartTimeAM(), mtd.getEndTimeAM()); //上午
        model.addAttribute("ticketAmountAM", buildTicketAmount(list));

        List<BuffetOrder> listPM = buffetOrderService.findByTicket(mtd.getStartTimePM(), mtd.getEndTimePM()); //下午
        model.addAttribute("ticketAmountPM", buildTicketAmount(listPM));
    }

    private Map<MyTicketDto, Integer> buildTicketAmount(List<BuffetOrder> list) {
        MyTicketTools mtt = new MyTicketTools();
        for(BuffetOrder order : list) {
            String [] array = order.getDiscountReason().split("_");
            for(String single : array) {
                if(single!=null && single.indexOf(":")>0) {
                    String [] s_a = single.split(":");
                    Integer dataId = Integer.parseInt(s_a[0]); //Prize的id
                    Integer amount = Integer.parseInt(s_a[1]); //对应数量
                    Prize prize = prizeService.findOne(dataId);
                    mtt.add(prize.getId(), prize.getName(), amount);
                }
            }
        }
        return mtt.getDatas();
    }

    private void buildMemberMoney(MyTimeDto mtd, Model model) {
        Float memberMoneyAM = buffetOrderService.queryMemberDiscount(mtd.getStartTimeAM(), mtd.getEndTimeAM()); //会员抵价金额
        Float memberMoneyPM = buffetOrderService.queryMemberDiscount(mtd.getStartTimePM(), mtd.getEndTimePM()); //会员抵价金额

        model.addAttribute("memberMoneyAM", memberMoneyAM==null?0:memberMoneyAM);
        model.addAttribute("memberMoneyPM", memberMoneyPM==null?0:memberMoneyPM);
    }

    private void buildBond(MyTimeDto mtd, Model model) {
        Float weixinBondAM = buffetOrderService.queryBondByType(mtd.getStartTimeAM(), mtd.getEndTimeAM(), "3"); //微信押金
        Float weixinBondPM = buffetOrderService.queryBondByType(mtd.getStartTimePM(), mtd.getEndTimePM(), "3"); //微信押金

        Float alipayBondAM = buffetOrderService.queryBondByType(mtd.getStartTimeAM(), mtd.getEndTimeAM(), "4"); //支付宝押金
        Float alipayBondPM = buffetOrderService.queryBondByType(mtd.getStartTimePM(), mtd.getEndTimePM(), "4"); //支付宝押金

        Float cashBondAM = buffetOrderService.queryBondByType(mtd.getStartTimeAM(), mtd.getEndTimeAM(), "1"); //现金押金
        Float cashBondPM = buffetOrderService.queryBondByType(mtd.getStartTimePM(), mtd.getEndTimePM(), "1"); //现金押金

        Float returnedBondAM = buffetOrderService.queryReturnedBond(mtd.getStartTimeAM(), mtd.getEndTimeAM()); //已退押金
        Float returnedBondPM = buffetOrderService.queryReturnedBond(mtd.getStartTimePM(), mtd.getEndTimePM()); //已退押金

        model.addAttribute("weixinBondAM", weixinBondAM==null?0:weixinBondAM);
        model.addAttribute("weixinBondPM", weixinBondPM==null?0:weixinBondPM);
        model.addAttribute("alipayBondAM", alipayBondAM==null?0:alipayBondAM);
        model.addAttribute("alipayBondPM", alipayBondPM==null?0:alipayBondPM);
        model.addAttribute("cashBondAM", cashBondAM==null?0:cashBondAM);
        model.addAttribute("cashBondPM", cashBondPM==null?0:cashBondPM);
        model.addAttribute("returnedBondAM", returnedBondAM==null?0:returnedBondAM);
        model.addAttribute("returnedBondPM", returnedBondPM==null?0:returnedBondPM);
    }

    private void queryTotalMoney(MyTimeDto mtd, Model model) {
        Float totalAM = buffetOrderService.queryTotalMoneyByDay(mtd.getStartTimeAM(), mtd.getEndTimeAM());
        Float totalPM = buffetOrderService.queryTotalMoneyByDay(mtd.getStartTimePM(), mtd.getEndTimePM());
        model.addAttribute("totalAM", totalAM==null?0:totalAM);
        model.addAttribute("totalPM", totalPM==null?0:totalPM);
    }

    private void queryCount(String day, Model model) {
        Integer halfAm = buffetOrderDetailService.queryCount(day, "66666"); //午餐半票人数
        Integer fullAm = buffetOrderDetailService.queryCount(day, "88888"); //午餐全票人数
        Integer halfPm = buffetOrderDetailService.queryCount(day, "77777"); //晚餐半票人数
        Integer fullPm = buffetOrderDetailService.queryCount(day, "99999"); //晚餐全票人数

        model.addAttribute("halfAm", halfAm);
        model.addAttribute("fullAm", fullAm);
        model.addAttribute("halfPm", halfPm);
        model.addAttribute("fullPm", fullPm);
    }
}
