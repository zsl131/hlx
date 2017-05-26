package com.zslin.sms.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zslin.basic.exception.SystemException;
import com.zslin.basic.tools.NormalTools;
import com.zslin.sms.dto.ModuleDto;
import com.zslin.sms.model.Module;
import com.zslin.sms.model.SendRecord;
import com.zslin.sms.service.IModuleService;
import com.zslin.sms.service.ISendRecordService;
import com.zslin.wx.tools.InternetTools;
import com.zslin.wx.tools.JsonTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/14 10:42.
 * 短信接口工具类
 */
@Component
public class SmsTools {

    @Autowired
    private SmsConfig smsConfig;

    @Autowired
    private ISendRecordService sendRecordService;

    @Autowired
    private IModuleService moduleService;

    /**
     * 添加短信模板，返回接口方提供的
     * @param sign 签名
     * @param content 短信模板内容
     */
    public Integer addModule(String sign, String content) throws SystemException {
        try {
            String res = InternetTools.doGet(buildUrl(smsConfig.getAddModuleCode(), "sign", sign, "content", content), null);
            String resCode = JsonTools.getJsonParam(res, "res"); //获取返回代码
            String msg = JsonTools.getJsonParam(res, "msg");
            if("1".equals(resCode)) {
                Integer id = Integer.parseInt(JsonTools.getJsonParam(msg, "id"));
                return id;
            } else {
                throw new SystemException(resCode, "-4".equals(resCode)?"关键字："+msg:msg);
            }
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * 删除短信模板
     * @param iid
     */
    public void deleteModule(Integer iid) {
        String res = InternetTools.doGet(buildUrl(smsConfig.getDelModuleCode(), "id", iid), null);
        String resCode = JsonTools.getJsonParam(res, "res"); //获取返回代码
        if("1".equals(resCode)) {
            throw new SucException("1", "删除成功");
        } else {
            throw new SystemException(resCode, JsonTools.getJsonParam(res, "msg"));
        }
    }

    /**
     * 列表短信模板
     */
    public void listModules() {
        String res = InternetTools.doGet(buildUrl(smsConfig.getListModulesCode()), null);
        String resJson = JsonTools.getJsonParam(res, "res");
        JSONArray jsonArray = JSON.parseArray(resJson);
        for(int i=0; i<jsonArray.size(); i++) {
            JSONObject jsonObj = jsonArray.getJSONObject(i);
            ModuleDto dto = JSONObject.toJavaObject(jsonObj, ModuleDto.class);
//            System.out.println(dto);
            moduleService.updateStatus(dto.getId(), dto.getStatus()); //修改状态
        }
//        System.out.println(res);
    }

    /**
     * 查询短信余额
     */
    public Integer surplus() {
        String res = InternetTools.doGet(buildUrl(smsConfig.getSurplusCode()), null);
        String err = JsonTools.getJsonParam(res, "err");
        if("0".equals(err)) {
            return Integer.parseInt(JsonTools.getJsonParam(res, "res"));
        } else {
            throw new SystemException(err, JsonTools.getJsonParam(res, "msg"));
        }
    }

    /**
     * 发送短信
     * @param iid 短信模板Id
     * @param phone 接口者手机号码
     * @param params 替换值，格式如：key,value,key,value
     */
    public void sendMsg(Integer iid, String phone, String... params) {
        Module module = moduleService.findByIid(iid);
        if(module==null || !"1".equals(module.getStatus())) {
            throw new SystemException("短信模板不存在或已停用");
        }
        if(params==null || params.length%2!=0) {
            throw new SystemException("要替换的参数不匹配");
        }
        String par = buildMsgParmas(params);
        String res = InternetTools.doGet(buildUrl(smsConfig.getSendMsgCode(), "mid", iid, "phone", phone, "con", par), null);
//        String res = "{'err':'0', 'res':'1', 'msg':'测试发送，未真正发送'}";
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

            SendRecord sr = new SendRecord();
            sr.setContent("【"+module.getSign()+"】"+rebuildContent(module.getContent(), par));
            sr.setCreateDate(new Date());
            sr.setCreateDay(NormalTools.curDate("yyyy-MM-dd"));
            sr.setCreateLong(System.currentTimeMillis());
            sr.setCreateTime(NormalTools.curDate("yyyy-MM-dd HH:mm:ss"));
            sr.setModuleId(module.getId());
            sr.setPhone(phone);
            sr.setStatus(resCode);
            sr.setMsg(resMsg);
            sr.setAmount(getCount(sr.getContent())); //设置计数

            sendRecordService.save(sr);

        } else {
            throw new SystemException(JsonTools.getJsonParam(res, "res")+":"+JsonTools.getJsonParam(res, "msg"));
        }
    }

    /**
     * 获取短信条数
     * @param con 短信内容
     * @return
     */
    private Integer getCount(String con) {
        int len = con.length();
        int c = len/70;
        return c + (con.length()%70==0?0:1);
    }

    /** 生成真实的短信内容 */
    private String rebuildContent(String con, String params) {
        try {
            params = URLDecoder.decode(params, "utf-8");
        } catch (UnsupportedEncodingException e) {
        }
        String[] pArray = params.split("&");
        for(String p : pArray) {
            String [] cArray = p.split("=");
            con = con.replaceAll(cArray[0], cArray[1]);
        }
        return con;
    }

    private String buildMsgParmas(String... params) {
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<params.length; i+=2) {
            sb.append("#").append(params[i]).append("#=")
              .append(params[i+1]);
            if((i/2)+1<params.length/2) {
                sb.append("&");
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
