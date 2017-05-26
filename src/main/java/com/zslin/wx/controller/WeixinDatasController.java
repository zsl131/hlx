package com.zslin.wx.controller;

import com.zslin.web.service.IBuffetOrderService;
import com.zslin.wx.datadto.BarDto;
import com.zslin.wx.datadto.DateDto;
import com.zslin.wx.datadto.PieDto;
import com.zslin.wx.datadto.SingleData;
import com.zslin.wx.datatools.DateHandlerTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 钟述林 393156105@qq.com on 2017/4/21 15:12.
 */
@RestController
@RequestMapping(value = "wx/datas")
public class WeixinDatasController {

    @Autowired
    private IBuffetOrderService buffetOrderService;

    /**
     * 获取自助餐和外卖的订单数量
     * @param amount 1-今天，2-昨天，3-7天内，4-本月，5-上个月
     * @return
     */
    @GetMapping(value = "buildCountData")
    public PieDto buildCountData(String amount) {
        DateDto dto = DateHandlerTools.buildDate(amount);
        String [] payType = new String[]{"1", "2", "3", "4"};
        Integer selfCount = buffetOrderService.findFinishSelfCount(dto.getStartDay(), dto.getEndDay(), payType);
        Integer outCount = buffetOrderService.findFinishOutCount(dto.getStartDay(), dto.getEndDay(), payType);
        PieDto res = new PieDto("外卖与自助订单比拼图", dto.toString(), "{b} : {c} ({d}%)",
                new SingleData("自助餐", selfCount*1d),
                new SingleData("外卖", outCount*1d));
        return res;
    }

    @GetMapping(value = "buildMoneyData")
    public BarDto buildMoneyData(String amount) {
        DateDto dto = DateHandlerTools.buildDate(amount);

        ///** 付款方式，只有到店下单才会有此值；1-现金；2-刷卡；3-微信支付；4-支付宝支付 */
//        private String payType;
        Double m1 = buffetOrderService.sumMoney(dto.getStartDay(), dto.getEndDay(), "1", "1", new String[]{"4", "5"});
        Double m2 = buffetOrderService.sumMoney(dto.getStartDay(), dto.getEndDay(), "1", "2", new String[]{"4", "5"});
        Double m3 = buffetOrderService.sumMoney(dto.getStartDay(), dto.getEndDay(), "1", "3", new String[]{"4", "5"});
        Double m4 = buffetOrderService.sumMoney(dto.getStartDay(), dto.getEndDay(), "1", "4", new String[]{"4", "5"});

        Double m5 = buffetOrderService.sumMoney(dto.getStartDay(), dto.getEndDay(), "0", "1", "2");
        Double m6 = buffetOrderService.sumMoney(dto.getStartDay(), dto.getEndDay(), "0", "2", "2");
        Double m7 = buffetOrderService.sumMoney(dto.getStartDay(), dto.getEndDay(), "0", "3", "2");
        Double m8 = buffetOrderService.sumMoney(dto.getStartDay(), dto.getEndDay(), "0", "4", "2");

        m1=m1==null?0:m1;
        m2=m2==null?0:m2;
        m3=m3==null?0:m3;
        m4=m4==null?0:m4;
        m5=m5==null?0:m5;
        m6=m6==null?0:m6;
        m7=m7==null?0:m7;
        m8=m8==null?0:m8;

//        System.out.println("="+m1+","+m2+","+m3+","+m4+","+m5+","+m6+","+m7+","+m8);

        BarDto res = new BarDto("收费方式对比图", dto.toString());
        res.addLegend("自助餐", "外卖").
                addCate("现金", "刷卡", "微信", "支付宝").
                addValues("自助餐", m1+"", m2+"", m3+"", m4+"").
                addValues("外卖", m5+"", m6+"", m7+"", m8+"");
        return res;
    }
}
