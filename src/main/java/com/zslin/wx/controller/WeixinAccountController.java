package com.zslin.wx.controller;

import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.repository.SimpleSpecificationBuilder;
import com.zslin.basic.tools.DateTools;
import com.zslin.basic.tools.NormalTools;
import com.zslin.client.service.IMemberService;
import com.zslin.client.tools.ClientFileTools;
import com.zslin.client.tools.ClientJsonTools;
import com.zslin.finance.dao.IFinancePersonalDao;
import com.zslin.finance.model.FinancePersonal;
import com.zslin.kaoqin.model.Worker;
import com.zslin.kaoqin.service.IWorkerService;
import com.zslin.multi.dao.IMoneybagDao;
import com.zslin.multi.dao.IMoneybagDetailDao;
import com.zslin.multi.model.Moneybag;
import com.zslin.multi.model.MoneybagDetail;
import com.zslin.sms.tools.RandomTools;
import com.zslin.sms.tools.SmsConfig;
import com.zslin.sms.tools.SmsTools;
import com.zslin.stock.model.StockUser;
import com.zslin.stock.service.IStockUserService;
import com.zslin.web.model.*;
import com.zslin.web.service.*;
import com.zslin.weixin.annotation.HasTemplateMessage;
import com.zslin.weixin.annotation.TemplateMessageAnnotation;
import com.zslin.weixin.model.HlxTicket;
import com.zslin.weixin.service.IHlxTicketService;
import com.zslin.weixin.tools.SendTemplateMessageTools;
import com.zslin.weixin.tools.TemplateMessageTools;
import com.zslin.wx.dbtools.ScoreAdditionalDto;
import com.zslin.wx.dbtools.ScoreTools;
import com.zslin.wx.tools.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/1 17:13.
 */
@Controller
@RequestMapping(value = "wx/account")
@HasTemplateMessage
public class WeixinAccountController {

    @Autowired
    private IAccountService accountService;

    @Autowired
    private IWalletService walletService;

    @Autowired
    private IOwnService ownService;

    @Autowired
    private IWalletDetailService walletDetailService;

    @Autowired
    private ICommentService commentService;

    @Autowired
    private IFeedbackService feedbackService;

    @Autowired
    private IQrcodeService qrcodeService;

    @Autowired
    private QrTools qrTools;

    @Autowired
    private SmsTools smsTools;

    @Autowired
    private SmsConfig smsConfig;

    @Autowired
    private ScoreTools scoreTools;

    @Autowired
    private IWorkerService workerService;

    @Autowired
    private IMemberChargeService memberChargeService;

//    @Autowired
//    private IOrdersService ordersService;

    @Autowired
    private IRulesService rulesService;

    @Autowired
    private IBuffetOrderService buffetOrderService;

    @Autowired
    private IWalletRemovedService walletRemovedService;

    @Autowired
    private IIncomeService incomeService;

    @Autowired
    private EventTools eventTools;

    @Autowired
    private IMemberService memberService;

    @Autowired
    private ClientFileTools clientFileTools;

    @Autowired
    private IMoneybagDao moneybagDao;

    @Autowired
    private IMoneybagDetailDao moneybagDetailDao;

    @Autowired
    private IHlxTicketService hlxTicketService;

    @Autowired
    private IFinancePersonalDao financePersonalDao;

    @Autowired
    private SendTemplateMessageTools sendTemplateMessageTools;

    @Autowired
    private IStockUserService stockUserService;

