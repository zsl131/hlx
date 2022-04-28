package com.zslin.stockWx.controller;

import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.repository.SimpleSpecificationBuilder;
import com.zslin.basic.repository.SpecificationOperator;
import com.zslin.basic.tools.DateTools;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.kaoqin.model.Worker;
import com.zslin.multi.dao.IStoreDao;
import com.zslin.multi.model.Store;
import com.zslin.stock.dto.GoodsDto;
import com.zslin.stock.model.OuterApply;
import com.zslin.stock.model.OuterApplyDetail;
import com.zslin.stock.model.StockGoods;
import com.zslin.stock.service.IOuterApplyDetailService;
import com.zslin.stock.service.IOuterApplyService;
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
 * 出库申请
 */
@Controller
@RequestMapping(value = "wx/stock/outerApply")
public class WeixinStockOuterApplyController {

    @Autowired
    private IStockCategoryService stockCategoryService;

    @Autowired
    private IStockGoodsService stockGoodsService;

    @Autowired
    private IOuterApplyDetailService outerApplyDetailService;

    @Autowired
    private IOuterApplyService outerApplyService;

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
        Page<OuterApply> datas = outerApplyService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request,
//                new SpecificationOperator("applyOpenid", "eq", openid),
                new SpecificationOperator("storeSn", "eq", storeSn)),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id_d")));
        model.addAttribute("datas", datas);
        return "weixin/stock/outerApply/listApply";
    }

    @GetMapping(value="list")
    public String list(Model model, String storeSn, Integer page, HttpServletRequest request) {
        Page<OuterApply> datas = outerApplyService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request,
                new SpecificationOperator("storeSn", "eq", storeSn)),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id_d")));
        model.addAttribute("datas", datas);
        return "weixin/stock/outerApply/listApply";
    }

    @PostMapping(value = "updateStatus")
    public @ResponseBody String updateStatus(String batchNo, String status, HttpServletRequest request) {
        outerApplyService.updateStatus(batchNo, status); //2-驳回申购申请,1-通过申购申请
//        stockNoticeTools.noticeApplyStatus(outerApplyService.findByBatchNo(batchNo)); //通知
        return "1";
    }

    @GetMapping(value = "show")
    public String show(Model model, String batchNo, HttpServletRequest request) {
        OuterApply ga = outerApplyService.findByBatchNo(batchNo);
        List<OuterApplyDetail> detailList = outerApplyDetailService.listByBatchNo(batchNo);
        List<GoodsDto> dtoList = stockGoodsService.listByIds(buildGoodsIds(detailList));
        model.addAttribute("detailList", detailList);
        model.addAttribute("dtoList", dtoList);
        model.addAttribute("goodsApply", ga);
        return "weixin/stock/outerApply/show";
    }

    private Integer [] buildGoodsIds(List<OuterApplyDetail> detailList) {
        Integer [] res = new Integer[detailList.size()];
        for(int i =0;i<detailList.size();i++) {
            res[i] = detailList.get(i).getGoodsId();
        }
        return res;
    }

    @PostMapping(value = "applyPost")
    public @ResponseBody String applyPost(String datas, String batchNo, String storeSn, String isCheck, HttpServletRequest request) {
        boolean isAdd = (batchNo == null || "".equals(batchNo));
        String openid = SessionTools.getOpenid(request);
        Worker worker = stockWxTools.getLoginWorker(openid);
        OuterApply ga ;
        if(isAdd) {
            ga = new OuterApply();
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

            Integer no = goodsNoTools.generateOuterApplyNo(storeSn);
            ga.setNo(no);
            ga.setBatchNo(goodsNoTools.buildApplyBatchNo(no));
            ga.setStatus("0");
        } else {
            ga = outerApplyService.findByBatchNo(batchNo);
            if("1".equals(ga.getStatus())) {
                return "redirect:/wx/stock/outerApply/show?batchNo="+batchNo;
            }
        }
        boolean isCheckOpt = (isCheck!=null && "1".equals(isCheck));
        if(isCheckOpt) {
            if(openid.equals(ga.getApplyOpenid())) {return "不能审核自己的申请";}
            ga.setCheckDatas(datas);
            ga.setCheckName(worker.getName());
            ga.setCheckOpenid(worker.getOpenid());
            ga.setCheckPhone(worker.getPhone());
        } else {
            ga.setApplyDatas(datas);
        }
        buildOuterDetail(ga, datas, isAdd, isCheckOpt);
        return "1";
    }

    private void buildOuterDetail(OuterApply ga, String datas, boolean isAdd, boolean isCheck) {
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

            OuterApplyDetail gad;
            boolean needAdd = false;
            if(!isAdd) {
                gad = outerApplyDetailService.findByBatchNoAndGoodsId(batchNo, goodsId);
                if(gad==null) {gad = new OuterApplyDetail(); needAdd = true;}
            } else {needAdd = true;gad = new OuterApplyDetail();}

            if(needAdd && amount>0) {
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
                gad.setUnit(sg.getUnit());
            }
            if(isCheck) { //如果是审核
                gad.setAmountTrue(amount);
                stockGoodsService.plusAmount(goodsId, 0-amount); //出库
            } else { //如果是初次申请或修改
                gad.setAmount(amount);
            }
            if(amount<=0 && !isCheck) { //如果数量为0，且不是审核
                outerApplyDetailService.deleteByBatchNoAndGoodsId(batchNo, goodsId);
            } else {
                if(amount>0 || outerApplyDetailService.findByBatchNoAndGoodsId(batchNo, goodsId) != null) {
                    outerApplyDetailService.save(gad);
                }
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
        if(isCheck) {
            ga.setCheckAmount(totalAmount);
            ga.setCheckRemark(sb.toString());
            ga.setStatus("1"); //如果是核对，则修改状态为1
        } else {
            ga.setApplyAmount(totalAmount);
            ga.setApplyRemark(sb.toString());
        }

        if(isAdd) {
            //TODO 通知
            stockNoticeTools.noticeNewOuterApply(ga); //新增时通知
        }

        outerApplyService.save(ga);
        if(isCheck) {
            //TODO 通知
            stockNoticeTools.noticeCheckOuterApply(batchNo);
        }
    }

    /** POST提交 *//*
    @PostMapping(value = "postCheckGoods")
    public @ResponseBody String postCheckGoods(String datas, String batchNo, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        Worker w = stockWxTools.getLoginWorker(openid);
        String [] array = datas.split("_");
        for(String str : array) {
            if(str==null || "".equals(str) || str.indexOf("-")<0) {continue;}
            Integer goodsId = Integer.parseInt(str.split("-")[0]);
            Integer amount = Integer.parseInt(str.split("-")[1]);
            outerApplyDetailService.updateAmountTrue(batchNo, goodsId, amount);
            stockGoodsService.plusAmount(goodsId, 0-amount); //出库
        }
//        OuterApply oa = outerApplyService.findByBatchNo(batchNo);
//        oa.setCheckDatas();

        outerApplyService.updateStatus(batchNo, "1"); //修改状态
        //TODO 通知
        stockNoticeTools.noticeCheckOuterApply(batchNo);
        return "1";
    }*/

    /** 收货入库 */
    @GetMapping(value = "checkGoods")
    public String checkGoods(Model model, String batchNo, HttpServletRequest request) {
        OuterApply ga = outerApplyService.findByBatchNo(batchNo);
        if(!"1".equals(ga.getStatus())) {
            return "redirect:/wx/stock/outerApply/show?batchNo="+batchNo;
        }
        List<OuterApplyDetail> detailList = outerApplyDetailService.listByBatchNo(batchNo);
        model.addAttribute("outerApply", ga);
        model.addAttribute("detailList", detailList);
        return "weixin/stock/outerApply/checkGoods";
    }

    @GetMapping(value = "modifyApply")
    public String modifyApply(Model model, String batchNo, String isCheck, HttpServletRequest request) {
        OuterApply ga = outerApplyService.findByBatchNo(batchNo);
        if(!"0".equals(ga.getStatus())) {
            return "redirect:/wx/stock/outerApply/show?batchNo="+batchNo;
        }
        model.addAttribute("apply", ga);
        List<StockGoods> list = stockGoodsService.findAll(new SimpleSpecificationBuilder<>("amount", ">", "0").generate(), SimpleSortBuilder.generateSort("locationType_a", "cateId_a"));
        /*List<StockGoods> list = stockGoodsService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request,
                new SpecificationOperator("storeSn", "eq", storeSn),
                new SpecificationOperator("amount", "gt", "0")),
                SimpleSortBuilder.generateSort("locationType_a", "cateId_a"));*/
        buildStockGoods(list, model);
        model.addAttribute("isCheck", isCheck);
        return "weixin/stock/outerApply/apply";
    }

    @GetMapping(value = "apply")
    public String apply(Model model, String storeSn, HttpServletRequest request) {
//        List<StockGoods> list = stockGoodsService.findAll(new SimpleSpecificationBuilder<>("amount", ">", "0").generate(), SimpleSortBuilder.generateSort("locationType_a", "cateId_a"));
        List<StockGoods> list = stockGoodsService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request,
                new SpecificationOperator("storeSn", "eq", storeSn),
                new SpecificationOperator("amount", "gt", 0)),
                SimpleSortBuilder.generateSort("locationType_a", "cateId_a"));
        buildStockGoods(list, model);
        return "weixin/stock/outerApply/apply";
    }

    private void buildStockGoods(List<StockGoods> list, Model model) {
        List<StockGoods> list1 = new ArrayList<>();
        List<StockGoods> list2 = new ArrayList<>();
        List<StockGoods> list3 = new ArrayList<>();
        for(StockGoods sg : list) {
            if(sg.getAmount()<=0) {continue;} //如果库存为0，则不能再出库
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
