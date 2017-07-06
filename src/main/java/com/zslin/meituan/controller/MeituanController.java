package com.zslin.meituan.controller;

import com.zslin.client.tools.ClientFileTools;
import com.zslin.client.tools.ClientJsonTools;
import com.zslin.meituan.model.MeituanShop;
import com.zslin.meituan.service.IMeituanShopService;
import com.zslin.web.dto.ReturnDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by 钟述林 393156105@qq.com on 2017/7/5 11:19.
 */
@Controller
@RequestMapping(value = "meituan")
public class MeituanController {

    @Autowired
    private IMeituanShopService meituanShopService;

    @Autowired
    private ClientFileTools clientFileTools;

    @RequestMapping(value = "bind")
    public @ResponseBody
    ReturnDto bind(HttpServletRequest request) {
        String ePoiId = request.getParameter("ePoiId");
        String token = request.getParameter("appAuthToken");
        meituanShopService.updateToken(ePoiId, token);
        MeituanShop shop = meituanShopService.findByPoiId(ePoiId);
        if(shop!=null) {
            sendShop2Client("update", shop);
        }
        return new ReturnDto("success");
    }

    @RequestMapping(value = "unbind")
    public ReturnDto unbind(HttpServletRequest request) {
        System.out.println("========================");
        Map<String, String[]> map = request.getParameterMap();
        for(String m : map.keySet()) {
            System.out.println(m+"========="+map.get(m)[0]);
        }
        return new ReturnDto("success");
    }

    private void sendShop2Client(String action, MeituanShop shop) {
        String content = ClientJsonTools.buildDataJson(ClientJsonTools.buildMeituanShop(action, shop));
        clientFileTools.setChangeContext(content, true);
    }
}
