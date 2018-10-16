package com.zslin.card.tools;

import com.zslin.card.service.ICardService;
import com.zslin.card.service.IGrantCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 卡号处理工具类
 * Created by zsl on 2018/10/13.
 */
@Component
public class CardNoTools {

    @Autowired
    private ICardService cardService;

    @Autowired
    private IGrantCardService grantCardService;

    public List<Integer> buildAllotNos(String type, Integer count) {
        List<Integer> result = new ArrayList<>();
        Integer firstNo = grantCardService.findMinNo(type);
        Integer maxNo = queryGrantMaxNo(type);
        System.out.println("========firstNo:"+firstNo+"---------maxNo:"+maxNo);
        if(firstNo!=null) {
            result.add(firstNo);
            for (int i = 0; i < count-1; i++) {
                Integer no = firstNo + i + 1;
                if (no <= maxNo) {
                    result.add(no);
                }
            }
        }
        return result;
    }

    /** 从GrantCard中取最大编号 */
    public List<Integer> buildGrantCardNos(String type, Integer count) {
        Integer firstNo = queryGrantMaxNo(type); //起始编码
        List<Integer> result = new ArrayList<>();
        for(int i=0;i<count;i++) {
            result.add(firstNo+i+1);
        }
        return result;
    }

    public List<Integer> buildCardNos(String type, Integer count) {
        Integer firstNo = queryMaxNo(type); //起始编码
        List<Integer> result = new ArrayList<>();
        for(int i=0;i<count;i++) {
            result.add(firstNo+i+1);
        }
        return result;
    }

    private Integer queryMaxNo(String type) {
        Integer maxNo = cardService.findMaxNo(type);
        if(maxNo==null) {
            maxNo = buildFirstNo(type);
        }
        return maxNo;
    }

    private Integer buildFirstNo(String type) {
        StringBuffer sb = new StringBuffer(type);
        sb.append("000000");
        return Integer.parseInt(sb.toString());
    }

    private Integer queryGrantMaxNo(String type) {
        Integer maxNo = grantCardService.findMaxNo(type);
        if(maxNo==null) {
            maxNo = buildFirstNo(type);
        }
        return maxNo;
    }

    public Integer queryMaxOrderNo(String day) {
        Integer maxOrderNo = grantCardService.findMaxOrderNo(day);
        if(maxOrderNo==null) {maxOrderNo = 0;}
        return maxOrderNo + 1;
    }

    /** 生成批次编号 */
    public String buildBatchNo(String day, Integer orderNo) {
        StringBuffer sb = new StringBuffer(day);
        for(int i=0;i<3-(""+orderNo).length();i++) {sb.append("0");}
        sb.append(orderNo);
        return sb.toString();
    }
}
