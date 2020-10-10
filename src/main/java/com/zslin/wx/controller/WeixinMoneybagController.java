package com.zslin.wx.controller;

import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.NormalTools;
import com.zslin.multi.dao.IMoneybagDao;
import com.zslin.multi.dao.IMoneybagDetailDao;
import com.zslin.multi.dao.IStoreDao;
import com.zslin.multi.model.Moneybag;
import com.zslin.multi.model.MoneybagDetail;
import com.zslin.multi.model.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(value = "weixin/moneybag")
public class WeixinMoneybagController {

    @Autowired
    private IMoneybagDao moneybagDao;

    @Autowired
    private IMoneybagDetailDao moneybagDetailDao;

    @Autowired
    private IStoreDao storeDao;

    /*@PostMapping(value = "queryBag")
    public @ResponseBody Moneybag queryBag(String phone, String password) {
        Moneybag bag = moneybagDao.findByPhone(phone);
//System.out.println(bag);
        if(bag==null) {
            bag = new Moneybag();
            bag.setName("未检索到会员信息，请检查手机号码是否正确");
            return bag;
        } //未检索到会员信息
        else if(!password.equals(bag.getPassword())) {
            bag = new Moneybag();
            bag.setName("密码不正确");
            return bag;
        } //密码不正确
        else {return bag;}
    }*/

    @PostMapping(value = "queryBag")
    public @ResponseBody Moneybag queryBag(String phone) {
        Moneybag bag = moneybagDao.findByPhone(phone);
//System.out.println(bag);
        if(bag==null) {
            bag = new Moneybag();
            bag.setName("未检索到会员信息，请检查手机号码是否正确");
            return bag;
        } //未检索到会员信息
        else {return bag;}
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
        addDetail(bag, store, money, "会员充值");
        return "1";
    }

    /** 消费 */
    @PostMapping(value = "addDetail")
    public @ResponseBody String addDetail(Integer bagId, String sn, Float money, String password) {
        Moneybag bag = moneybagDao.findOne(bagId);
        Store store = storeDao.findBySn(sn);
        if(bag==null) {return "-1";} //没有找到会员信息
        if(store==null) {return "-2";} //没有找到店铺信息
        if(!password.equals(bag.getPassword())) {return "-10";} //密码不正确
        if(money>bag.getMoney()) {return "-3";} //账户余额不足

        addDetail(bag, store, 0-money, store.getName()+"-消费");
        return "1";
    }

    private MoneybagDetail addDetail(Moneybag bag, Store store, Float money, String reason) {
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
        detail.setSurplus(surplus);
        detail.setOptStoreId(store.getId());
        detail.setOptStoreName(store.getName());
        detail.setOptStoreSn(store.getSn());
        detail.setMoney(money);
        detail.setReason(reason);
        detail.setFlag(money>0?MoneybagDetail.FLAG_IN:MoneybagDetail.FLAG_OUT);
        moneybagDetailDao.save(detail);
        bag.setMoney(surplus);

        moneybagDao.save(bag);
        return detail;
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
    }
}
