package com.zslin.wx.controller;

import com.zslin.multi.dao.IMoneybagDao;
import com.zslin.multi.dao.IMoneybagDetailDao;
import com.zslin.multi.dao.IMoneybagSearchRecordDao;
import com.zslin.multi.dao.IStoreDao;
import com.zslin.weixin.annotation.HasTemplateMessage;
import com.zslin.weixin.tools.SendTemplateMessageTools;
import com.zslin.wx.tools.AccountTools;
import com.zslin.wx.tools.FinanceTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "weixin/moneybag")
@HasTemplateMessage
public class
WeixinMoneybagController {

    @Autowired
    private IMoneybagDao moneybagDao;

    @Autowired
    private IMoneybagDetailDao moneybagDetailDao;

    @Autowired
    private IStoreDao storeDao;

    @Autowired
    private IMoneybagSearchRecordDao moneybagSearchRecordDao;

    @Autowired
    private AccountTools accountTools;

    @Autowired
    private SendTemplateMessageTools sendTemplateMessageTools;

    @GetMapping(value = "index")
    public String index(Model model, String day, String sn, HttpServletRequest request) {
        day = FinanceTools.isNull(day)?"":day;
        return "redirect:/wx/moneybag/index?day="+day+"&sn="+sn;
    }

    /*@PostMapping(value = "queryBag")
    public @ResponseBody Moneybag queryBag(String phone) {
        Moneybag bag = moneybagDao.findByPhone(phone);
        if(bag==null) {
            bag = new Moneybag();
            bag.setName("未检索到会员信息，请检查手机号码是否正确");
            return bag;
        } //未检索到会员信息
        addRecord(bag, phone);
        return bag;
    }

    private void addRecord(Moneybag bag, String phone) {
        try {
            new Thread(() -> {
                MoneybagSearchRecord msr = new MoneybagSearchRecord();
                if(bag!=null && bag.getId()!=null && bag.getId()>0)  {
                    msr.setName(bag.getName());
                }
                msr.setCreateDay(NormalTools.curDate());
                msr.setCreateTime(NormalTools.curDatetime());
                msr.setCreateLong(System.currentTimeMillis());
                msr.setPhone(phone);
                moneybagSearchRecordDao.save(msr);
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping(value = "addDetail")
    public String addDetail(Model model, String sn) {
        model.addAttribute("sn", sn);
        return "weixin/moneybag/addDetail";
    }

    @GetMapping(value = "addBag")
    public String addBag(Model model, String sn) {

        model.addAttribute("sn", sn);
        return "weixin/moneybag/addBag";
    }

    @PostMapping(value = "addBag")
    public @ResponseBody String addBag(String sn, String name, String phone, Float money) {
        Store store = storeDao.findBySn(sn);
        if(store==null) {return "-1"; } //无法创建会员信息，可能店铺信息出错
        Moneybag bag = moneybagDao.findByPhone(phone);
        if(bag==null) {
            bag = addBag(store, name, phone);
        }
        if(bag==null) {return "-1"; } //无法创建会员信息，可能店铺信息出错
        addDetail(bag, store, money, "会员充值", 0f);
        noticeAdmin(bag, store, money);
        return "1";
    }

    @TemplateMessageAnnotation(name = "会员充值提醒", keys = "店铺名称-会员类型-充值金额-当前余额-充值时间")
    private void noticeAdmin(Moneybag bag, Store store, Float money) {
        List<String> openids = buildOpenids();
        String sep = "\\n";

        sendTemplateMessageTools.send2Wx(openids, "会员充值提醒", "", "有会员充值啦~",
                TemplateMessageTools.field("店铺名称", store.getName()),
                TemplateMessageTools.field("会员类型", bag.getName()+"-"+bag.getPhone()),
                TemplateMessageTools.field("充值金额", money+" 元"),
                TemplateMessageTools.field("充值时间", NormalTools.curDatetime()),
                TemplateMessageTools.field("当前余额："+bag.getMoney()+" 元"+sep+"如果有疑问，请与店长联系。"));
    }

    *//** 查看会员明细 *//*
    @PostMapping(value = "showDetail")
    public @ResponseBody List<MoneybagDetail> showDetail(String phone) {
        List<MoneybagDetail> detailList = moneybagDetailDao.findByPhone(phone, SimpleSortBuilder.generateSort("id_d"));
        return detailList;
    }

    private List<String> buildOpenids() {
        List<String> list = new ArrayList<>();
        list.add("o_TZkwbz0pzuCTmrWqMGNHriMHTo");
        return list;
    }

    *//** 消费 *//*
    @PostMapping(value = "addDetail")
    public @ResponseBody String addDetail(Integer bagId, String sn, Float money, String password, Float dealMoney) {
        Moneybag bag = moneybagDao.findOne(bagId);
        Store store = storeDao.findBySn(sn);
        if(bag==null) {return "-1";} //没有找到会员信息
        if(store==null) {return "-2";} //没有找到店铺信息
        if(!password.equals(bag.getPassword())) {return "-10";} //密码不正确
        if(money>bag.getMoney()) {return "-3";} //账户余额不足

        addDetail(bag, store, 0-money, store.getName()+"-消费", dealMoney);
        return "1";
    }

    @TemplateMessageAnnotation(name = "会员消费提醒", keys = "会员卡号-店铺名称-消费金额")
    private MoneybagDetail addDetail(Moneybag bag, Store store, Float money, String reason, Float dealMoney) {
        String flag = money>0?MoneybagDetail.FLAG_IN:MoneybagDetail.FLAG_OUT;
        Float surplus = bag.getMoney() + money; //当前剩余金额
        MoneybagDetail detail = new MoneybagDetail();
        detail.setCreateDay(NormalTools.curDate("yyyy-MM-dd"));
        detail.setCreateDate(NormalTools.curDate("yyyyMMdd"));
        detail.setCreateTime(NormalTools.curDate("yyyy-MM-dd HH:mm:ss"));
        detail.setCreateLong(System.currentTimeMillis());
        detail.setBagId(bag.getId());
        detail.setName(bag.getName());
        detail.setPhone(bag.getPhone());
        detail.setStoreId(bag.getStoreId());
        detail.setStoreName(bag.getStoreName());
        detail.setStoreSn(bag.getStoreSn());

        detail.setOptStoreId(store.getId());
        detail.setOptStoreName(store.getName());
        detail.setOptStoreSn(store.getSn());
        detail.setMoney(money);
        detail.setReason(reason);
        detail.setDealMoney(dealMoney);
        if(flag.equals(MoneybagDetail.FLAG_IN)) { //如果是入账都设置为冻结，第二才自动解冻
            detail.setFreezeFlag("1");
        } else {detail.setFreezeFlag("0");}
        detail.setFlag(flag);
        detail.setSurplus(surplus+bag.getFreezeMoney());
        moneybagDetailDao.save(detail);
        if(flag.equals(MoneybagDetail.FLAG_OUT)) { //如果是出账才变化金额，入账时金额不变，自动变化
            bag.setMoney(surplus);
        } else { //入账时先冻结
            bag.setFreezeMoney(bag.getFreezeMoney()+money); //充多笔时需要加起来
        }

        moneybagDao.save(bag);

        String openid = accountTools.queryOpenid(bag.getPhone());
        if(openid!=null && !"".equals(openid)) {
            String sep = "\\n";

            //会员卡号-店铺名称-消费金额
            sendTemplateMessageTools.send2Wx(openid, "会员消费提醒", "/wx/account/money", "您的会员账户发生变化啦~",
                    TemplateMessageTools.field("会员卡号", bag.getName()+"-"+bag.getPhone()),
                    TemplateMessageTools.field("店铺名称", store.getName()),
                    TemplateMessageTools.field("消费金额", money+" 元"),

                    TemplateMessageTools.field("可用余额："+surplus+" 元"+sep+"冻结金额："+bag.getFreezeMoney()+" 元"+sep+"若非本人操作，请及联系我们！"));
        }
        return detail;
    }

    private String buildFlagName(String flag) {
        if(MoneybagDetail.FLAG_IN.equals(flag)) {return "会员充值";}
        else {return  "消费扣款";}
    }

    private Moneybag addBag(Store store, String name, String phone) {
        Moneybag bag = null;
        if(store!=null) { //先初始化钱包，不能存入具体金额，金额在添加详情记录时修改
            bag = new Moneybag();
            bag.setCreateLong(System.currentTimeMillis());
            bag.setCreateTime(NormalTools.curDate("yyyy-MM-dd HH:mm:ss"));
            bag.setCreateDay(NormalTools.curDate("yyyy-MM-dd"));
            bag.setPassword(phone.substring(phone.length()-4));
            bag.setPhone(phone);
            bag.setName(name);
            bag.setStoreId(store.getId());
            bag.setStoreName(store.getName());
            bag.setStoreSn(store.getSn());
            moneybagDao.save(bag);
        }
        return bag;
    }

    @GetMapping(value = "index")
    public String index(Model model, String day, String sn, HttpServletRequest request) {
        day = buildDay(day); //重构Day

        Integer count1 = moneybagDetailDao.queryCount(MoneybagDetail.FLAG_IN, sn, day); //充值笔数
        Integer count2 = moneybagDetailDao.queryCount(MoneybagDetail.FLAG_OUT, sn, day); //消费笔数
        count1 = count1==null?0:count1;
        count2 = count2==null?0:count2;

        Float money1 = moneybagDetailDao.queryMoney(MoneybagDetail.FLAG_IN, sn, day); //充值金额
        Float money2 = moneybagDetailDao.queryMoney(MoneybagDetail.FLAG_OUT, sn, day); //消费金额
        money1 = money1==null?0:money1;
        money2 = money2==null?0:money2;

        model.addAttribute("day", day);
        model.addAttribute("sn", sn);

        model.addAttribute("count1", count1);
        model.addAttribute("count2", count2);
        model.addAttribute("money1", money1);
        model.addAttribute("money2", money2);
        return "weixin/moneybag/index";
    }

    @GetMapping(value = "listDetails")
    public String listDetails(Model model, String sn, String day) {
        day = buildDay(day); //重构Day
        List<MoneybagDetail> detailList = moneybagDetailDao.listByDay(day, sn, SimpleSortBuilder.generateSort("id_d"));
        model.addAttribute("detailList", detailList);
        model.addAttribute("day", day);
        model.addAttribute("sn", sn);
        return "weixin/moneybag/listDetails";
    }

    private String buildDay(String day) {
        if(day==null || "".equals(day)) {
            day = NormalTools.curDate("yyyyMMdd");
        }
        return day;
    }*/
}
