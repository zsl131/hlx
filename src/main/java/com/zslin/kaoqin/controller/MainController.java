package com.zslin.kaoqin.controller;

import com.zslin.kaoqin.service.ICompanyService;
import com.zslin.kaoqin.tools.GetJsonTools;
import com.zslin.kaoqin.tools.KaoqinFileTools;
import com.zslin.kaoqin.tools.PostJsonTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/26 22:37.
 */
@RestController
@RequestMapping(value = "api/data")
public class MainController {

    @Autowired
    private PostJsonTools postJsonTools;

    @Autowired
    private ICompanyService companyService;

    //设备向服务端推送数据
    @PostMapping(value = "post")
    public String post(String sn, @RequestBody String json) {
        System.out.println("=======sn:"+sn);
        System.out.println(json);
        String res = postJsonTools.handlerPost(json);
        return res;
    }

    //设备从服务端下载数据
    @GetMapping(value = "get")
    public String get(String sn, String requesttime, HttpServletRequest request) {

        String configJson = GetJsonTools.buildDataJson(GetJsonTools.buildConfigJson(companyService.loadOne()));

        System.out.println(System.currentTimeMillis()/1000);

        String json = KaoqinFileTools.getFileContext();
        if(json==null || "".equals(json.trim())) {
            json = configJson;
        }
        System.out.println(json);
        KaoqinFileTools.setFileContext(configJson);
        return json;
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
