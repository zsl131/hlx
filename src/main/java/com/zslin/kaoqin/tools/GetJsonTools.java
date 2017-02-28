package com.zslin.kaoqin.tools;

import com.zslin.basic.tools.NormalTools;
import com.zslin.kaoqin.model.Company;
import com.zslin.kaoqin.model.DeviceAdvert;
import com.zslin.kaoqin.model.Worker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/26 23:07.
 */
public class GetJsonTools {

    /**
     * 生成配置的Json数据
     * @param company
     * @return
     */
    public static String buildConfigJson(Company company) {
        if(company==null) {return "";}
        StringBuffer sb = new StringBuffer();
        sb.append("{id:\"0\",do:\"update\",data:\"config\",name:\"").append(company.getName()).
                append("\",company:\"").append(company.getLongName()).append("\",companyid:").
                append(company.getId()).append(",max:3000,function:65535,delay:").append(company.getDelay()).
                append(",errdelay:").append(company.getErrdelay()).append(",timezone:+8,encrypt:0,expired:\"2055-12-10 12:10:10\"}");
        return sb.toString();
    }

    public static String buildWorkerJson(List<Worker> workerList) {
        StringBuffer sb = new StringBuffer();
        int index = 0;
        for(Worker w : workerList) {
            sb.append("{id:\"1001\",do:\"update\",data:\"user\",ccid:").append(w.getId()).
                    append(",name:\"").append(w.getName()).append("\",passwd:\"").append(w.getPassword()).
                    append("\",card:\"65852\",deptid:").append(w.getDepId()).append(",auth:").append(w.getAuth()).append(", faceexist:1}");
            if(index++<workerList.size()-1) {sb.append(",");}
        }
        return sb.toString();
    }

    public static String buildAdvertJson(List<DeviceAdvert> list, String path) {
        StringBuffer sb = new StringBuffer();
        //{id:\"1005\",do:\"update\",data:\"advert\",index:1,advert:\"base64\"}
        int i = 0;
        for(DeviceAdvert da : list) {
            sb.append("{id:\"1005\",do:\"update\",data:\"advert\",index:").append(da.getOrderNo()).
                    append(",advert:\"").append(PicTools.getPicBase64(path+da.getPicPath())).append("\"}");
            if(i++<list.size()-1) {sb.append(",");}
        }
        return sb.toString();
    }

    public static String buildDeleteWorkerJson(Integer... ids) {
        StringBuffer sb = new StringBuffer();
        sb.append("{id:\"1006\",do:\"delete\",data:[\"user\",\"fingerprint\",\"face\",\"headpic\",\"clockin\",\"pic\"],ccid:[");
        int i=0;
        for(Integer id:ids) {
            sb.append(id);
            if(i++<ids.length-1) {sb.append(",");}
        }
        sb.append("]}");
        return sb.toString();
    }

    //重启设备
    public static String buildRebootDeviceJson() {
        return "{id:\"1010\",do:\"cmd\",cmd:\"reboot\"}";
    }

    public static String buildWorkerJson(Worker... workers) {
        if(workers==null || workers.length<=0) {return null;}
        List<Worker> list = new ArrayList<>();
        for(Worker w : workers) {
            list.add(w);
        }
        return buildWorkerJson(list);
    }

    /** 生成部门Json数据，写死，只有这两个部门 */
    public static String buildDeparentJson() {
        String res = "{id:\"1004\",do:\"update\",data:\"dept\",dept:[{id:1,pid:0,name:\"前厅\"},{id:2,pid:0,name:\"后厨\"}]}";
        return res;
    }

    /** 生成清空设备的Json数据 */
    public static String buildCleanJson() {
        String res = "{id:\"1009\",do:\"delete\",data:[\"user\",\"fingerprint\",\"face\",\"headpic\",\"clockin\",\"pic\",\"dept\"]}";
        return res;
    }

    /**
     * 构建能发送的Json数据
     * @param dataJson
     * @return
     */
    public static String buildDataJson(String dataJson) {
        StringBuffer sb = new StringBuffer();
        sb.append("{status:1,info:\"ok\",data:[");
        sb.append(dataJson);
        sb.append("]}");
        return sb.toString();
    }

    public static String buildUnixTimeJson() {
        StringBuffer sb = new StringBuffer();
//        {status:1,info:\"ok\",data:{timezone:"UTC",unixtime:1476851317,datetime:"2016/10/19 12:28:37"}}
        sb.append("{status:1,info:\"ok\",data:{timezone:\"UTC\",unixtime:")
                .append(System.currentTimeMillis()/1000).append(",datetime:\"")
                .append(NormalTools.curDate("yyyy/MM/dd HH:mm:ss")).append("\"}}");
        return sb.toString();
    }
}
