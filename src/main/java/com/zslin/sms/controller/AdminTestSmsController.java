package com.zslin.sms.controller;

import com.zslin.basic.exception.SystemException;
import com.zslin.multi.dao.IMoneybagDao;
import com.zslin.multi.model.Moneybag;
import com.zslin.sms.model.Module;
import com.zslin.sms.service.IModuleService;
import com.zslin.sms.tools.SmsConfig;
import com.zslin.wx.tools.InternetTools;
import com.zslin.wx.tools.JsonTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "admin/test/sms")
public class AdminTestSmsController {

    @Autowired
    private IModuleService moduleService;

    @Autowired
    private SmsConfig smsConfig;

    @Autowired
    private IMoneybagDao moneybagDao;

    /** 批量发送 */
    @GetMapping(value = "sendBatch")
    public @ResponseBody String sendBatch() {
        List<Moneybag> bagList = moneybagDao.findAll();
        for(Moneybag bag : bagList) {
            sendMsg(4, bag.getPhone(), "code", "1234");
        }
        return "1";
    }

    /** 发送测试 */
    @GetMapping(value = "sendSingle")
    public @ResponseBody String sendSingle() {
        sendMsg(4, "15925061256", "code", "1234");
        return "1";
    }

    public void sendMsg(Integer iid, String phone, String... params) {
        Module module = moduleService.findByIid(iid);
        if(module==null || !"1".equals(module.getStatus())) {
            throw new SystemException("短信模板不存在或已停用");
        }
        if(params!=null && params.length%2!=0) {
            throw new SystemException("要替换的参数不匹配");
        }
        String par = buildMsgParmas(params);
        String sendStr = buildUrl(smsConfig.getSendMsgCode(), "mid", iid, "phone", phone, "con", par);
        System.out.println("---sendCode->>>"+sendStr);
        String res = InternetTools.doGet(sendStr, null);
//        String res = "{'err':'0', 'res':'1', 'msg':'测试发送，未真正发送'}";
        System.out.println("==sendCode=="+res);
        String err = JsonTools.getJsonParam(res, "err");
        if("0".equals(err)) {
            String resCode = JsonTools.getJsonParam(res, "res");
            String msg = JsonTools.getJsonParam(res, "msg");
            String resMsg = msg;
            if("0".equals(resCode)) {
                resMsg = "发送成功";
            } else if("-6".equals(resCode)) {
                resMsg = "关键字："+msg;
            }

        } else {
            throw new SystemException(JsonTools.getJsonParam(res, "res")+":"+JsonTools.getJsonParam(res, "msg"));
        }
    }

    private String buildMsgParmas(String... params) {
        StringBuffer sb = new StringBuffer();
        if(params!=null) {
            for (int i = 0; i < params.length; i += 2) {
                sb.append("#").append(params[i]).append("#=")
                        .append(params[i + 1]);
                if ((i / 2) + 1 < params.length / 2) {
                    sb.append("&");
                }
            }
        }
        return sb.toString();
    }

    private String buildUrl(String code, Object... params) {
        Map<String, Object> map = new HashMap();
        if(params!=null && params.length%2==0) {
            for(int i=0; i<params.length; i+=2) {
                map.put(params[i].toString(), params[i+1]);
            }
        }
        return buildUrl(code, map);
    }

    private String buildUrl(String code, Map<String, Object> params) {
        StringBuffer sb = new StringBuffer(smsConfig.getUrl());
        sb.append("?token=").append(smsConfig.getToken()).append("&code=")
                .append(code).append("&p=");
        if(params==null || params.size()<=0) {return sb.toString();}
        sb.append(buildParams(params));

        return sb.toString();
    }

    private String buildParams(Map<String, Object> params) {
        StringBuffer sb = new StringBuffer("{");
        int flag = 0;
        for(String key : params.keySet()) {
            flag ++;
            sb.append("'").append(key).append("':'")
                    .append(params.get(key)).append("'");
            if(flag<params.size()) {sb.append(",");}
        }
        sb.append("}");
        String res = sb.toString();
        try {
            res = URLEncoder.encode(res, "utf-8");
        } catch (UnsupportedEncodingException e) {
        }
        return res;
    }
}