    //修改密码
    @PostMapping(value = "setPassword")
    public @ResponseBody String setPassword(String password, HttpServletRequest request) {
        try {
            String openid = SessionTools.getOpenid(request);
            walletService.updatePassword(password, openid);

            Wallet w = walletService.findByOpenid(openid);
            if(w.getPhone()!=null && !"".equals(w.getPhone())) {
                memberService.updatePassword(password, w.getPhone());
                moneybagDao.updatePasswordByPhone(password, w.getPhone());
                //TODO 此时应通知收银前端
                send2Client(w.getPhone(), password);
                send2Client(w);
            }

            StringBuffer sb = new StringBuffer();
            sb.append("新密码：").append(password).append("\\n")
                    .append("请妥善保管好新密码，当使用账户余额（现金、积分）消费时需要向收银员提供此密码！\\n若非本人操作，请及时点击修改！");
            eventTools.eventRemind(openid, "密码修改通知", "重要事件通知", DateTools.date2Str(new Date()), sb.toString(), "/wx/account/me");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "1";
    }

    private void send2Client(String phone, String password) {
        String content = ClientJsonTools.buildDataJson(ClientJsonTools.buildUpdatePassword(phone, password));
        clientFileTools.setChangeContext(content, true);
    }

    private void send2Client(Wallet w) {
        String content = ClientJsonTools.buildDataJson(ClientJsonTools.buildWallet(w));
        clientFileTools.setChangeContext(content, true);
    }

    //获取用户支付密码
    @PostMapping(value = "getPassword")
    public @ResponseBody String getPassword(HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        Wallet w = walletService.findByOpenid(openid);
        if(w==null) {
            return "-2"; //未初始化钱包
        } else {
            return w.getPassword()==null?"-1":w.getPassword();
        }
    }

    /** 设置生日 */
    @PostMapping(value = "setBirthday")
    public @ResponseBody String setBirthday(String birthday, String phone, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        moneybagDao.updateBirthday(birthday, phone);
        accountService.updateBirthday(birthday, openid);
        return "1";
    }

    //微信用户个人中心
    @GetMapping(value = "me")
    public String me(Model model, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        Account account = accountService.findByOpenid(openid);
        if(account==null) {
            model.addAttribute("hasError", true);
            model.addAttribute("message", "请取消关注后再重新关注");
        } else {
            StockUser stockUser = stockUserService.findByPhone(account.getPhone());
            model.addAttribute("stockUser", stockUser); //获取仓管人员
            FinancePersonal personal = financePersonalDao.findByOpenid(openid);
            model.addAttribute("personal", personal); //获取财务人员
            model.addAttribute("hadError", false);
            model.addAttribute("account", account);
            if(account.getPhone()!=null && !"".equals(account.getPhone())) { //如果已经绑定手机
                model.addAttribute("moneybag", moneybagDao.findByPhone(account.getPhone()));
            }
            model.addAttribute("wallet", walletService.findByOpenid(openid));
            model.addAttribute("pullCount", accountService.findPullCount(account.getId()));
//            model.addAttribute("ownCount", ownService.findCount(openid)); //礼物数量
//            model.addAttribute("commentCount", commentService.findCount(openid)); //评论数量
//            model.addAttribute("feedbackCount", feedbackService.findCount(openid)); //消息数量
            model.addAttribute("ticketCount", hlxTicketService.queryCount(openid, "0")); //可用卡券张数
            model.addAttribute("chargeCount", memberChargeService.findCount(openid)); //充值次数

            if (AccountTools.isPartner(account.getType())) {
                String storeSns = personal.getStoreSns();
                String storeSn = FinanceTools.getFirstStoreSn(storeSns);

                model.addAttribute("friendOrdersCount", buffetOrderService.findFriendCount(account.getPhone())); //友情折扣的次数
                if(FinanceTools.isNotNull(storeSn)) {
                    Double incomeMoney = incomeService.totalMoney(storeSn, NormalTools.curDate("yyyyMM"));
                    model.addAttribute("incomeMoney", incomeMoney == null ? 0 : incomeMoney);
//
//                    Double incomeMoney2 = incomeService.totalMoney(ClientFileTools.QWZW_SN, NormalTools.curDate("yyyyMM"));
//                    model.addAttribute("incomeMoney_qwzw", incomeMoney2 == null ? 0 : incomeMoney2);
                }
            }
        }
        return "weixin/account/me";
    }

    /** 卡券 */
    @GetMapping(value = "ticket")
    public String ticket(Model model, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        List<HlxTicket> ticketList = hlxTicketService.findByOpenidAndStatus(openid, "0");
        model.addAttribute("ticketList", ticketList);
        return "weixin/account/ticket";
    }

    //礼品列表
    @GetMapping(value = "own")
    public String own(Model model, Integer page, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        SimpleSpecificationBuilder builder = new SimpleSpecificationBuilder("openid", "eq", openid);
        Page<Own> datas = ownService.findAll(builder.generate(), SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("createDate_d")));
        model.addAttribute("datas", datas);
        model.addAttribute("ownCount", ownService.findCount(openid)); //礼物数量
        return "weixin/account/own";
    }

