package com.zslin.weixin.tools;

import com.zslin.basic.tools.NormalTools;
import com.zslin.basic.tools.PinyinToolkit;
import com.zslin.weixin.dao.ITemplateMessageRelationDao;
import com.zslin.weixin.dto.SendMessageDto;
import com.zslin.weixin.dto.TempParamDto;
import com.zslin.weixin.model.TemplateMessageRelation;
import com.zslin.wx.tools.AccountTools;
import com.zslin.wx.tools.EventTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zsl on 2018/8/28.
 * 发送模板消息工具类
 */
@Component
public class TemplateMessageTools {

    @Autowired
    private ITemplateMessageRelationDao templateMessageRelationDao;

    @Autowired
    private AccountTools accountTools;

    @Autowired
    private EventTools eventTools;

    public void sendMessageByDto(SendMessageDto dto) {
        List<String> keys = dto.getKeyValues();
        String [] key_array = new String[keys.size()];
        for(int i=0;i<keys.size();i++) {
            key_array[i] = keys.get(i);
        }
        if(dto.getToUser()!=null && !"".equals(dto.getToUser())) { //发给单个的
            sendMessage(dto.getTemplateName(), dto.getToUser(), dto.getUrl(), dto.getFirst(), key_array);
        } else if(dto.getUsers()!=null && dto.getUsers().size()>0) { //发给多个的
            sendMessage(dto.getTemplateName(), dto.getUsers(), dto.getUrl(), dto.getFirst(), key_array);
        }
    }

    public void sendMessage(String templateName, List<String> toUsers, String url, String first, String... keyValues) {
        for(String toUser : toUsers) {
            sendMessage(templateName, toUser, url, first, keyValues);
        }
    }

    public void sendMessage(String templateName, String toUser, String url, String first, String...keyValues) {
        try {
            String pinyin = getPinyin(templateName);
//        String templateId = templateMessageRelationDao.findTemplateIdByTemplatePinyin(pinyin);
            TemplateMessageRelation tmr = templateMessageRelationDao.findByTemplatePinyin(pinyin);
            if(tmr==null || tmr.getTemplateId()==null || "".equals(tmr.getTemplateId())) {
                sendErrorMessage(templateName);
            } else {
                eventTools.sendMsg(toUser, tmr.getTemplateId(), url, "#000000", buildParams(first, tmr.getKeyValues(), keyValues));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendErrorMessage(String templateName) {
        List<String> admins = accountTools.getOpenid(AccountTools.ADMIN);
        eventTools.eventRemind(admins, "模板消息异常", "模板消息异常", NormalTools.curDate(), "模板名称:"+templateName, "#");
    }

    private List<TempParamDto> buildParams(String first, String keyMaps, String... keyValues) {
        List<TempParamDto> result = new ArrayList<>();
        result.add(new TempParamDto("first", first, "#666666"));
        Map<String, String> keys = buildKeyMaps(keyMaps);
        StringBuffer remark = new StringBuffer();
        for(String kvs : keyValues) {
            if(kvs==null || "".equals(kvs.trim())) {continue;}
            TempParamDto dto = buildSingleDto(kvs);
            if(dto.getParamName().equals("remark")) {
                remark.append(dto.getValue());
            } else {
                String paramName = keys.get(dto.getParamName());
                if(paramName==null || "".equals(paramName)) {continue;} //如果未设置，则忽略
                if(paramName.equals("remark")) {
                    remark.append(dto.getParamName()).append(":").append(dto.getValue()).append("\\n");
                } else {
                    dto.setParamName(paramName);
                    result.add(dto);
                }
            }
        }
        result.add(new TempParamDto("remark", remark.toString(), "#888888"));
        return result;
    }

    private TempParamDto buildSingleDto(String keyValue) {
        TempParamDto dto ;
        if(keyValue.indexOf("=")>0) {
            String [] temp = keyValue.split("=");
            dto = new TempParamDto(temp[0], temp[1], "#666666");
        } else {
            dto = new TempParamDto("remark", keyValue, "#666666");
        }
        return dto;
    }


    private Map<String, String> buildKeyMaps(String keyMaps) {
        //keyMaps:: 反馈日期=keyword1-回复内容=keyword2-备注=remark
        Map<String, String> result = new HashMap<>();
        String [] array = keyMaps.split("-");
        for(String keys : array) {
            String [] temp = keys.split("=");
            result.put(temp[0], temp[1]);
        }
        return result;
    }

    /** 获得拼音 */
    private String getPinyin(String templateName) {
        return PinyinToolkit.cn2Spell(templateName, "");
    }

    public static String field(String name, String value) {
        return name + "=" + value;
    }

    public static String field(String value) {return value;}
}
