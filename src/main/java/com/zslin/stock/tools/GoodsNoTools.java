package com.zslin.stock.tools;

import com.zslin.basic.tools.DateTools;
import com.zslin.stock.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by zsl on 2018/5/22.
 */
@Component
public class GoodsNoTools {

    @Autowired
    private IStockGoodsService stockGoodsService;

    @Autowired
    private IGoodsApplyService goodsApplyService;

    @Autowired
    private IOuterApplyService outerApplyService;

    @Autowired
    private IGoodsRegisterService goodsRegisterService;

    @Autowired
    private IPreenterService preenterService;

    /** 生成物品编号 */
    public Integer generateOrderNo() {
        Integer maxOrderNo = stockGoodsService.maxOrderNo();
        maxOrderNo = (maxOrderNo == null)?0:maxOrderNo;
        return maxOrderNo+1;
    }

    /** 生成物品编号 */
    public String buildNo(String locationType, Integer orderNo) {
        StringBuffer sb = new StringBuffer(locationType);
        String temp = orderNo+"";
        for(int i=0;i<7-temp.length();i++) {
            sb.append("0");
        }
        sb.append(temp);
        return sb.toString();
    }

    /** 生成申购编号 */
    public Integer generateApplyNo() {
        Integer maxNo = goodsApplyService.maxNo();
        maxNo = (maxNo == null)?0:maxNo;
        return maxNo + 1;
    }

    /** 生成库出申购编号 */
    public Integer generateOuterApplyNo() {
        Integer maxNo = outerApplyService.maxNo();
        maxNo = (maxNo == null)?0:maxNo;
        return maxNo + 1;
    }

    /** 生成入库编号 */
    public Integer generateGoodsRegisterNo() {
        Integer maxNo = goodsRegisterService.maxNo();
        maxNo = (maxNo == null)?0:maxNo;
        return maxNo + 1;
    }

    /** 生成预录入编号 */
    public Integer generatePreenterNo() {
        Integer maxNo = preenterService.maxNo();
        maxNo = (maxNo == null)?0:maxNo;
        return maxNo + 1;
    }

    /** 生成申购批次编号 */
    public String buildApplyBatchNo(Integer no) {
        StringBuffer sb = new StringBuffer(DateTools.date2Str(new Date(), "yyyyMM"));
        String temp = no+"";
        for(int i=0;i<6-temp.length();i++) {
            sb.append("0");
        }
        sb.append(temp);
        return sb.toString();
    }
}
