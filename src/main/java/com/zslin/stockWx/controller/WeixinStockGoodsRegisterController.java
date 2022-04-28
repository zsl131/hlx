package com.zslin.stockWx.controller;

import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.repository.SpecificationOperator;
import com.zslin.basic.tools.DateTools;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.kaoqin.model.Worker;
import com.zslin.multi.dao.IStoreDao;
import com.zslin.multi.model.Store;
import com.zslin.stock.dto.GoodsDto;
import com.zslin.stock.model.GoodsRegister;
import com.zslin.stock.model.GoodsRegisterDetail;
import com.zslin.stock.model.StockGoods;
import com.zslin.stock.service.IGoodsRegisterDetailService;
import com.zslin.stock.service.IGoodsRegisterService;
import com.zslin.stock.service.IStockCategoryService;
import com.zslin.stock.service.IStockGoodsService;
import com.zslin.stock.tools.GoodsNoTools;
import com.zslin.stockWx.tools.StockNoticeTools;
import com.zslin.stockWx.tools.StockWxTools;
import com.zslin.wx.tools.SessionTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zsl on 2018/5/22.
 * 入库登记
 */
@Controller
@RequestMapping(value = "wx/stock/goodsRegister")
public class WeixinStockGoodsRegisterController {

    @Autowired
    private IStockCategoryService stockCategoryService;

    @Autowired
    private IStockGoodsService stockGoodsService;

    @Autowired
    private IGoodsRegisterService goodsRegisterService;

    @Autowired
    private IGoodsRegisterDetailService goodsRegisterDetailService;

    @Autowired
    private StockWxTools stockWxTools;

    @Autowired
    private GoodsNoTools goodsNoTools;

    @Autowired
    private StockNoticeTools stockNoticeTools;

    @Autowired
    private IStoreDao storeDao;

