package com.zslin.client.tools;

import com.alibaba.fastjson.JSON;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.basic.tools.NormalTools;
import com.zslin.client.model.Member;
import com.zslin.client.model.Orders;
import com.zslin.client.service.IMemberService;
import com.zslin.web.model.*;
import com.zslin.web.service.*;
import com.zslin.wx.dbtools.MoneyTools;
import com.zslin.wx.dto.EventRemarkDto;
import com.zslin.wx.tools.EventTools;
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
    private IBuffetOrderService buffetOrderService;

    @Autowired
    private IBuffetOrderDetailService buffetOrderDetailService;

    @Autowired
    private MoneyTools moneyTools;

    @Autowired
    private ShopTools shopTools;

    @Autowired
    private EventTools eventTools;

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

    public void handlerBuffetOrder(JSONObject jsonObj) {
        BuffetOrder bo = JSON.toJavaObject(JSON.parseObject(jsonObj.toString()), BuffetOrder.class);
        String no = bo.getNo();
        BuffetOrder order = buffetOrderService.findByNo(no);
        if(order==null) {
            order = new BuffetOrder();
        }
        MyBeanUtils.copyProperties(bo, order, true);
        buffetOrderService.save(order);
        if("0".equalsIgnoreCase(order.getStatus()) && "4".equalsIgnoreCase(order.getType())) {
            noticeAdmin(order); //需要通知管理人员
        }
        shopTools.onShopping(order); //处理账户余额等信息
    }

    private void noticeAdmin(BuffetOrder o) {
        //如果是友情价订单并且状态为刚下订单，则通过管理员
        if("0".equalsIgnoreCase(o.getStatus()) && "4".equalsIgnoreCase(o.getType())) {
            //当有友情价是discountReason必须存老板手机号码
            String openid = accountService.findOpenidByPhone(o.getDiscountReason());
            if(openid!=null) {
                eventTools.eventRemind(openid,"亲情价折扣提醒", "有顾客需要亲情折扣价", NormalTools.curDate("yyyy-MM-dd HH:mm"),
                        "/wx/buffetOrders/confirmFriend?no="+o.getNo(),
                        new EventRemarkDto("订单编号", o.getNo()),
                        new EventRemarkDto("商品总数", o.getCommodityCount()+""),
                        new EventRemarkDto("订单金额", o.getTotalMoney()+ " 元"),
                        new EventRemarkDto("优惠金额", o.getDiscountMoney()+ " 元"),
                        new EventRemarkDto("", "点击查看可确认！"));
            }
        }
    }

    public void handlerBuffetOrderDetail(JSONObject jsonObj) {
        BuffetOrderDetail bod = JSON.toJavaObject(JSON.parseObject(jsonObj.toString()), BuffetOrderDetail.class);
        buffetOrderDetailService.save(bod);
    }

    /** 处理会员信息，在店内办理非微信会员 */
    public void handlerMember(JSONObject jsonObj, String action) {
        Member m = JSON.toJavaObject(JSON.parseObject(jsonObj.toString()), Member.class);
        if("delete".equalsIgnoreCase(action)) {
            memberService.deleteByPhone(m.getPhone());
        } else if("update".equalsIgnoreCase(action)) {
            Member member = memberService.findByPhone(m.getPhone());
            if (member == null) {
                member = new Member();
            }
            MyBeanUtils.copyProperties(m, member, true);
            memberService.save(member);
            checkWallet(member); //添加会员信息时先检测有无钱包信息
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
            w.setCreateTime(m.getCreateTime());
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
