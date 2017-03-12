package com.zslin.client.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimpleSpecificationBuilder;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.client.model.ClientCode;
import com.zslin.client.model.ClientConfig;
import com.zslin.client.model.Code;
import com.zslin.client.service.IClientCodeService;
import com.zslin.client.service.IClientConfigService;
import com.zslin.client.service.ICodeService;
import com.zslin.client.tools.ClientFileTools;
import com.zslin.client.tools.ClientJsonTools;
import com.zslin.sms.tools.RandomTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/5 15:03.
 */
@Controller
@RequestMapping(value = "admin/clientConfig")
@AdminAuth(name = "客户端管理", orderNum = 1, porderNum = 1, psn = "客户端管理", pentity = 0, icon = "fa fa-chain")
public class AdminClientConfigController {

    @Autowired
    private IClientConfigService clientConfigService;

    @Autowired
    private ICodeService codeService;

    @Autowired
    private IClientCodeService clientCodeService;

    @Autowired
    private ClientFileTools clientFileTools;

    @AdminAuth(name="客户端管理", orderNum=1, icon="fa fa-chain", type="1")
    @RequestMapping(value="index", method= RequestMethod.GET)
    public String index(Model model, HttpServletRequest request) {
        ClientConfig cc = clientConfigService.loadOne();
        if(cc==null) {cc = new ClientConfig();}
        model.addAttribute("clientConfig", cc);
        return "admin/clientConfig/index";
    }

    @RequestMapping(value="index", method=RequestMethod.POST)
    public String index(Model model, ClientConfig clientConfig, HttpServletRequest request) {

        ClientConfig c = clientConfigService.loadOne();
        if(c==null) {
            clientConfig.setToken(RandomTools.randomString(10));
            clientConfigService.save(clientConfig);
            send2Client(clientConfig);
        } else {
            MyBeanUtils.copyProperties(clientConfig, c, new String[]{"id", "token"});
            clientConfigService.save(c);
            send2Client(c);
        }

        return "redirect:/admin/clientConfig/index";
    }

    /** 授权 */
    @GetMapping(value = "auth")
    @AdminAuth(name="客户端授权", orderNum=5, icon = "fa fa-key")
    public String auth(Model model, Integer id, HttpServletRequest request) {
        model.addAttribute("clientConfig", clientConfigService.findOne(id));
        SimpleSpecificationBuilder builder = new SimpleSpecificationBuilder("uid", "eq", id);
        List<ClientCode> list = clientCodeService.findAll(builder.generate());
        model.addAttribute("datas", list); //已经拥有的权限

        List<Code> codeList = codeService.findAll();
        model.addAttribute("codeList", codeList);
        return "admin/clientConfig/auth";
    }

    @RequestMapping(value="auth", method=RequestMethod.POST)
    public @ResponseBody String auth(Integer uid, Integer cid) {
        try {
            ClientCode uc = clientCodeService.findByUidAndCid(uid, cid);
            if(uc==null) {
                uc = new ClientCode();
                ClientConfig u = clientConfigService.findOne(uid);
                Code c = codeService.findOne(cid);
                uc.setToken(u.getToken());
                uc.setCid(cid);
                uc.setCode(c.getC());
                uc.setMethodName(c.getMethodName());
                uc.setServiceName(c.getServiceName());
                uc.setUid(uid);
                clientCodeService.save(uc);
            } else {
                clientCodeService.delete(uc);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
        return "1";
    }

    private void send2Client(ClientConfig cc) {
        String json = ClientJsonTools.buildDataJson(ClientJsonTools.buildConfig(cc));
        clientFileTools.setConfigContext(json);
    }
}