    @GetMapping(value = "listApply")
    public String listApply(Model model, String storeSn, Integer page, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        Page<GoodsRegister> datas = goodsRegisterService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request,
//                new SpecificationOperator("applyOpenid", "eq", openid),
                new SpecificationOperator("storeSn", "eq", storeSn)
                ),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id_d")));
        model.addAttribute("datas", datas);
        return "weixin/stock/goodsRegister/listApply";
    }

    @GetMapping(value="list")
    public String list(Model model, String storeSn, Integer page, HttpServletRequest request) {
        Page<GoodsRegister> datas = goodsRegisterService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request,
                new SpecificationOperator("storeSn", "eq", storeSn)),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id_d")));
        model.addAttribute("datas", datas);
        return "weixin/stock/goodsRegister/listApply";
    }

    @GetMapping(value = "show")
    public String show(Model model, String batchNo, HttpServletRequest request) {
        GoodsRegister ga = goodsRegisterService.findByBatchNo(batchNo);
        List<GoodsRegisterDetail> detailList = goodsRegisterDetailService.listByBatchNo(batchNo);
        List<GoodsDto> dtoList = stockGoodsService.listByIds(buildGoodsIds(detailList));
        model.addAttribute("detailList", detailList);
        model.addAttribute("dtoList", dtoList);
        model.addAttribute("goodsApply", ga);
        return "weixin/stock/goodsRegister/show";
    }

    private Integer [] buildGoodsIds(List<GoodsRegisterDetail> detailList) {
        Integer [] res = new Integer[detailList.size()];
        for(int i =0;i<detailList.size();i++) {
            res[i] = detailList.get(i).getGoodsId();
        }
        return res;
    }

    @PostMapping(value = "applyPost")
    public @ResponseBody String applyPost(String datas, String storeSn, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        Worker worker = stockWxTools.getLoginWorker(openid);
        GoodsRegister ga = new GoodsRegister();
        ga.setCreateDate(new Date());
        ga.setApplyName(worker.getName());
        ga.setApplyOpenid(openid);
        ga.setApplyPhone(worker.getPhone());
        ga.setCreateDay(DateTools.date2Str(new Date()));
        ga.setCreateLong(System.currentTimeMillis());
        ga.setCreateTime(DateTools.date2Str(new Date(), "HH:mm:ss"));
        Store store = storeDao.findBySn(storeSn);
        ga.setStoreName(store.getName());
        ga.setStoreId(store.getId());
        ga.setStoreSn(storeSn);
        Integer no = goodsNoTools.generateGoodsRegisterNo(storeSn);
        ga.setNo(no);
        ga.setBatchNo(goodsNoTools.buildApplyBatchNo(no));
        ga.setApplyDatas(datas);

        buildRegisterDetail(ga, datas);
        return "1";
    }

    private void buildRegisterDetail(GoodsRegister ga, String datas) {
        //datas = 1-1_2-5_
        String batchNo = ga.getBatchNo();
        StringBuffer sb = new StringBuffer();
        Integer totalAmount = 0;
        Integer amount1 = 0, amount2 = 0, amount3 = 0;

        String [] array = datas.split("_");
        for(String singleStr : array) {
            if(singleStr==null || "".equals(singleStr.trim()) || singleStr.indexOf("-")<0) {continue;}
            Integer goodsId = Integer.parseInt(singleStr.split("-")[0]);
            Integer amount = Integer.parseInt(singleStr.split("-")[1]);

            GoodsRegisterDetail gad = new GoodsRegisterDetail();

            if(amount>0) {
                StockGoods sg = stockGoodsService.findOne(goodsId);
                gad.setGoodsId(goodsId);
                gad.setBatchNo(ga.getBatchNo());
                gad.setCateId(sg.getCateId());
                gad.setCateName(sg.getCateName());
                gad.setLocationType(sg.getLocationType());
                gad.setName(sg.getName());
                gad.setNameFull(sg.getNameFull());
                gad.setNameShort(sg.getNameShort());
                gad.setNo(sg.getNo());
                gad.setAmount(amount);
                gad.setUnit(sg.getUnit());

                stockGoodsService.plusAmount(goodsId, amount); //入库

                goodsRegisterDetailService.save(gad);
            }
            totalAmount += amount;
            if("1".equals(gad.getLocationType())) {amount1 += amount;}
            else if("2".equals(gad.getLocationType())) {amount2 += amount;}
            else if("3".equals(gad.getLocationType())) {amount3 += amount;}
        }

        if(amount1>0) {
            sb.append("冻库：").append(amount1).append("  ");
        }
        if(amount2>0) {
            sb.append("仓库：").append(amount2).append("  ");
        }
        if(amount3>0) {
            sb.append("店内：").append(amount3).append("  ");
        }

        ga.setApplyAmount(totalAmount);
        ga.setApplyRemark(sb.toString());

        //TODO 通知
//        stockNoticeTools.noticeNewOuterApply(ga); //新增时通知

        goodsRegisterService.save(ga);
//        //TODO 通知
//        stockNoticeTools.noticeCheckOuterApply(batchNo);
    }

    @GetMapping(value = "apply")
    public String apply(Model model, String storeSn, HttpServletRequest request) {
        List<StockGoods> list = stockGoodsService.findAll(
                ParamFilterUtil.getInstance().buildSearch(model, request,
                        new SpecificationOperator("storeSn", "eq", storeSn)),
                SimpleSortBuilder.generateSort("locationType_a", "cateId_a"));
        buildStockGoods(list, model);
        return "weixin/stock/goodsRegister/apply";
    }

    private void buildStockGoods(List<StockGoods> list, Model model) {
        List<StockGoods> list1 = new ArrayList<>();
        List<StockGoods> list2 = new ArrayList<>();
        List<StockGoods> list3 = new ArrayList<>();
        for(StockGoods sg : list) {
            if("1".equals(sg.getLocationType())) {
                list1.add(sg);
            } else if("2".equals(sg.getLocationType())) {
                list2.add(sg);
            } else if("3".equals(sg.getLocationType())) {
                list3.add(sg);
            }
        }
        model.addAttribute("list1", list1);
        model.addAttribute("list2", list2);
        model.addAttribute("list3", list3);
    }
}
