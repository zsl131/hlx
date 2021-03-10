package com.zslin.weixin.controller;

import com.zslin.client.tools.ClientFileTools;
import com.zslin.weixin.model.OrderTicket;
import com.zslin.weixin.service.IOrderTicketService;
import com.zslin.weixin.tools.OrderTicketTools;
import com.zslin.wx.tools.QrTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 订单领券
 */
@RestController
@RequestMapping(value = "weixin/orderTicket")
public class WeixinOrderTicketController {

    @Autowired
    private IOrderTicketService orderTicketService;

    @Autowired
    private OrderTicketTools orderTicketTools;

    @Autowired
    private QrTools qrTools;

    /**
     * 生成卡券
     * @param orderNo
     * @param request
     * @return
     */
    @GetMapping(value = "buildQr")
    public @ResponseBody
    String buildQr(String orderNo, String money, String sn, HttpServletRequest request) {
        money = (money==null || "".equals(money.trim()))?OrderTicketTools.VALUE:money; //默认使用以前的100元
        sn = (sn==null || "".equals(sn.trim()))? ClientFileTools.HLX_SN:sn;
        sn = ("qwzw".equalsIgnoreCase(sn) || "qwzw_auto".equalsIgnoreCase(sn))?"qwzw":sn; //处理签王之王，不能有下划线
        String value = orderTicketTools.buildTicketNo(sn, money, orderNo);

        OrderTicket ot = orderTicketService.findByOrderNo(orderNo);
        if(ot==null) {
            ot = new OrderTicket();
            ot.setOrderNo(orderNo);
            ot.setStatus("0");
            ot.setStoreSn(sn.equals("qwzw")?ClientFileTools.QWZW_SN:sn);
            orderTicketService.save(ot);
        }
        //System.out.println("-------->WeixinOrderTicketController--value::"+value);
        String path = qrTools.genTicketQr(value, false);
        path = buildUrl(request)+path;
        return path;
    }

    private String buildUrl(HttpServletRequest request) {
        StringBuffer sb = new StringBuffer();
        sb.append(request.getScheme()).append("://")
                .append(request.getServerName()).append(":")
                .append(request.getServerPort());
        return sb.toString();
    }
}
