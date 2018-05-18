package com.zslin.admin.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.DateTools;
import com.zslin.basic.tools.NormalTools;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.client.service.IMemberService;
import com.zslin.client.tools.ClientFileTools;
import com.zslin.client.tools.ClientJsonTools;
import com.zslin.web.model.Wallet;
import com.zslin.web.model.WalletDetail;
import com.zslin.web.service.IWalletService;
import com.zslin.wx.dbtools.MoneyTools;
import com.zslin.wx.dbtools.ScoreTools;
import com.zslin.wx.tools.EventTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/25 23:46.
 */
@Controller
@RequestMapping(value = "admin/wallet")
@AdminAuth(name = "钱包管理", psn = "应用管理", orderNum = 12, porderNum = 1, pentity = 0, icon = "fa fa-shopping-bag")
public class AdminWalletController {

    @Autowired
    private IWalletService walletService;

    @Autowired
    private MoneyTools moneyTools;

    @Autowired
    private ScoreTools scoreTools;

    @Autowired
    private ClientFileTools clientFileTools;

    @Autowired
    private IMemberService memberService;

    @Autowired
    private EventTools eventTools;

    /**
     * 由于最开始没有设计支付密码，现在新增该功能后需要为每个用户初始化一个密码
     * 此功能只能使用一次
     * @return
     */
    @GetMapping(value = "initAllPasswordUseOnce")
    public @ResponseBody String initAllPasswordUseOnce() {
        List<Wallet> list = walletService.findAll();
        for(Wallet w : list) {
            initPwd(w.getId());
        }
        return "1";
    }

    @PostMapping(value = "initPassword")
    public @ResponseBody String initPassword(Integer id) {
        initPwd(id);
        return "1";
    }

    private void initPwd(Integer id) {
        try {
            Wallet w = walletService.findOne(id);
            if(w!=null && w.getPhone()!=null && !"".equalsIgnoreCase(w.getPhone())) {
                String password = "0000";
                walletService.updatePasswordByPhone(password, w.getPhone());

                memberService.updatePassword(password, w.getPhone());
                //TODO 此时应通知收银前端
                send2Client(w.getPhone(), password);

                if(w.getOpenid()!=null && !"".equalsIgnoreCase(w.getOpenid())) {
                    StringBuffer sb = new StringBuffer();
                    sb.append("新密码：").append(password).append("\\n")
                            .append("请妥善保管好新密码，当使用账户余额（现金、积分）消费时需要向收银员提供此密码！");
                    eventTools.eventRemind(w.getOpenid(), "密码初始化通知", "重要事件通知", DateTools.date2Str(new Date()), sb.toString(), "/wx/account/me");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void send2Client(String phone, String password) {
        String content = ClientJsonTools.buildDataJson(ClientJsonTools.buildUpdatePassword(phone, password));
        clientFileTools.setChangeContext(content, true);
    }

    @GetMapping(value = "list")
    @AdminAuth(name = "钱包管理", type = "1", orderNum = 1, icon = "fa fa-shopping-bag")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<Wallet> datas = walletService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("createDate_d")));
        model.addAttribute("datas", datas);
        return "admin/wallet/list";
    }

    @RequestMapping(value = "plus", method = RequestMethod.POST)
    @AdminAuth(name = "钱包充值", orderNum = 1, icon = "fa fa-plus")
    public @ResponseBody String plus(String phone, String type, Float amount, String reason) {
        try {
            //type-1-积分；type-2-现金
            if("2".equals(type)) {
                moneyTools.processScoreByPhone(phone, (int) (amount * 100), reason);
                sendWalletDetail2Client(phone, (int) (amount * 100));
            } else if("1".equals(type)) { //如果是对积分的操作，页面传过来的phone应该为openid
                scoreTools.processScoreByAmount(phone, (int) (amount*1), reason);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
        return "1";
    }

    private void sendWalletDetail2Client(String phone, Integer amount) {
        Wallet w = walletService.findByPhone(phone);
        WalletDetail wd = new WalletDetail();
        wd.setPhone(phone);
        wd.setType("1");
        wd.setCreateDay(NormalTools.curDate("yyyy-MM-dd"));
        wd.setCreateLong(System.currentTimeMillis());
        wd.setCreateTime(NormalTools.curDate("yyyy-MM-dd HH:mm:ss"));
        wd.setAmount(amount);
        wd.setOpenid(w.getOpenid());
        wd.setAccountId(w.getAccountId());
        wd.setAccountName(w.getAccountName());
        wd.setId(1);

        String content = ClientJsonTools.buildDataJson(ClientJsonTools.buildWalletDetail(wd));
        clientFileTools.setChangeContext(content, true);
    }
}
