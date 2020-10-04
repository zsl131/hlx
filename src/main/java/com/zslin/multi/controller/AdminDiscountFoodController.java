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
import com.zslin.multi.dao.IDiscountFoodDao;
import com.zslin.multi.dao.IStoreDao;
import com.zslin.multi.model.DiscountFood;
import com.zslin.multi.model.Store;
import com.zslin.web.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(value="admin/discountFood")
@AdminAuth(name="打折菜品管理", orderNum=10, psn="多店管理", pentity=0, porderNum=20)
public class AdminDiscountFoodController {

    @Autowired
    private IDiscountFoodDao discountFoodDao;

    @Autowired
    private IStoreDao storeDao;

    @Autowired
    private ClientFileTools clientFileTools;

    @Autowired
    private ICategoryService categoryService;

    @GetMapping(value = "list")
    @AdminAuth(name = "打折菜品管理", orderNum = 1, type = "1", icon = "fa fa-bank")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<DiscountFood> datas = discountFoodDao.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id_d")));
        model.addAttribute("datas", datas);
        return "admin/multi/discountFood/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name = "添加打折菜品", orderNum = 2, icon="fa fa-plus")
    @RequestMapping(value="add", method= RequestMethod.GET)
    public String add(Model model, HttpServletRequest request) {
        model.addAttribute("discountFood", new DiscountFood());
        List<Store> storeList = storeDao.findAll();
        model.addAttribute("storeList", storeList);
        return "admin/multi/discountFood/add";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="add", method=RequestMethod.POST)
    public String add(Model model, DiscountFood discountFood, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) { //不是重复提交
            DiscountFood d = discountFoodDao.findOne(discountFood.getId());
            if(d!=null) {
                throw new SystemException("打折菜品【"+discountFood.getFoodName()+"】已经存在");
            }
            discountFoodDao.save(discountFood);

            send2Client(discountFood, "save");
        }
        return "redirect:/admin/discountFood/list";
    }

    @AdminAuth(name="删除打折开关", orderNum=4, icon = "fa fa-remove")
    @RequestMapping(value="delete/{id}", method=RequestMethod.POST)
    public @ResponseBody
    String delete(@PathVariable Integer id) {
        try {
            DiscountFood dm = discountFoodDao.findOne(id);
            send2Client(dm, "save");
            discountFoodDao.delete(dm);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }

    private void send2Client(DiscountFood d, String action) {
        if(!"hlx".equals(d.getStoreSn())) { //如果不是hlx则需要传至客户端
            String content = ClientJsonTools.buildDataJson(ClientJsonTools.buildDiscountFood(d, action));
            clientFileTools.setChangeContext(d.getStoreSn(), content, true);
        }
    }
}
