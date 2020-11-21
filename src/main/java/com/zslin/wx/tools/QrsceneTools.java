package com.zslin.wx.tools;

import com.zslin.basic.tools.DateTools;
import com.zslin.web.model.Account;
import com.zslin.web.model.ScoreRule;
import com.zslin.web.service.IAccountService;
import com.zslin.weixin.tools.HlxTicketTools;
import com.zslin.wx.dbtools.ScoreAdditionalDto;
import com.zslin.wx.dbtools.ScoreTools;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/22 22:49.
 * 微信二维码处理
 */
@Component
public class QrsceneTools {

    //初次扫描二维码关注时会自动加此前缀，所以需要过虑
    private String prefix = "qrscene_";

    @Autowired
    private ExchangeTools exchangeTools;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private EventTools eventTools;

    @Autowired
    private ScoreTools scoreTools;

    @Autowired
    private HlxTicketTools hlxTicketTools;

    /**
     * 处理二维码
     * @param openid 操作的用户openid
     * @param sceneStr 二维码值
     */
    public void processQrscene(String openid, String sceneStr) {
        //System.out.println("------>QrSceneTools--->"+openid+"---->"+sceneStr);
        sceneStr = sceneStr.replace(prefix, "");
        if(sceneStr.startsWith(QrTools.USER_TYPE)) { //用户个人二维码
            userFollow(openid, sceneStr);
        } else if(sceneStr.startsWith(QrTools.TICKET_TYPE)) { //扫二维码领券
            sceneStr = sceneStr.replace(QrTools.TICKET_TYPE, ""); //只获取值
            hlxTicketTools.receiveTicket(openid, sceneStr);
        }
    }

    /**
     * 拉用户关注
     * @param openid 操作的用户id
     * @param sceneStr 包含用户Id的字符串
     */
    private void userFollow(String openid, String sceneStr) {
        sceneStr = sceneStr.replace(QrTools.USER_TYPE, "");
        Integer userId = Integer.parseInt(sceneStr); //用户Id
        JSONObject jsonObj = exchangeTools.getUserInfo(openid);
        System.out.println(jsonObj.toString());
        Account account = accountService.findByOpenid(openid);
        Account oldAcc = accountService.findOne(userId);
        String nickname = oldAcc==null?"管理员":oldAcc.getNickname();
        if(account!=null && account.getFollowUserId()<=0) { //只有FollowUserId小于等于0时，才表示该用户未被其他人拉过
            accountService.updateFollow(account.getId(), oldAcc==null?0:oldAcc.getId(), nickname);

            eventTools.eventRemind(openid, "关注提醒", "用户关注提醒",
                    DateTools.date2Str(new Date(), "yyyy-MM-dd"), "您已成功被["+nickname+"]邀请关注，欢迎使用！", "");

            if(oldAcc!=null) {
                eventTools.eventRemind(oldAcc.getOpenid(), "关注提醒", "邀请用户关注提醒",
                        DateTools.date2Str(new Date(), "yyyy-MM-dd"), "您已成功邀请["+account.getNickname()+"]关注，感谢您的参与！", "");
                scoreTools.processScore(oldAcc.getOpenid(), ScoreRule.PULL_USER, new ScoreAdditionalDto("好友昵称", account.getNickname()));
            }
        } else {
            //TODO
            eventTools.eventRemind(openid, "关注提醒", "用户关注提醒",
                    DateTools.date2Str(new Date(), "yyyy-MM-dd"), "您是["+nickname+"]邀请关注的，不可再次被其他人邀请！ ", "");
        }
    }
}
