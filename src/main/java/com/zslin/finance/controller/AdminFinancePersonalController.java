package com.zslin.finance.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.annotations.Token;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.tools.TokenTools;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.finance.dao.IFinancePersonalDao;
import com.zslin.finance.model.FinancePersonal;
import com.zslin.multi.dao.IStoreDao;
import com.zslin.web.model.Account;
import com.zslin.web.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(value = "admin/financePersonal")
@AdminAuth(name="财务人员管理", orderNum=10, psn="财务管理", pentity=0, porderNum=20)
public class AdminFinancePersonalController {

    @Autowired
    private IFinancePersonalDao financePersonalDao;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private IStoreDao storeDao;

    @GetMapping(value = "list")
    @AdminAuth(name = "财务人员管理", orderNum = 1, type = "1", icon = "fa fa-user-circle-o")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<FinancePersonal> datas = financePersonalDao.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page));
        model.addAttribute("datas", datas);
        model.addAttribute("storeList", storeDao.findByStatus("1"));
        return "admin/finance/financePersonal/list";
    }

    /** 通过昵称或电话获取用户信息 */
    @PostMapping(value = "queryAccount")
    public @ResponseBody
    List<Account> queryAccount(String query) {
        List<Account> list = accountService.query(query);
        return list;
    }

    @Token(flag= Token.READY)
    @AdminAuth(name = "添加财务人员", orderNum = 2, icon="fa fa-plus")
    @RequestMapping(value="add", method= RequestMethod.GET)
    public String add(Model model, HttpServletRequest request) {
        model.addAttribute("financePersonal", new FinancePersonal());
        model.addAttribute("storeList", storeDao.findByStatus("1"));
        return "admin/finance/financePersonal/add";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="add", method=RequestMethod.POST)
    public String add(Model model, FinancePersonal financePersonal, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) { //不是重复提交
            if(financePersonalDao.findByOpenid(financePersonal.getOpenid())==null) { //同一用户不能被添加两次
                if("0".equals(financePersonal.getType())) { //如果指定是签收人员，则需要设置收货标记
                    financePersonal.setMarkFlag("1");
                }
                financePersonalDao.save(financePersonal);
            }
        }
        return "redirect:/admin/financePersonal/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name="修改财务人员信息", orderNum=3, icon = "fa fa-pencil")
    @RequestMapping(value="update/{id}", method=RequestMethod.GET)
    public String update(Model model, @PathVariable Integer id, HttpServletRequest request) {
        FinancePersonal p = financePersonalDao.findOne(id);
        model.addAttribute("personal", p);
        model.addAttribute("storeList", storeDao.findByStatus("1"));
        return "admin/finance/financePersonal/update";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="update/{id}", method=RequestMethod.POST)
    public String update(Model model, @PathVariable Integer id, FinancePersonal personal, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) {
            FinancePersonal c = financePersonalDao.findOne(id);
            c.setName(personal.getName());
            c.setPhone(personal.getPhone());
            c.setType(personal.getType());
            c.setMarkFlag(personal.getMarkFlag());
            c.setStoreName(personal.getStoreName());
            c.setStoreSn(personal.getStoreSn());
            c.setIsPartner(personal.getIsPartner());
            c.setPartStores(personal.getPartStores());
            c.setIsCasher(personal.getIsCasher());
            c.setCashStores(personal.getCashStores());
            c.setStoreSns(personal.getStoreSns());
            financePersonalDao.save(c);
        }
        return "redirect:/admin/financePersonal/list";
    }

    @AdminAuth(name="删除财务人员", orderNum=4, icon = "fa fa-remove")
    @RequestMapping(value="delete/{id}", method=RequestMethod.POST)
    public @ResponseBody
    String delete(@PathVariable Integer id) {
        try {
            financePersonalDao.delete(id);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }
}
