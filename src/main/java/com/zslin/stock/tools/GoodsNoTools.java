package com.zslin.stock.tools;

import com.zslin.stock.service.IStockGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by zsl on 2018/5/22.
 */
@Component
public class GoodsNoTools {

    @Autowired
    private IStockGoodsService stockGoodsService;

    public Integer generateOrderNo() {
        Integer maxOrderNo = stockGoodsService.maxOrderNo();
        maxOrderNo = (maxOrderNo == null)?0:maxOrderNo;
        return maxOrderNo+1;
    }

    public String buildNo(String locationType, Integer orderNo) {
        StringBuffer sb = new StringBuffer(locationType);
        String temp = orderNo+"";
        for(int i=0;i<7-temp.length();i++) {
            sb.append("0");
        }
        sb.append(temp);
        return sb.toString();
    }
}
