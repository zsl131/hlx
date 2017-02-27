package com.zslin.wx.tools;

import com.zslin.basic.tools.DateTools;
import com.zslin.web.model.Account;
import com.zslin.web.model.Feedback;
import com.zslin.web.service.IAccountService;
import com.zslin.web.service.IFeedbackService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/24 22:26.
 */
@Component
public class DatasTools {

    @Autowired
    private IFeedbackService feedbackService;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private ExchangeTools exchangeTools;

    /** 当用户取消关注时 */
    public void onUnsubscribe(String openid) {
        accountService.updateStatus(openid, "0");
    }

    /** 添加文本内容 */
    public void onEventText(String openid, String content) {
        Feedback f = new Feedback();
        f.setCreateDate(new Date());
        f.setCreateLong(System.currentTimeMillis());
        f.setCreateDay(DateTools.date2Str(new Date()));
        f.setCreateTime(DateTools.formatDate(new Date()));
        f.setOpenid(openid);
        f.setStatus("0");
        f.setType("text");
        f.setContent(content);
        Account a = accountService.findByOpenid(openid);
        if(a!=null) {
            f.setAccountId(a.getId());
            f.setNickname(a.getNickname());
            f.setHeadimgurl(a.getHeadimgurl());
        }
        feedbackService.save(f);
    }

    /** 添加图片内容 */
    public void onEventImage(String openid, String picPath, String mediaId) {
        Feedback f = new Feedback();
        f.setCreateDate(new Date());
        f.setCreateLong(System.currentTimeMillis());
        f.setCreateDay(DateTools.date2Str(new Date()));
        f.setCreateTime(DateTools.formatDate(new Date()));
        f.setOpenid(openid);
        f.setStatus("0");
        f.setType("image");
        f.setPicUrl(picPath);
        f.setMediaId(mediaId);
        Account a = accountService.findByOpenid(openid);
        if(a!=null) {
            f.setAccountId(a.getId());
            f.setNickname(a.getNickname());
            f.setHeadimgurl(a.getHeadimgurl());
        }
        feedbackService.save(f);
    }

    /** 当用户关注时 */
    public void onSubscribe(String openid) {
        Integer id = (Integer) accountService.queryByHql("SELECT a.id FROM Account a WHERE a.openid=?", openid);
        Account a = null;
        if(id==null || id<=0) { //说明初次关注
            a = new Account();
            a.setStatus("1");
            a.setType("0");
            a.setOpenid(openid);
            a.setCreateDate(new Date());
            a.setCreateLong(System.currentTimeMillis());
            a.setCreateDay(DateTools.date2Str(new Date()));
            a.setCreateTime(DateTools.formatDate(new Date()));
        } else {
//            accountService.updateStatus(id, "1");
            a = accountService.findOne(id);
            a.setStatus("1");
        }
        JSONObject jsonObj = exchangeTools.getUserInfo(openid);
        if(jsonObj!=null) {
            String jsonStr = jsonObj.toString();
            if(jsonStr.indexOf("errcode")<0 && jsonStr.indexOf("errmsg")<0) {
                String nickname = "";
                try {
                    nickname = jsonObj.getString("nickname");
//						nickname = nickname.replaceAll("[^\\u0000-\\uFFFF]", "");
                } catch (Exception e) {
                }
                a.setHeadimgurl(jsonObj.getString("headimgurl"));
                a.setNickname(nickname);
                a.setOpenid(openid);
                a.setSex(jsonObj.getInt("sex")+"");
            }
        }
        accountService.save(a);
    }

    /** 同步更新微信用户信息，主要是昵称、头像、性别 */
    public void updateAccount(Integer accountId) {
        Account a = accountService.findOne(accountId);
        JSONObject jsonObj = exchangeTools.getUserInfo(a.getOpenid());
        if(jsonObj!=null) {
            String jsonStr = jsonObj.toString();
            if(jsonStr.indexOf("errcode")<0 && jsonStr.indexOf("errmsg")<0) {
                String nickname = "";
                try {
                    nickname = jsonObj.getString("nickname");
//						nickname = nickname.replaceAll("[^\\u0000-\\uFFFF]", "");
                } catch (Exception e) {
                }
                a.setHeadimgurl(jsonObj.getString("headimgurl"));
                a.setNickname(nickname);
                a.setSex(jsonObj.getInt("sex")+"");
            }
        }
        accountService.save(a);
    }
}
