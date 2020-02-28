package com.zslin.multi.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.annotations.Token;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.basic.tools.TokenTools;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.client.tools.ClientFileTools;
import com.zslin.client.tools.ClientJsonTools;
import com.zslin.multi.dao.IDiningTableDao;
import com.zslin.multi.dao.IStoreDao;
import com.zslin.multi.model.DiningTable;
import com.zslin.multi.model.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(value="admin/diningTable")
@AdminAuth(name="餐桌管理", orderNum=10, psn="多店管理", pentity=0, porderNum=20)
public class AdminDiningTableController {

    @Autowired
    private IStoreDao storeDao;

    @Autowired
    private IDiningTableDao diningTableDao;

    @Autowired
    private ClientFileTools clientFileTools;

    @GetMapping(value = "list")
    @AdminAuth(name = "餐桌管理", orderNum = 1, type = "1", icon = "fa fa-desktop")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<DiningTable> datas = diningTableDao.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("orderNo_d")));
        model.addAttribute("datas", datas);
        List<Store> storeList = storeDao.findAll();
        model.addAttribute("storeList", storeList);
        return "admin/multi/diningTable/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name = "添加餐桌", orderNum = 2, icon="fa fa-plus")
    @RequestMapping(value="add", method= RequestMethod.GET)
    public String add(Model model, HttpServletRequest request) {
        model.addAttribute("diningTable", new DiningTable());

        List<Store> storeList = storeDao.findAll();
        model.addAttribute("storeList", storeList);
        return "admin/multi/diningTable/add";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="add", method=RequestMethod.POST)
    public String add(Model model, DiningTable table, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) { //不是重复提交
            diningTableDao.save(table);

            send2Client(table, "save");
        }
        return "redirect:/admin/diningTable/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name="修改餐桌信息", orderNum=3, icon = "fa fa-pencil")
    @RequestMapping(value="update/{id}", method=RequestMethod.GET)
    public String update(Model model, @PathVariable Integer id, HttpServletRequest request) {
        DiningTable s = diningTableDao.findOne(id);
        model.addAttribute("diningTable", s);

        List<Store> storeList = storeDao.findAll();
        model.addAttribute("storeList", storeList);
        return "admin/multi/diningTable/update";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="update/{id}", method=RequestMethod.POST)
    public String update(Model model, @PathVariable Integer id, DiningTable table, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) {
            DiningTable d = diningTableDao.findOne(id);
            MyBeanUtils.copyProperties(table, d, "id");
            diningTableDao.save(d);

            send2Client(d, "save");
        }
        return "redirect:/admin/diningTable/list";
    }

    @AdminAuth(name="删除餐桌", orderNum=4, icon = "fa fa-remove")
    @RequestMapping(value="delete/{id}", method=RequestMethod.POST)
    public @ResponseBody
    String delete(@PathVariable Integer id) {
        try {
            DiningTable table = diningTableDao.findOne(id);
            send2Client(table, "delete");

            diningTableDao.delete(table);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }

    private void send2Client(DiningTable table, String action) {
        if(!"hlx".equals(table.getStoreSn())) { //如果不是hlx则需要传至客户端
            String content = ClientJsonTools.buildDataJson(ClientJsonTools.buildDiningTable(table, action));
            clientFileTools.setChangeContext(table.getStoreSn(), content, true);
        }
    }
}
