package com.zslin.wx.controller;

import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.repository.SimpleSpecificationBuilder;
import com.zslin.basic.tools.NormalTools;
import com.zslin.web.model.Account;
import com.zslin.web.model.MemberCharge;
import com.zslin.web.model.MemberLevel;
import com.zslin.web.service.IAccountService;
import com.zslin.web.service.IMemberChargeService;
import com.zslin.web.service.IMemberLevelService;
import com.zslin.web.tools.CashierTools;
import com.zslin.wx.dbtools.MoneyTools;
import com.zslin.wx.tools.EventTools;
import com.zslin.wx.tools.SessionTools;
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
 * Created by 钟述林 393156105@qq.com on 2017/3/7 16:44.
 * 微信会员
 */
@Controller
@RequestMapping(value = "wx/member")
public class WeixinMemberController {

    @Autowired
    private IMemberLevelService memberLevelService;

    @Autowired
    private IMemberChargeService memberChargeService;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private CashierTools cashierTools;

    @Autowired
    private EventTools eventTools;

    @Autowired
    private MoneyTools moneyTools;

    /** 会员等级 */
    @GetMapping(value = "level")
    public String level(Model model, HttpServletRequest request) {
        SimpleSpecificationBuilder builder = new SimpleSpecificationBuilder("status", "eq", "1");
        List<MemberLevel> datas = memberLevelService.findAll(builder.generate(), SimpleSortBuilder.generateSort("level"));
        model.addAttribute("datas", datas);
        return "weixin/member/level";
    }

    @GetMapping(value = "charge")
    public String charge(Integer id, Model model, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        model.addAttribute("account", accountService.findByOpenid(openid)); //
        model.addAttribute("level", memberLevelService.findOne(id));
        return "weixin/member/charge";
    }

    @GetMapping(value = "chargeList")
    public String chargeList(Model model, Integer page, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        SimpleSpecificationBuilder builder = new SimpleSpecificationBuilder("openid", "eq", openid);
        Page<MemberCharge> datas = memberChargeService.findAll(builder.generate(),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("createDate_d")));
        model.addAttribute("datas", datas);
        return "weixin/member/chargeList";
    }

    @GetMapping(value = "showCharge")
    public String showCharge(Integer id, Model model, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        MemberCharge mc = memberChargeService.findOne(id);
        model.addAttribute("charge", mc);
        model.addAttribute("account", accountService.findByOpenid(openid)); //如果是管理人员可以查看并且可以设置状态
        return "weixin/member/showCharge";
    }

    //取消，只能是本人操作
    @PostMapping(value = "cancelCharge")
    public @ResponseBody String cancelCharge(Integer id, HttpServletRequest request) {
        try {
            String openid = SessionTools.getOpenid(request);
            memberChargeService.cancelCharge(id, openid);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }

    @PostMapping(value = "findChargeStatus")
    public @ResponseBody String findChargetStatus(Integer id) {
        String status = memberChargeService.findStatus(id);
        return status;
    }

    /*//审核通过，只能是非顾客人员操作
    @PostMapping(value = "verifyCharge")
    public @ResponseBody String verifyCharge(Integer id, HttpServletRequest request) {
        try {
            String openid = SessionTools.getOpenid(request);
            Account a = accountService.findByOpenid(openid);
            if(!"0".equals(a.getType())) {
                MemberCharge mc = memberChargeService.findOne(id);
                if(mc==null || !"0".equals(mc.getStatus())) {return "0";} //如果状态已经不是未审核状态，则不可以审核
                mc.setStatus("1");
                mc.setVerifyAccountId(a.getId());
                mc.setVerifyAccountName(a.getName());
                mc.setVerifyAccountOpenid(a.getOpenid());
                mc.setVerifyAccountPhone(a.getPhone());
                mc.setVerifyDate(new Date());
                memberChargeService.save(mc);

                accountService.updateCard(mc.getOpenid(), "1"); //修改用户为会员

                moneyTools.processScore(mc.getOpenid(), (mc.getChargeMoney()+mc.getGiveMoney())*100, "会员充值",
                        new ScoreAdditionalDto("充值金额", mc.getChargeMoney()+" 元"),
                        new ScoreAdditionalDto("赠送金额", mc.getGiveMoney()+" 元"),
                        new ScoreAdditionalDto("手机号码", mc.getPhone()),
                        new ScoreAdditionalDto("在您的允许下他人可通过该手机号码使用您的会员账户！"));

                return "1";
            }
        } catch (Exception e) {
        }
        return "0";
    }*/

    @PostMapping(value = "addCharge")
    public @ResponseBody String addCharge(Integer id, HttpServletRequest request) {
        try {
            String openid = SessionTools.getOpenid(request); //当前操作人员的openid
            Account a = accountService.findByOpenid(openid); //

            MemberLevel ml = memberLevelService.findOne(id);

            MemberCharge mc = new MemberCharge();
            mc.setAccountId(a.getId());
            mc.setPhone(a.getPhone());
            mc.setName(a.getName());
            mc.setBalance(ml.getChargeMoney()+ml.getGiveMoney());
            mc.setChargeMoney(ml.getChargeMoney()*1.0f);
            mc.setGiveMoney(ml.getGiveMoney()*1.0f);
            mc.setHeadimg(a.getHeadimgurl());
            mc.setNickname(a.getNickname());
            mc.setLevel(ml.getLevel());
            mc.setCreateDate(new Date());
            mc.setCreateLong(System.currentTimeMillis());
            mc.setCreateTime(NormalTools.curDate("yyyy-MM-dd HH:mm:ss"));
            mc.setCreateDay(NormalTools.curDate("yyyy-MM-dd"));
            mc.setOpenid(openid);
            mc.setStatus("0");
            memberChargeService.save(mc);

            //TODO 通知收银员
            List<String> openids = cashierTools.getCurrentCashier(); //获取当前收银人员
            eventTools.eventRemind(openids, "会员办理通知", "有人正在办理"+ml.getName(),
                    NormalTools.curDate("yyyy-MM-dd HH:mm:ss"), buildStr(a, ml), "/wx/member/showCharge?id="+mc.getId());

            return mc.getId()+"";
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }

    }
    private String buildStr(Account a, MemberLevel level) {
        StringBuffer sb = new StringBuffer();
        sb.append("会员等级：").append(level.getName()).append("\\n")
          .append("应付金额：").append(level.getChargeMoney()).append("元\\n")
          .append("办理客户：").append(a.getNickname()).append("\\n")
          .append("手机号码：").append(a.getPhone());
        return sb.toString();
    }
}
