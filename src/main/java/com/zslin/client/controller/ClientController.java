package com.zslin.client.controller;

import com.zslin.client.service.IClientCodeService;
import com.zslin.client.service.IClientConfigService;
import com.zslin.client.service.ICodeService;
import com.zslin.client.tools.ClientFileTools;
import com.zslin.client.tools.ClientPostHandler;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
//        System.out.println("==ClientController.upload======"+token);
//        System.out.println("========json:"+json);

        clientPostHandler.handler(json);
    }

    /**
     * 设备从服务端下载数据
     * @param token 用于每个店铺的唯一标识
     * @param request
     * @return
     */
    @GetMapping(value = "download")
    public String download(String token, HttpServletRequest request) {

        if(token==null || "".equals(token.trim())) {token = "hlx";}
        System.out.println("--ClientController.download-----token:::"+token);
        String json = clientFileTools.getChangeContext(token);
        if(json==null || "".equals(json.trim())) {
            json = clientFileTools.getConfigContext(token);
            clientFileTools.setConfigContext(token, ""); //获取之后也需要清空内容
        }
        System.out.println(json);
        clientFileTools.setChangeContext(token,"", false); //处理完成后清空内容
        return json;
    }
}
