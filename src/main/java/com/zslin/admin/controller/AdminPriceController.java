package com.zslin.admin.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.annotations.Token;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.basic.tools.NormalTools;
import com.zslin.basic.tools.TokenTools;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.client.tools.ClientFileTools;
import com.zslin.client.tools.ClientJsonTools;
import com.zslin.multi.dao.IStoreDao;
import com.zslin.multi.model.Store;
import com.zslin.web.model.Commodity;
import com.zslin.web.model.Price;
import com.zslin.web.service.ICommodityService;
import com.zslin.web.service.IPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/25 23:46.
 */
@Controller
@RequestMapping(value = "admin/price")
@AdminAuth(name = "价格管理", psn = "会员管理", orderNum = 12, porderNum = 1, pentity = 0, icon = "fa fa-rmb")
public class AdminPriceController {

    @Autowired
    private IPriceService priceService;

    @Autowired
    private ClientFileTools clientFileTools;

    @Autowired
    private ICommodityService commodityService;

    @Autowired
    private IStoreDao storeDao;

    @GetMapping(value = "list")
    @AdminAuth(name = "价格配置列表", type = "1", orderNum = 1, icon = "fa fa-list")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<Price> datas = priceService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page));
        model.addAttribute("datas", datas);
        return "admin/price/list";
    }

    /*@AdminAuth(name="价格配置管理", orderNum=1, icon="fa fa-rmb", type="1")
    @RequestMapping(value="index", method= RequestMethod.GET)
    public String index(Model model, HttpServletRequest request) {
        Price price = priceService.loadOne();
        if(price==null) {price = new Price();}
        model.addAttribute("price", price);
        return "admin/price/index";
    }

    @RequestMapping(value="index", method=RequestMethod.POST)
    public String index(Model model, Price price, HttpServletRequest request) {

        Price p = priceService.loadOne();
        if(p==null) {
            priceService.save(price);
            send2Client(price);
        } else {
            MyBeanUtils.copyProperties(price, p, new String[]{"id"});
            priceService.save(p);
            send2Client(p);
        }

        request.getSession().setAttribute("price", price); //修改后需要修改一次Session中的值
        return "redirect:/admin/price/index";
    }*/


    @RequestMapping(value="index", method= RequestMethod.GET)
    public String index(Model model, HttpServletRequest request) {
        return "redirect:/admin/price/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name = "添加价格配置", orderNum = 2, icon="fa fa-plus")
    @RequestMapping(value="add", method= RequestMethod.GET)
    public String add(Model model, HttpServletRequest request) {
        model.addAttribute("price", new Price());

        List<Store> storeList = storeDao.findAll();
        model.addAttribute("storeList", storeList);
        return "admin/price/add";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="add", method=RequestMethod.POST)
    public String add(Model model, Price price, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) { //不是重复提交
            priceService.save(price);

            send2Client(price);
        }
        return "redirect:/admin/price/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name="修改价格配置", orderNum=3, icon = "fa fa-pencil")
    @RequestMapping(value="update/{id}", method=RequestMethod.GET)
    public String update(Model model, @PathVariable Integer id, HttpServletRequest request) {
        Price p = priceService.findOne(id);
        model.addAttribute("price", p);
        return "admin/price/update";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="update/{id}", method=RequestMethod.POST)
    public String update(Model model, @PathVariable Integer id, Price price, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) {
            Price p = priceService.findOne(id);
            MyBeanUtils.copyProperties(price, p, "storeSn", "storeName", "storeId");
            priceService.save(p);

            send2Client(p);
        }
        return "redirect:/admin/price/list";
    }

    private void send2Client(Price p) {
        String content = ClientJsonTools.buildDataJson(ClientJsonTools.buildPrice(p));
        clientFileTools.setChangeContext(p.getStoreSn(), content, true);
        buildCommodity(p);
    }

    //添加商品，在配置价格时修改
    private void buildCommodity(Price p) {
        Commodity speCommodity = commodityService.findByNo(Commodity.SPE_NO); //不开火餐券(简餐券)
        if(speCommodity==null) {
            speCommodity = new Commodity();
            speCommodity.setType("1");
            speCommodity.setNo(Commodity.SPE_NO);
            speCommodity.setName("牛排套餐");
        }
        speCommodity.setPrice(p.getSpeMoney());
        commodityService.save(speCommodity);
        send2Client(speCommodity, "update");

        Commodity speHalfCommodity = commodityService.findByNo(Commodity.SPE_HALF_NO); //不开火餐券(简餐券 半票)
        if(speHalfCommodity==null) {
            speHalfCommodity = new Commodity();
            speHalfCommodity.setType("1");
            speHalfCommodity.setNo(Commodity.SPE_HALF_NO);
            speHalfCommodity.setName("牛排套餐（儿童票）");
        }
        speHalfCommodity.setPrice(p.getSpeMoneyHalf());
        commodityService.save(speHalfCommodity);
        send2Client(speHalfCommodity, "update");

        Commodity breakfastCommodity = commodityService.findByNo(Commodity.BREAKFAST_NO); //早餐券
        if(breakfastCommodity==null) {
            breakfastCommodity = new Commodity();
            breakfastCommodity.setType("1");
            breakfastCommodity.setNo(Commodity.BREAKFAST_NO);
            breakfastCommodity.setName("午餐券");
        }
        breakfastCommodity.setPrice(p.getBreakfastPrice());
        commodityService.save(breakfastCommodity);
        send2Client(breakfastCommodity, "update");

        Commodity dinnerCommodity = commodityService.findByNo(Commodity.DINNER_NO); //晚餐券
        if(dinnerCommodity==null) {
            dinnerCommodity = new Commodity();
            dinnerCommodity.setType("2");
            dinnerCommodity.setNo(Commodity.DINNER_NO);
            dinnerCommodity.setName("晚餐券");
        }
        dinnerCommodity.setPrice(p.getDinnerPrice());
        commodityService.save(dinnerCommodity);
        send2Client(dinnerCommodity, "update");

        Commodity halfBreakfast = commodityService.findByNo(Commodity.HALF_BREADFAST_NO); //早餐券（半票）
        if(halfBreakfast==null) {
            halfBreakfast = new Commodity();
            halfBreakfast.setType("1");
            halfBreakfast.setNo(Commodity.HALF_BREADFAST_NO);
            halfBreakfast.setName("午餐券（儿童票）");
        }
        halfBreakfast.setPrice((float)NormalTools.buildPoint(p.getBreakfastPrice()/2f));
        commodityService.save(halfBreakfast);
        send2Client(halfBreakfast, "update");

        Commodity halfDinner = commodityService.findByNo(Commodity.HALF_DINNER_NO); //晚餐券（半票）
        if(halfDinner==null) {
            halfDinner = new Commodity();
            halfDinner.setType("2");
            halfDinner.setNo(Commodity.HALF_DINNER_NO);
            halfDinner.setName("晚餐券（儿童票）");
        }
        halfDinner.setPrice((float)NormalTools.buildPoint(p.getDinnerPrice()/2f));
        commodityService.save(halfDinner);
        send2Client(halfDinner, "update");

        Commodity freeCom = commodityService.findByNo(Commodity.FREE_NO); //免票券
        if(freeCom==null) {
            freeCom = new Commodity();
            freeCom.setType("2");
            freeCom.setNo(Commodity.FREE_NO);
            freeCom.setName("免票券");
        }
        freeCom.setPrice(0f);
        commodityService.save(freeCom);
        send2Client(freeCom, "update");
    }

    public void send2Client(Commodity commodity, String action) {
        String content = ClientJsonTools.buildDataJson(ClientJsonTools.buildCommodity(commodity, action));
        clientFileTools.setChangeContext(commodity.getStoreSn(), content, true);
    }
}
