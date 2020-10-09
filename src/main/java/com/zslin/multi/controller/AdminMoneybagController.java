package com.zslin.multi.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.annotations.Token;
import com.zslin.basic.exception.SystemException;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.NormalTools;
import com.zslin.basic.tools.TokenTools;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.multi.dao.IMoneybagDao;
import com.zslin.multi.dao.IMoneybagDetailDao;
import com.zslin.multi.dao.IStoreDao;
import com.zslin.multi.model.Moneybag;
import com.zslin.multi.model.MoneybagDetail;
import com.zslin.multi.model.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value="admin/moneybag")
@AdminAuth(name="会员管理", orderNum=10, psn="多店管理", pentity=0, porderNum=20)
public class AdminMoneybagController {

    @Autowired
    private IMoneybagDao moneybagDao;

    @Autowired
    private IStoreDao storeDao;

    @Autowired
    private IMoneybagDetailDao moneybagDetailDao;

    @GetMapping(value = "list")
    @AdminAuth(name = "会员列表", orderNum = 1, type = "1", icon = "fa fa-money")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<Moneybag> datas = moneybagDao.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id_d")));
        model.addAttribute("datas", datas);
        model.addAttribute("storeList", storeDao.findAll());
        return "admin/multi/moneybag/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name = "添加会员", orderNum = 2, icon="fa fa-plus")
    @RequestMapping(value="add", method= RequestMethod.GET)
    public String add(Model model, HttpServletRequest request) {
        model.addAttribute("moneybag", new Moneybag());
        model.addAttribute("storeList", storeDao.findAll());
        return "admin/multi/moneybag/add";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="add", method=RequestMethod.POST)
    public String add(Model model, Moneybag moneybag, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) { //不是重复提交
            String phone = moneybag.getPhone();
            Moneybag m = moneybagDao.findByPhone(phone);
            if(m!=null) {
                throw new SystemException("会员【"+phone+"】已经存在");
            }
            moneybag.setPassword(phone.substring(phone.length()-4));
            moneybag.setCreateDay(NormalTools.curDate("yyyy-MM-dd"));
            moneybag.setCreateTime(NormalTools.curDate("yyyy-MM-dd HH:mm:ss"));
            moneybag.setCreateLong(System.currentTimeMillis());
            moneybagDao.save(moneybag);
        }
        return "redirect:/admin/moneybag/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name="修改会员信息", orderNum=3, icon = "fa fa-pencil")
    @RequestMapping(value="update/{id}", method=RequestMethod.GET)
    public String update(Model model, @PathVariable Integer id, HttpServletRequest request) {
        Moneybag m = moneybagDao.findOne(id);
        model.addAttribute("moneybag", m);
        return "admin/multi/moneybag/update";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="update/{id}", method=RequestMethod.POST)
    public String update(Model model, @PathVariable Integer id, Moneybag moneybag, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) {
            String phone = moneybag.getPhone();
            Moneybag m = moneybagDao.findOne(id);
            Moneybag mb = moneybagDao.findByPhone(phone);
            if(!id.equals(mb.getId())) {throw new SystemException("会员【"+phone+"】已经存在");}
            m.setName(moneybag.getName());
            m.setPhone(phone);
            m.setPassword(phone.substring(phone.length()-4));
            moneybagDao.save(m);
        }
        return "redirect:/admin/moneybag/list";
    }

    /** 后台充值 */
    @PostMapping(value = "plus")
    public @ResponseBody String plus(Float amount, String reason, Integer bagId) {
        try {
            Moneybag bag = moneybagDao.findOne(bagId);
            Float surplus = bag.getMoney() + amount; //当前剩余金额
            MoneybagDetail detail = new MoneybagDetail();
            detail.setCreateDay(NormalTools.curDate("yyyy-MM-dd"));
            detail.setCreateDate(NormalTools.curDate("yyyyMMdd"));
            detail.setCreateTime(NormalTools.curDate("yyyy-MM-dd HH:mm:ss"));
            detail.setCreateLong(System.currentTimeMillis());
            detail.setBagId(bagId);
            detail.setName(bag.getName());
            detail.setPhone(bag.getPhone());
            detail.setStoreId(bag.getStoreId());
            detail.setStoreName(bag.getStoreName());
            detail.setStoreSn(bag.getStoreSn());
            detail.setSurplus(surplus);
            detail.setOptStoreId(0);
            detail.setOptStoreName("后台操作");
            detail.setOptStoreSn("-");
            detail.setMoney(amount);
            detail.setReason(reason);
            detail.setFlag(amount>0?MoneybagDetail.FLAG_IN:MoneybagDetail.FLAG_OUT);
            moneybagDetailDao.save(detail);
            bag.setMoney(surplus);

            moneybagDao.save(bag);
            return "1";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
