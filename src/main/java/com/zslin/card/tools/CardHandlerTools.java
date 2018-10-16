package com.zslin.card.tools;

import com.alibaba.fastjson.JSON;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.card.model.Card;
import com.zslin.card.model.CardApply;
import com.zslin.card.model.CardCheck;
import com.zslin.card.model.GrantCard;
import com.zslin.card.service.ICardApplyService;
import com.zslin.card.service.ICardCheckService;
import com.zslin.card.service.ICardService;
import com.zslin.card.service.IGrantCardService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by zsl on 2018/10/15.
 */
@Component
public class CardHandlerTools {

    @Autowired
    private ICardService cardService;

    @Autowired
    private IGrantCardService grantCardService;

    @Autowired
    private ICardApplyService cardApplyService;

    @Autowired
    private ICardCheckService cardCheckService;

    public void handleGrantCard(JSONObject jsonObj) {
        GrantCard gc = JSON.toJavaObject(JSON.parseObject(jsonObj.toString()), GrantCard.class);
        Integer cardNo = gc.getCardNo();
        GrantCard card = grantCardService.findByCardNo(cardNo);
        if(card!=null) {
            MyBeanUtils.copyProperties(gc, card);
            grantCardService.save(card);
        } else {grantCardService.save(gc);}
    }

    public void handlCard(JSONObject jsonObj) {
        Card c = JSON.toJavaObject(JSON.parseObject(jsonObj.toString()), Card.class);
        Integer no = c.getNo();
        Card card = cardService.findByNo(no);
        if(card==null) {cardService.save(c);} else {
            MyBeanUtils.copyProperties(c, card);
            cardService.save(card);
        }
    }

    public void handCardApply(JSONObject jsonObj) {
        CardApply ca = JSON.toJavaObject(JSON.parseObject(jsonObj.toString()), CardApply.class);
        Integer cardNo = ca.getCardNo();
        CardApply apply = cardApplyService.findByCardNo(cardNo);
        if(apply==null) {cardApplyService.save(ca);} else {
            MyBeanUtils.copyProperties(ca, apply);
            cardApplyService.save(apply);
        }
    }

    public void handCardCheck(JSONObject jsonObj) {
        CardCheck cc = JSON.toJavaObject(JSON.parseObject(jsonObj.toString()), CardCheck.class);
        Integer cardNo = cc.getCardNo();
        CardCheck check = cardCheckService.findByCardNo(cardNo);
        if(check==null) {cardCheckService.save(cc);}
    }
}
