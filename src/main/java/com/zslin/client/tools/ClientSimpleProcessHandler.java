package com.zslin.client.tools;

import com.alibaba.fastjson.JSON;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.client.model.Member;
import com.zslin.client.service.IMemberService;
import com.zslin.web.model.Account;
import com.zslin.web.model.MemberCharge;
import com.zslin.web.model.Wallet;
import com.zslin.web.service.IAccountService;
import com.zslin.web.service.IMemberChargeService;
import com.zslin.web.service.IWalletService;
import com.zslin.wx.dbtools.MoneyTools;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/17 22:50.
 */
@Component
public class ClientSimpleProcessHandler {

    @Autowired
    private IMemberChargeService memberChargeService;

    @Autowired
    private IMemberService memberService;

    @Autowired
    private IWalletService walletService;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private MoneyTools moneyTools;

    /** 处理充值或消费记录，只有添加 */
    public void handlerMemberCharge(JSONObject jsonObj) {
        MemberCharge mc = JSON.toJavaObject(JSON.parseObject(jsonObj.toString()), MemberCharge.class);
        mc.setId(null);
        setMemberCharge(mc);
        memberChargeService.save(mc);

        //店内会员充值
        moneyTools.processScoreByPhone(mc.getPhone(), (int)(mc.getChargeMoney()+mc.getGiveMoney())*100, mc.getChargeMoney()<=0?"会员消费":"会员充值");
    }

    //当电话号码匹配时需要设置充值记录相应字段
    private void setMemberCharge(MemberCharge mc) {
        String phone = mc.getPhone();
        if(phone!=null && !"".equalsIgnoreCase(phone)) {
            Account a = accountService.findByPhone(phone);
            if(a!=null) {
                mc.setOpenid(a.getOpenid());
                mc.setAccountId(a.getId());
                mc.setNickname(a.getNickname());
                mc.setHeadimg(a.getHeadimgurl());
            }
        }
    }

    /** 处理会员信息，在店内办理非微信会员 */
    public void handlerMember(JSONObject jsonObj, String action) {
        Member m = JSON.toJavaObject(JSON.parseObject(jsonObj.toString()), Member.class);
        if("delete".equalsIgnoreCase(action)) {
            memberService.deleteByPhone(m.getPhone());
        } else if("update".equalsIgnoreCase(action)) {
            Member member = memberService.findByPhone(m.getPhone());
            if (member == null) {
                m.setId(null);
                memberService.save(m);
                checkWallet(m); //添加会员信息时先检测有无钱包信息
            } else {
                MyBeanUtils.copyProperties(m, member);
                memberService.save(member);
                checkWallet(member); //添加会员信息时先检测有无钱包信息
            }
        }
    }

    private void checkWallet(Member m) {
        Wallet w = walletService.findByPhone(m.getPhone());
        Account a = accountService.findByPhone(m.getPhone());
        if(w==null) {
            w = new Wallet();
            w.setScore(0);
            w.setMoney(0); //都设置为0，是因为初始的时候都为0，具体增值在充值记录中操作
            w.setCreateLong(m.getCreateLong());
            w.setPhone(m.getPhone());
            w.setCreateDate(new Date());
            w.setCreateDay(m.getCreateDay());
            if(a!=null) {
                w.setAccountName(a.getNickname());
                w.setAccountId(a.getId());
                w.setOpenid(a.getOpenid());
            }
            walletService.save(w);
        } else if(a!=null && (w.getOpenid()==null || "".equalsIgnoreCase(w.getOpenid()))) {
            w.setAccountName(a.getNickname());
            w.setAccountId(a.getId());
            w.setOpenid(a.getOpenid());
            walletService.save(w);
        }
    }
}