    @GetMapping(value = "score")
    public String score(Model model, Integer page, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        SimpleSpecificationBuilder builder = new SimpleSpecificationBuilder("openid", "eq", openid);
        builder.add("type", "eq", "2");
        Rules rules = rulesService.findByStoreSn();
        Wallet w = walletService.findByOpenid(openid);
        model.addAttribute("wallet", w);
        model.addAttribute("canMoney", rules.getScoreMoney()==null|| rules.getScoreMoney()<=0?0:NormalTools.buildPoint(w.getScore()*1.0/rules.getScoreMoney()));
        Page<WalletDetail> datas = walletDetailService.findAll(builder.generate(),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("createDate_d")));
        model.addAttribute("datas", datas);
        return "weixin/account/score";
    }

    @GetMapping(value = "money")
    public String money(Model model, Integer page, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        Account account = accountService.findByOpenid(openid);
        /*SimpleSpecificationBuilder builder = new SimpleSpecificationBuilder("openid", "eq", openid);
        builder.add("type", "eq", "1");
        model.addAttribute("wallet", walletService.findByOpenid(openid));
        Page<WalletDetail> datas = walletDetailService.findAll(builder.generate(),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("createDate_d")));*/

        Moneybag bag = moneybagDao.findByPhone(account.getPhone());
        if(bag!=null) {
            SimpleSpecificationBuilder builder = new SimpleSpecificationBuilder("phone", "eq", bag.getPhone());

            Page<MoneybagDetail> datas = moneybagDetailDao.findAll(builder.generate(),
                    SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id_d")));
            model.addAttribute("datas", datas);
            model.addAttribute("bag", bag);
        }
        return "weixin/account/money";
    }

    @GetMapping(value = "commentList")
    public String commentList(Model model, Integer page, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        SimpleSpecificationBuilder builder = new SimpleSpecificationBuilder("openid", "eq", openid);
        Page<Comment> datas = commentService.findAll(builder.generate(),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("createDate_d")));
        model.addAttribute("datas", datas);
        return "weixin/account/commentList";
    }

    @GetMapping(value = "feedbackList")
    public String feedbackList(Model model, Integer page, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        SimpleSpecificationBuilder builder = new SimpleSpecificationBuilder("openid", "eq", openid);
        Page<Feedback> datas = feedbackService.findAll(builder.generate(),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("createDate_d")));
        model.addAttribute("datas", datas);
        return "weixin/account/feedbackList";
    }

    @GetMapping(value = "getQr")
    public String getQr(Model model, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        Account a = accountService.findByOpenid(openid);
        if(a!=null) {
            Qrcode qr = qrcodeService.findByOpenid(openid);
            if(qr==null) {
                String path = qrTools.genUserQr(a.getId() + "", a.getHeadimgurl());
                Qrcode qrcode = new Qrcode();
                qrcode.setOpenid(openid);
                qrcode.setAccountId(a.getId());
                qrcode.setNickname(a.getNickname());
                qrcode.setQrPath(path);
                qrcode.setName(a.getNickname());
                qrcode.setHeadimg(a.getHeadimgurl());
                qrcode.setCreateDate(new Date());
                qrcode.setCreateLong(System.currentTimeMillis());
                qrcode.setCreateDay(NormalTools.curDate("yyyy-MM-dd"));
                qrcode.setCreateTime(NormalTools.curDate("yyyy-MM-dd HH:mm:ss"));
                qr = qrcodeService.findByOpenid(openid);
                if(qr==null) {
                    qrcodeService.save(qrcode);
                }
            }
            return "redirect:/weixin/qr?id="+a.getId();
        }
        return "redirect:/weixin/index";
    }

    @PostMapping(value = "updateQrName")
    public @ResponseBody String updateQrName(String name, HttpServletRequest request) {
        if(name!=null && !"".equals(name.trim())) {
            String openid = SessionTools.getOpenid(request);
            qrcodeService.updateName(name, openid);
        }
        return "1";
    }

    @GetMapping(value = "modifyPhone")
    public String modifyPhone(Model model, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        Account a = accountService.findByOpenid(openid);
        model.addAttribute("account", a);
        return "weixin/account/modifyPhone";
    }

    @PostMapping(value = "sendCode")
    public @ResponseBody String sendCode(String phone, HttpServletRequest request) {
        try {
            Account a = accountService.findByPhone(phone);
            if(a!=null) {return "-1";}
            String code = RandomTools.randomNum4();
            request.getSession().setAttribute("sms_code", code);
            smsTools.sendMsg(Integer.parseInt(smsConfig.getSendCodeIid()), phone, "code", code);
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
        return "1";
    }

    @PostMapping(value = "modifyPhone")
    @TemplateMessageAnnotation(name = "手机绑定成功通知", keys = "手机号码-绑定时间")
    public @ResponseBody String modifyPhone(String phone, String code, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        String sessionCode = (String) request.getSession().getAttribute("sms_code");
        if(code.equals(sessionCode)) {
            //walletService.modifyPhone(phone, openid); //不能先修改数据
            updateData(phone, openid);
            updateWallet(phone, openid);
            //处理积分
            scoreTools.processScore(openid, ScoreRule.BIND_PHONE, new ScoreAdditionalDto("手机号码", phone));

            sendTemplateMessageTools.send2Wx(openid, "手机绑定成功通知", "#", "操作提示",
                    TemplateMessageTools.field("手机号码", phone),
                    TemplateMessageTools.field("绑定时间", NormalTools.curDatetime()));

            return "1";
        } else {
            return "0";
        }
    }

    //修改员工数据和微信用户数据
    private void updateData(String phone, String openid) {
        Worker w = workerService.findByPhone(phone);
        if(w!=null) {
            Account a = accountService.findByOpenid(openid);
            w.setHeadimgurl(a.getHeadimgurl());
            w.setOpenid(openid);
            w.setAccountId(a.getId());
            workerService.save(w);

            a.setName(w.getName());
            a.setIdentity(w.getIdentity());
            a.setPhone(phone);
            a.setBindPhone("1");
            if("0".equals(a.getType())) {
                a.setType("1");
            }
            accountService.save(a);
        } else {
            accountService.modifyPhone(phone, openid);
        }
    }

    private void updateWallet(String phone, String openid) {
        Wallet w = walletService.findByPhone(phone);
        Wallet wxWallet = walletService.findByOpenid(openid);
        if(w!=null && wxWallet!=null && !w.getId().equals(wxWallet.getId())) { //两个钱包都不为空，且不是同一个
            Account a = accountService.findByOpenid(openid);
            w.setMoney(w.getMoney()+wxWallet.getMoney());
            w.setOpenid(openid);
            w.setAccountId(a.getId());
            w.setScore(w.getScore()+wxWallet.getScore());
            w.setAccountName(wxWallet.getAccountName());
            walletService.save(w);

            a.setHasCard(w.getMoney()>0?"1":"2");
            accountService.save(a);

            WalletRemoved wr = new WalletRemoved();
            wr.setAccountName(wxWallet.getAccountName());
            wr.setScore(wxWallet.getScore());
            wr.setAccountId(wxWallet.getAccountId());
            wr.setOpenid(wxWallet.getOpenid());
            wr.setMoney(wxWallet.getMoney());
            wr.setPassword(wxWallet.getPassword());
            wr.setPhone(wxWallet.getPhone());
            wr.setRemoveTime(NormalTools.curDate());
            wr.setCreateDate(wxWallet.getCreateDate());
            wr.setCreateDay(wxWallet.getCreateDay());
            wr.setCreateLong(wxWallet.getCreateLong());
            wr.setCreateTime(wxWallet.getCreateTime());
            walletRemovedService.save(wr);

            walletService.delete(wxWallet);
        } else {
            walletService.modifyPhone(phone, openid);
        }
    }

    @PostMapping(value = "sharePage")
    public @ResponseBody String sharePage(String type, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        if(openid==null || "".equals(openid)) {return "未检测到用户信息";}
        //type可以为SHARE和SHARE-FRIEND
        scoreTools.processScore(openid, type); //通知积分
        return "1";
    }
}
