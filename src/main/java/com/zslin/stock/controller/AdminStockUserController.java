package com.zslin.stock.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.annotations.Token;
import com.zslin.basic.exception.SystemException;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.tools.TokenTools;
import com.zslin.multi.dao.IStoreDao;
import com.zslin.multi.model.Store;
import com.zslin.stock.model.StockUser;
import com.zslin.stock.model.StockUserStore;
import com.zslin.stock.service.IStockUserService;
import com.zslin.stock.service.IStockUserStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by zsl on 2018/5/21.
 * 库存管理-分类管理
 */
@Controller
@RequestMapping(value = "admin/stockUser")
@AdminAuth(name = "仓管员管理", psn = "库存管理", orderNum = 1, porderNum = 1, pentity = 0, icon = "fa fa-folder-open")
public class AdminStockUserController {

    @Autowired
    private IStockUserService stockUserService;

    @Autowired
    private IStoreDao storeDao;

    @Autowired
    private IStockUserStoreService stockUserStoreService;

    /** 列表 */
    @AdminAuth(name = "仓管员管理", orderNum = 1, icon="fa fa-user", type="1")
    @RequestMapping(value="list", method=RequestMethod.GET)
    public String list(Model model, Integer page, HttpServletRequest request) {
//        Page<User> datas = userService.findAll(PageableUtil.basicPage(page));
        Page<StockUser> datas = stockUserService.findAll(SimplePageBuilder.generate(page));
        model.addAttribute("datas", datas);
        return "admin/stock/stockUser/list";
    }

    @AdminAuth(name="指定仓管店铺", orderNum=5)
    @RequestMapping(value="stores/{id}", method=RequestMethod.GET)
    public String stores(Model model, @PathVariable Integer id, HttpServletRequest request) {
        model.addAttribute("stockUser", stockUserService.findOne(id)); //获取当前用户
        List<Integer> storeIds = stockUserService.listStoreIds(id);
        StringBuffer sb = new StringBuffer();
        for(Integer rid : storeIds) {sb.append(rid).append(",");}
        sb.append("0");
        List<Store> storeList = storeDao.findByStatus("1"); //获取所有店铺
        model.addAttribute("storeList", storeList);
        model.addAttribute("storeIds", sb.toString()); //获取当前用户所拥有的角色Id
        return "admin/stock/stockUser/stores";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name = "添加仓管人员", orderNum = 2, icon="icon-plus")
    @RequestMapping(value="add", method=RequestMethod.GET)
    public String add(Model model, HttpServletRequest request) {
        StockUser user = new StockUser();
        user.setStatus("1");
        model.addAttribute("stockUser", user);
        return "admin/stock/stockUser/add";
    }

    /** 添加POST */
    @Token(flag= Token.CHECK)
    @RequestMapping(value="add", method=RequestMethod.POST)
    public String add(Model model, StockUser stockUser, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) {
            StockUser u = stockUserService.findByPhone(stockUser.getPhone());
            if(u!=null) {throw new SystemException("手机号码【"+stockUser.getPhone()+"】已经存在，不可重复添加！");}
            stockUserService.save(stockUser);
        }
        return "redirect:/admin/stockUser/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name="修改仓管人员", orderNum=3)
    @RequestMapping(value="update/{id}", method=RequestMethod.GET)
    public String update(Model model, @PathVariable Integer id, HttpServletRequest request) {
        StockUser u = stockUserService.findOne(id);
        model.addAttribute("stockUser", u);
        return "admin/stock/stockUser/update";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="update/{id}", method=RequestMethod.POST)
    public String update(Model model, @PathVariable Integer id, StockUser stockUser, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) {
            StockUser u = stockUserService.findOne(id);
            u.setStatus(stockUser.getStatus());
            u.setName(stockUser.getName());
            stockUserService.save(u);
        }
        return "redirect:/admin/stockUser/list";
    }

    @AdminAuth(name="删除用户", orderNum=4)
    @RequestMapping(value="delete/{id}", method=RequestMethod.POST)
    public @ResponseBody String delete(@PathVariable Integer id) {
        try {
            stockUserService.delete(id);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }

    @RequestMapping(value = "addOrDeleteUserStore", method = RequestMethod.POST)
    @AdminAuth(name="为用户分配角色", orderNum=5)
    public @ResponseBody String addOrDelUserRole(Integer userId, Integer storeId) {
        try {
            addOrDeleteUserStore(userId, storeId);
        } catch (Exception e) {
            throw new SystemException("为用户分配角色失败");
        }
        return "1";
    }

    /**
     * 添加或删除用户角色对应关系，如果存在则删除，如果不存在则添加
     * @param userId 用户Id
     * @param storeId 店铺Id
     */
    public void addOrDeleteUserStore(Integer userId, Integer storeId) {
        StockUserStore sus = stockUserStoreService.findByUserIdAndStoreId(userId, storeId);
        if(sus==null) {
            StockUser user = stockUserService.findOne(userId);
            Store store = storeDao.findOne(storeId);

            sus = new StockUserStore();
            sus.setStoreId(storeId);
            sus.setStoreSn(store.getSn());
            sus.setUserId(userId);
            sus.setUserPhone(user.getPhone());

            stockUserStoreService.save(sus);
        } else {
            stockUserStoreService.delete(sus);
        }
        rebuildStoreNames(userId);
    }

    /** 修改显示内容 */
    private void rebuildStoreNames(Integer userId) {
        List<String> sns = stockUserService.listStoreSns(userId);
        StringBuffer sb = new StringBuffer();
        for(String sn : sns) {
            sb.append(sn).append("；");
        }
        stockUserService.updateStoreSns(sb.toString(), userId);
    }
}
