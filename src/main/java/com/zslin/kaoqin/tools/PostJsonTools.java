package com.zslin.kaoqin.tools;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/27 10:35.
 * 考勤机Post数据的工具类
 */
@Component
public class PostJsonTools {

    @Autowired
    private ClockinTools clockinTools;

    public String handlerPost(String json) {
        StringBuffer sb = new StringBuffer();
        sb.append("{status:1,info:\"ok\",data:[");
        JSONArray array = new JSONArray(json);
        for(int i=0; i<array.length(); i++) {
            JSONObject jsonObj = array.getJSONObject(i);
            sb.append(handlerJsonObj(jsonObj));
            if(i<array.length()-1) {sb.append(",");}
        }
        sb.append("]}");
        return sb.toString();
    }

    private Integer handlerJsonObj(JSONObject jsonObj) {
        Integer id = jsonObj.getInt("id");
        String data = jsonObj.getString("data");
        if("clockin".equalsIgnoreCase(data)) { //打卡记录
            Integer ccid = jsonObj.getInt("ccid"); //员工id
            String time = jsonObj.getString("time"); //打卡时间
            Integer verify = jsonObj.getInt("verify"); //打卡验证方式
            clockinTools.clockin(ccid, time, verify); //员工打卡
        }

        return id;
    }

    /*private Integer handlerJsonObj(JSONObject jsonObj) {
        Integer id = jsonObj.getInt("id");
        switch (id) {
            case 1: //推送员工数据 //{ id:1, data:"user",ccid:123456,name:"张三",passwd:"md5",auth:0,deptid:0,card:123456}
            case 2: //推送指纹数据 //{ id:2, data:"fingerprint",ccid:123456,fingerprint:[”base64”,”base64”]}
            case 3: //推送人脸数据 //{ id:3, data:"face",ccid:123456,face:["base64","base64","base64","base64","base64","base64","base64","base64","base64"]}
            case 4: //推送头像数据，解除绑定 //{ id:4, data:"headpic",ccid:123456,headpic:”base64”}//{ id:4, data:"unbound",validcode:”md5” }
            case 5: //推送打卡记录 //{ id:5, data:"clockin", ccid:123456,time:"2015-09-05 18:05:21",verify:0,pic:"base64"} //{ id:5, data:"clockin", ccid:123456,time:"2015-09-05 18:05:21",verify:0}
            case 6: //推送设备信息 //{ id:6, data:"info",model:”QY-168”, rom:”1.1.2”,app:"1.0.3", space:54821,memory:1000,user:300,fingerprint:150,face:200,headpic:300,clockin:2054,pic:2054}
            case 7: //推送处理结果 //{ id:7, data:"return",return:[{id:1001,result:0},{id:1002, result:0},{id:1003, result:”shell return msg”}] }
        }
        return id;
    }*/
}
