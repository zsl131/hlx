package com.zslin.client.tools;

import com.zslin.kaoqin.service.IWorkerService;
import com.zslin.wx.tools.JsonTools;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/12 21:55.
 */
@Component
public class ClientPostHandler {

    @Autowired
    private IWorkerService workerService;

    public void handler(String json) {
        Integer status = Integer.parseInt(JsonTools.getJsonParam(json, "status"));
        if(status!=null && status==1) { //表示获取成功
            String datas = JsonTools.getJsonParam(json, "data");
            processDatas(datas);
        }
    }

    private void processDatas(String dataJson) {
        JSONArray array = new JSONArray(dataJson);
        for(int i=0; i<array.length();i++) {
            JSONObject jsonObj = array.getJSONObject(i);
            processSingleData(jsonObj);
        }
    }

    private void processSingleData(JSONObject jsonObj) {
        String action = jsonObj.getString("action"); //操作
        String type = jsonObj.getString("type"); //类型
        Integer dataId = jsonObj.getInt("dataId"); //对象Id
        JSONObject dataObj = jsonObj.getJSONObject("data"); //具体对象的Json数据
        if("worker".equals(type)) { //修改员工密码
            updateWorkerPwd(dataId, dataObj);
        }
    }

    private void updateWorkerPwd(Integer dataId, JSONObject jsonObj) {
        String password = jsonObj.getString("password");
        workerService.updatePwd(password, dataId);
    }
}
