package com.zslin.weixin.tools;

import com.zslin.rabbit.RabbitMQConfig;
import com.zslin.weixin.dto.SendMessageDto;
import com.zslin.wx.tools.AccountTools;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 发送模板消息工具类
 */
@Component
public class SendTemplateMessageTools {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private AccountTools accountTools;

    /**
     * 发送模板消息
     * @param miniOpenid 小程序Openid
     * @param tempName 模板名称
     * @param url 请求地址
     * @param title 模板消息主题
     * @param fields 消息内容
     */
    /*public void send(String miniOpenid, String tempName, String url, String title, String ...fields) {
        String wxOpenid = queryWxOpenid(miniOpenid);
        if(wxOpenid!=null && !"".equals(wxOpenid)) {
            send2Wx(wxOpenid, tempName, url, title, fields);
        }
    }*/

    /**
     * 发送模板消息
     * @param wxOpenid 微信Openid
     * @param tempName 模板名称
     * @param url 请求地址
     * @param title 模板消息主题
     * @param fields 消息内容
     */
    public void send2Wx(String wxOpenid, String tempName, String url, String title, String ...fields) {
        //String wxOpenid = gueryWxOpenid(miniOpenid);
        //System.out.println("-----------------"+wxOpenid);
        if(wxOpenid!=null && !"".equals(wxOpenid)) {
            SendMessageDto smd = new SendMessageDto(tempName, wxOpenid, url, title, fields);
            rabbitTemplate.convertAndSend(RabbitMQConfig.DIRECT_EXCHANGE, RabbitMQConfig.DIRECT_ROUTING, smd);
        }
    }

    public void send2Wx(List<String> wxOpenids, String tempName, String url, String title, String ...fields) {
        //String wxOpenid = gueryWxOpenid(miniOpenid);
        //System.out.println("-----------------"+wxOpenid);
        if(wxOpenids!=null && wxOpenids.size()>0) {
            SendMessageDto smd = new SendMessageDto(tempName, wxOpenids, url, title, fields);
            rabbitTemplate.convertAndSend(RabbitMQConfig.DIRECT_EXCHANGE, RabbitMQConfig.DIRECT_ROUTING, smd);
        }
    }

    /**
     * 向管理员发送模板消息
     * @param type 管理员类型
     * @param tempName 模板名称
     * @param url 请求地址
     * @param title 模板消息主题
     * @param fields 消息内容
     */
    public void send2Manager(String type, String tempName, String url, String title, String ...fields) {
        List<String> openids = accountTools.getOpenid(type);
        if(openids!=null && openids.size()>0) {
            SendMessageDto smd = new SendMessageDto(tempName, openids, url, title, fields);
            rabbitTemplate.convertAndSend(RabbitMQConfig.DIRECT_EXCHANGE, RabbitMQConfig.DIRECT_ROUTING, smd);
        }
    }
}
