package com.zslin.wx.controller;

import com.zslin.web.model.Feedback;
import com.zslin.wx.tools.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/24 10:43.
 */
@Controller
@RequestMapping(value = "weixin")
public class WeixinController {

    @Autowired
    private SignTools signTools;

    @Autowired
    private EventTools eventTools;

    @Autowired
    private RepeatTools repeatTools;

    @Autowired
    private DatasTools datasTools;

    @Autowired
    private QrsceneTools qrsceneTools;

    @GetMapping(value = "root")
    public void root(String signature, String timestamp, String nonce, String echostr, HttpServletResponse response) {
        try {
            PrintWriter out = response.getWriter();
            // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
            if (signTools.checkSignature(signature, timestamp, nonce)) {
                out.print(echostr);
            }
            out.close();

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping(value = "root")
    public void root(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/xml");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = null;
        String docSend = "";

        try {
            out = response.getWriter();
			request.setCharacterEncoding("UTF-8");

            Element root = eventTools.getMessageEle(request);

            Node fromUser = root.getElementsByTagName("FromUserName").item(0);
            Node createTime = root.getElementsByTagName("CreateTime").item(0);
            Node msgType = root.getElementsByTagName("MsgType").item(0);
            Node content = root.getElementsByTagName("Content").item(0);
            Node event = root.getElementsByTagName("Event").item(0);
            Node eventKey = root.getElementsByTagName("EventKey").item(0);
            Node msgId = root.getElementsByTagName("MsgId").item(0);

            String fromOpenid = fromUser.getTextContent(); //用户的openid
            String cTime = createTime.getTextContent(); //创建时间
            String msgTypeStr = msgType.getTextContent(); //事件类型

            if(repeatTools.hasRepeat(fromOpenid, cTime)) { //如果重复
                out.print(docSend);
                out.flush();
                out.close();
                return;
            } else {
                if("text".equalsIgnoreCase(msgTypeStr)) { //文本信息
                    datasTools.onEventText(fromOpenid, content.getTextContent());

                } else if("image".equalsIgnoreCase(msgTypeStr)) { //图片信息
                    String picUrl = root.getElementsByTagName("PicUrl").item(0).getTextContent(); //图片地址
                    String mediaId = root.getElementsByTagName("MediaId").item(0).getTextContent(); //媒体ID

                    datasTools.onEventImage(fromOpenid, picUrl, mediaId); //添加图片信息
//                    System.out.println(picUrl);
//                    System.out.println(mediaId);
                } else if("voice".equalsIgnoreCase(msgTypeStr)) { //语音信息

                } else if("video".equalsIgnoreCase(msgTypeStr)) { //视频信息

                } else if("shortvideo".equalsIgnoreCase(msgTypeStr)) { //小视频信息

                } else if("location".equalsIgnoreCase(msgTypeStr)) { //地址位置信息

                } else if("link".equalsIgnoreCase(msgTypeStr)) { //链接信息

                }

                if("event".equalsIgnoreCase(msgTypeStr)) { //如果是事件
                    String eventStr = event.getTextContent(); //事件
                    if("subscribe".equalsIgnoreCase(eventStr)) { //关注
                        datasTools.onSubscribe(fromOpenid);
                    } else if("unsubscribe".equalsIgnoreCase(eventStr)) { //取消关注
                        datasTools.onUnsubscribe(fromOpenid);
                    } else if("LOCATION".equalsIgnoreCase(eventStr)) { //定位

                    } else if("VIEW".equalsIgnoreCase(eventStr)) { //点击菜单

                    }
                }

                if(eventKey!=null) { //扫描了二维码
//                    System.out.println("===eventKey:" + eventKey.getTextContent());
                    qrsceneTools.processQrscene(fromOpenid, eventKey.getTextContent());
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        out.print(docSend);
        out.flush();
        out.close();
    }
}
