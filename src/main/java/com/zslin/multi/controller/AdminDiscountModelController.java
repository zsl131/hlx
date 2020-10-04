package com.zslin.multi.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.annotations.Token;
import com.zslin.basic.exception.SystemException;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.TokenTools;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.client.tools.ClientFileTools;
import com.zslin.client.tools.ClientJsonTools;
import com.zslin.multi.dao.IDiscountModelDao;
import com.zslin.multi.dao.IStoreDao;
import com.zslin.multi.model.DiscountModel;
import com.zslin.multi.model.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(value="admin/discountModel")
@AdminAuth(name="打折开关管理", orderNum=10, psn="多店管理", pentity=0, porderNum=20)
public class AdminDiscountModelController {

    @Autowired
    private IDiscountModelDao discountModelDao;

    @Autowired
    private IStoreDao storeDao;

    @Autowired
    private ClientFileTools clientFileTools;

    @GetMapping(value = "list")
    @AdminAuth(name = "打折开关管理", orderNum = 1, type = "1", icon = "fa fa-bank")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<DiscountModel> datas = discountModelDao.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id_d")));
        model.addAttribute("datas", datas);
        return "admin/multi/discountModel/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name = "添加打折开关", orderNum = 2, icon="fa fa-plus")
    @RequestMapping(value="add", method= RequestMethod.GET)
    public String add(Model model, HttpServletRequest request) {
        model.addAttribute("discountModel", new DiscountModel());
        List<Store> storeList = storeDao.findAll();
        model.addAttribute("storeList", storeList);
        return "admin/multi/discountModel/add";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="add", method=RequestMethod.POST)
    public String add(Model model, DiscountModel discountModel, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) { //不是重复提交
            DiscountModel d = discountModelDao.loadOne(discountModel.getStoreSn());
            if(d!=null) {
                throw new SystemException("店铺编号【"+discountModel.getStoreSn()+"】已经存在");
            }
            discountModelDao.save(discountModel);

            send2Client(discountModel, "save");
        }
        return "redirect:/admin/discountModel/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name="修改打折开关", orderNum=3, icon = "fa fa-pencil")
    @RequestMapping(value="update/{id}", method=RequestMethod.GET)
    public String update(Model model, @PathVariable Integer id, HttpServletRequest request) {
        DiscountModel d = discountModelDao.findOne(id);
        model.addAttribute("discountModel", d);
        List<Store> storeList = storeDao.findAll();
        model.addAttribute("storeList", storeList);
        return "admin/multi/discountModel/update";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="update/{id}", method=RequestMethod.POST)
    public String update(Model model, @PathVariable Integer id, DiscountModel discountModel, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) {
            DiscountModel d = discountModelDao.findOne(id);
            d.setStatus(discountModel.getStatus());
            d.setPercent(discountModel.getPercent());
            discountModelDao.save(d);

            send2Client(d, "save");
        }
        return "redirect:/admin/discountModel/list";
    }

    @AdminAuth(name="删除打折开关", orderNum=4, icon = "fa fa-remove")
    @RequestMapping(value="delete/{id}", method=RequestMethod.POST)
    public @ResponseBody
    String delete(@PathVariable Integer id) {
        try {
            DiscountModel dm = discountModelDao.findOne(id);
            send2Client(dm, "save");
            discountModelDao.delete(dm);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }

    private void send2Client(DiscountModel dm, String action) {
        if(!"hlx".equals(dm.getStoreSn())) { //如果不是hlx则需要传至客户端
            String content = ClientJsonTools.buildDataJson(ClientJsonTools.buildDiscountModel(dm, action));
            clientFileTools.setChangeContext(dm.getStoreSn(), content, true);
        }
    }
}
