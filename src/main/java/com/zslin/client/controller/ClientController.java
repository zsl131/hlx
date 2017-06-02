package com.zslin.client.controller;

import com.zslin.client.dto.ResDto;
import com.zslin.client.model.ClientCode;
import com.zslin.client.model.Code;
import com.zslin.client.service.IClientCodeService;
import com.zslin.client.service.IClientConfigService;
import com.zslin.client.service.ICodeService;
import com.zslin.client.tools.ClientFileTools;
import com.zslin.client.tools.ClientPostHandler;
import com.zslin.client.tools.SmsException;
import com.zslin.client.tools.SmsSucException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/10 10:21.
 */
@RestController
@RequestMapping(value = "client/api")
public class ClientController {

    @Autowired
    private BeanFactory factory;

    @Autowired
    private IClientConfigService clientConfigService;

    @Autowired
    private IClientCodeService clientCodeService;

    @Autowired
    private ICodeService codeService;

    @Autowired
    private ClientFileTools clientFileTools;

    @Autowired
    private ClientPostHandler clientPostHandler;

    @PostMapping(value = "upload")
    public void upload(String token, @RequestBody String json) {
//        System.out.println("========"+token);
//        System.out.println("========json:"+json);

        clientPostHandler.handler(json);
    }

    //设备从服务端下载数据
    @GetMapping(value = "download")
    public String download(String token, HttpServletRequest request) {

        String json = clientFileTools.getChangeContext();
        if(json==null || "".equals(json.trim())) {
            json = clientFileTools.getConfigContext();
        }
//        System.out.println(json);
        clientFileTools.setChangeContext("", false); //处理完成后清空内容
        return json;
    }
}
