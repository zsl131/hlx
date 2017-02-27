package com.zslin.kaoqin.controller;

import com.zslin.kaoqin.model.Company;
import com.zslin.kaoqin.service.ICompanyService;
import com.zslin.kaoqin.tools.GetJsonTools;
import com.zslin.kaoqin.tools.KaoqinFileTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/26 22:37.
 */
@RestController
@RequestMapping(value = "api/data")
public class MainController {

    @Autowired
    private ICompanyService companyService;

    @GetMapping(value = "get")
    public String get(String sn, String requesttime, HttpServletRequest request) {

//        System.out.println(requesttime+"==========="+sn);
//        Company company = new Company();
//        company.setId(1);
//        company.setName("玉盘珍");
//        company.setCompany("昭通市玉盘珍餐饮有限公司");
//        company.setDelay(20);
//        company.setErrdelay(30);
//        String configJson = GetJsonTools.buildConfigJson(company);
//        System.out.println(configJson);

        String configJson = GetJsonTools.buildDataJson(GetJsonTools.buildConfigJson(companyService.loadOne()));

        System.out.println(System.currentTimeMillis()/1000);

        String json = KaoqinFileTools.getFileContext();
        if(json==null || "".equals(json.trim())) {
            json = configJson;
        }
        System.out.println(json);
        KaoqinFileTools.setFileContext(configJson);
        return json;

//        return configJson;
//        return "{status:1,info:\"ok\",data:[{id:\"1004\",do:\"update\",data:\"dept\",dept:[{id:10,pid:0,name:\"财务部\"},{id:11,pid:0,name:\"研发部\"}]}]";
//        return "{status:1,info:\\\"ok\\\",data:[{{id:\"1001\",do:\"update\",data:\"user\",ccid:1236,name:\"张三\",passwd:\"123456\",card:\"65852\",deptid:11,auth:14, faceexist:1}]";
    }

    //获取Unix时间戳
    @GetMapping(value = "unixtime")
    public String unixtime(String sn, HttpServletRequest request) {
        String json = GetJsonTools.buildUnixTimeJson();
        return json;
    }

    //检测版本更新
    @GetMapping(value = "version")
    public String version(String model, String rom, String app) {
        String res = "{status:1,info:\"ok\",data:0}"; //无更新
        return res;
    }
}
