package com.zslin.wx.tools;

import com.zslin.basic.tools.ConfigTools;
import com.zslin.basic.tools.DateTools;
import com.zslin.basic.tools.NormalTools;
import com.zslin.web.model.*;
import com.zslin.web.service.*;
import com.zslin.wx.dbtools.ScoreTools;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.UUID;

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

    @Autowired
    private IWalletService walletService;

    @Autowired
    private ICommentService commentService;

    @Autowired
    private IActivityRecordService activityRecordService;

    @Autowired
    private IWalletDetailService walletDetailService;

    @Autowired
    private IArticleService articleService;

    @Autowired
    private IQrcodeService qrcodeService;

    @Autowired
    private ScoreTools scoreTools;

    @Autowired
    private WxConfig wxConfig;

    @Autowired
    private ConfigTools configTools;

    @Autowired
    private AccountTools accountTools;

    @Autowired
    private EventTools eventTools;

    /** 当用户取消关注时 */
    public void onUnsubscribe(String openid) {
        accountService.updateStatus(openid, "0");
    }

    /**
     * 添加文本内容
     * @param openid 用户Openid
     * @param builderName 开发者微信号
     * @param content 具体内容
     * @return
     */
    public String onEventText(String openid, String builderName, String content) {
        if(content==null || "".equals(content.trim()) || "?".equals(content.trim())
                || "？".equals(content.trim()) || "1".equals(content.trim())
                || "help".equals(content.toLowerCase().trim())) { //帮助
            return WeixinXmlTools.createTextXml(openid, builderName, buildHelpStr());
        } else if("1".equals(content.trim()) || "gy".equals(content.toLowerCase().trim()) ||
                "guanyu".equals(content.toLowerCase().trim()) || "about".equals(content.toLowerCase().trim())||
                "关于".equals(content.trim())) { //关于
            Article article = articleService.findOne(1);
            return WeixinXmlTools.buildArticleStr(openid, builderName, article, wxConfig.getUrl());
        } else if("2".equals(content.trim()) || "jf".equals(content.toLowerCase().trim()) ||
                "jifen".equals(content.toLowerCase().trim()) || "积分".equals(content.trim())) { //ID为2的文章
            Article article = articleService.findOne(2);
            return WeixinXmlTools.buildArticleStr(openid, builderName, article, wxConfig.getUrl());
        } else if("hlx".equals(content.toLowerCase())) { //关注情况
            eventTools.eventRemind(openid, "查询提醒", "关注情况如下", DateTools.date2Str(new Date(), "yyyy-MM-dd"), accountTools.buildAccountStr(), "");
            return "";
        } else {
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
            if (a != null) {
                f.setAccountId(a.getId());
                f.setNickname(a.getNickname());
                f.setHeadimgurl(a.getHeadimgurl());
            }
            feedbackService.save(f);

            scoreTools.processScore(openid, ScoreRule.SEND_MESSAGE); //关注时送积分
            return "";
        }
    }

    private String buildHelpStr() {
        StringBuffer sb = new StringBuffer();
        sb.append("help    ").append("：").append("查看帮助").append("\n")
          .append("gy      ").append("：").append("查看昭通汉丽轩").append("\n")
          .append("jf      ").append("：").append("查询积分说明").append("\n")
          .append("hlx     ").append("：").append("查询关注情况");
        return sb.toString();
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
        f.setFilePath(exchangeTools.saveMedia(mediaId, configTools.getUploadPath("feedback/")+ UUID.randomUUID().toString()).replace(configTools.getUploadPath(), "\\"));
        Account a = accountService.findByOpenid(openid);
        if(a!=null) {
            f.setAccountId(a.getId());
            f.setNickname(a.getNickname());
            f.setHeadimgurl(a.getHeadimgurl());
        }
        feedbackService.save(f);

        scoreTools.processScore(openid, ScoreRule.SEND_MESSAGE); //关注时送积分
    }

    /** 当用户关注时 */
    public void onSubscribe(String openid) {
        Integer id = (Integer) accountService.queryByHql("SELECT a.id FROM Account a WHERE a.openid=?", openid);
        Account a ;
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
        a.setFollowDate(new Date());
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

        addWallet(a); //添加钱包

        scoreTools.processScore(openid, ScoreRule.INIT); //关注时送积分
    }

    private void addWallet(Account account) {
        Wallet w = walletService.findByOpenid(account.getOpenid());
        if(w==null) {
            w = new Wallet();
            w.setOpenid(account.getOpenid());
            w.setAccountId(account.getId());
            w.setAccountName(account.getNickname());
            w.setMoney(0);
            w.setScore(0);
            w.setCreateDate(new Date());
            w.setCreateLong(System.currentTimeMillis());
            w.setCreateDay(NormalTools.curDate("yyyy-MM-dd"));
            w.setCreateTime(NormalTools.curDate("yyyy-MM-dd HH:mm:ss"));
            walletService.save(w);
        }
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

        updateRelation(a); //更新所有关联数据
    }

    /** 构建关注时的数据 */
    public String buildSubscribeStr(String toUser, String fromUser) {
        List<Article> articleList = articleService.findFirst();
        return WeixinXmlTools.buildSubscribeStr(toUser, fromUser, articleList, wxConfig.getUrl());
    }

    private void updateRelation(Account a) {
        commentService.update(a.getNickname(), a.getHeadimgurl(), a.getOpenid());
        feedbackService.update(a.getNickname(), a.getHeadimgurl(), a.getOpenid());
        activityRecordService.update(a.getNickname(), a.getHeadimgurl(), a.getOpenid());
        walletDetailService.update(a.getNickname(), a.getOpenid());
        qrcodeService.update(a.getNickname(), a.getHeadimgurl(), a.getOpenid());
    }
}
