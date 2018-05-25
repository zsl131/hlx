package com.zslin.stockWx.controller;

import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.repository.SimpleSpecification;
import com.zslin.basic.repository.SpecificationOperator;
import com.zslin.basic.tools.DateTools;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.kaoqin.model.Worker;
import com.zslin.stock.dto.GoodsDto;
import com.zslin.stock.model.GoodsApply;
import com.zslin.stock.model.GoodsApplyDetail;
import com.zslin.stock.model.StockGoods;
import com.zslin.stock.service.IGoodsApplyDetailService;
import com.zslin.stock.service.IGoodsApplyService;
import com.zslin.stock.service.IStockCategoryService;
import com.zslin.stock.service.IStockGoodsService;
import com.zslin.stock.tools.GoodsNoTools;
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
 * 物品申购
 */
@Controller
@RequestMapping(value = "wx/stock/goodsApply")
public class WeixinStockGoodsApplyController {

    @Autowired
    private IStockCategoryService stockCategoryService;

    @Autowired
    private IStockGoodsService stockGoodsService;

    @Autowired
    private IGoodsApplyDetailService goodsApplyDetailService;

    @Autowired
    private IGoodsApplyService goodsApplyService;

    @Autowired
    private StockWxTools stockWxTools;

    @Autowired
    private GoodsNoTools goodsNoTools;

    @GetMapping(value = "listApply")
    public String listApply(Model model, Integer page, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        Page<GoodsApply> datas = goodsApplyService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request,
                new SpecificationOperator("applyOpenid", "=", openid)),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id_d")));
        model.addAttribute("datas", datas);
        return "weixin/stock/goodsApply/listApply";
    }

    @GetMapping(value="list")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<GoodsApply> datas = goodsApplyService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id_d")));
        model.addAttribute("datas", datas);
        return "weixin/stock/goodsApply/listApply";
    }

    /** 驳回申购 */
    @PostMapping(value = "updateStatus")
    public @ResponseBody String updateStatus(String batchNo, String status, HttpServletRequest request) {
        goodsApplyService.updateStatus(batchNo, status); //2-驳回申购申请,1-通过申购申请
        return "1";
    }

    @GetMapping(value = "show")
    public String show(Model model, String batchNo, HttpServletRequest request) {
        GoodsApply ga = goodsApplyService.findByBatchNo(batchNo);
        List<GoodsApplyDetail> detailList = goodsApplyDetailService.listByBatchNo(batchNo);
        List<GoodsDto> dtoList = stockGoodsService.listByIds(buildGoodsIds(detailList));
        model.addAttribute("detailList", detailList);
        model.addAttribute("dtoList", dtoList);
        model.addAttribute("goodsApply", ga);
        return "weixin/stock/goodsApply/show";
    }

    private Integer [] buildGoodsIds(List<GoodsApplyDetail> detailList) {
        Integer [] res = new Integer[detailList.size()];
        for(int i =0;i<detailList.size();i++) {
            res[i] = detailList.get(i).getGoodsId();
        }
        return res;
    }

    @PostMapping(value = "applyPost")
    public @ResponseBody String applyPost(String datas, String batchNo, String isVerify, HttpServletRequest request) {
        boolean isAdd = (batchNo == null || "".equals(batchNo));
        String openid = SessionTools.getOpenid(request);
        Worker worker = stockWxTools.getLoginWorker(openid);
        GoodsApply ga ;
        if(isAdd) {
            ga = new GoodsApply();
            ga.setCreateDate(new Date());
            ga.setApplyName(worker.getName());
            ga.setApplyOpenid(openid);
            ga.setApplyPhone(worker.getPhone());
            ga.setCreateDay(DateTools.date2Str(new Date()));
            ga.setCreateLong(System.currentTimeMillis());
            ga.setCreateTime(DateTools.date2Str(new Date(), "HH:mm:ss"));
            Integer no = goodsNoTools.generateApplyNo();
            ga.setNo(no);
            ga.setBatchNo(goodsNoTools.buildApplyBatchNo(no));
            ga.setStatus("0");
        } else {
            ga = goodsApplyService.findByBatchNo(batchNo);
        }
        ga.setDatas(datas);
        buildApplyDetail(ga, datas, isAdd, (isVerify!=null && "1".equals(isVerify)));
        return "1";
    }

    private void buildApplyDetail(GoodsApply ga, String datas, boolean isAdd, boolean isVerify) {
        String batchNo = ga.getBatchNo();
        StringBuffer sb = new StringBuffer();
        Integer totalAmount = 0;
        Integer amount1 = 0, amount2 = 0, amount3 = 0;
        String [] locationArray = datas.split("-_-");
        /*for(Integer i = 0; i<locationArray.length; i++) {
            System.out.println("====="+i+"===="+locationArray[i]);
        }*/
        String data1 = locationArray[2];
        String data2 = locationArray[4];
        String data3 = locationArray.length>=7?locationArray[6]:"";

        String [] array1 = data1.split("-");
        String [] array2 = data2.split("-");
        String [] array3 = data3.split("-");
        for(Integer i=0;i<array1.length; i++) {
            if(array1[i]==null || "".equals(array1[i]) || array1[i].indexOf("_")<=0) {continue;}
            Integer goodsId = Integer.parseInt(array1[i].split("_")[0]);
            Integer amount = Integer.parseInt(array1[i].split("_")[1]);
            GoodsApplyDetail gad;
            boolean needAdd = false;
            if(!isAdd) {
                gad = goodsApplyDetailService.findByBatchNoAndGoodsId(batchNo, goodsId);
                if(gad==null) {gad = new GoodsApplyDetail(); needAdd = true;}
            } else {needAdd = true;gad = new GoodsApplyDetail();}

            if(needAdd && amount>0) {
                StockGoods sg = stockGoodsService.findOne(goodsId);
                gad.setStatus("0");
                gad.setGoodsId(goodsId);
                gad.setBatchNo(ga.getBatchNo());
                /*gad.setAllowAmount(amount);
                gad.setAmount(amount);
                gad.setAmountTrue(amount);*/
                gad.setCateId(sg.getCateId());
                gad.setCateName(sg.getCateName());
                gad.setLocationType(sg.getLocationType());
                gad.setName(sg.getName());
                gad.setNameFull(sg.getNameFull());
                gad.setNameShort(sg.getNameShort());
                gad.setUnit(sg.getUnit());
            } /*else {
                gad.setAllowAmount(amount);
                gad.setAmount(amount);
                gad.setAmountTrue(amount);
            }*/
            if(isVerify) { //如果是审核
                gad.setAllowAmount(amount);
            } else { //如果是初次申请或修改
                gad.setAllowAmount(amount);
                gad.setAmount(amount);
                gad.setAmountTrue(amount);
            }
            if(amount<=0 && !isVerify) { //如果数量为0，且不是审核
                goodsApplyDetailService.deleteByBatchNoAndGoodsId(batchNo, goodsId);
            } else {
                goodsApplyDetailService.save(gad);
            }
            totalAmount += amount;
            amount1 += amount;
//            goodsApplyDetailService.save(gad);
        }

        for(Integer i=0;i<array2.length; i++) {
            if(array2[i]==null || "".equals(array2[i]) || array2[i].indexOf("_")<=0) {continue;}
            Integer goodsId = Integer.parseInt(array2[i].split("_")[0]);
            Integer amount = Integer.parseInt(array2[i].split("_")[1]);
            GoodsApplyDetail gad;
            boolean needAdd = false;
            if(!isAdd) {
                gad = goodsApplyDetailService.findByBatchNoAndGoodsId(batchNo, goodsId);
                if(gad==null) {gad = new GoodsApplyDetail(); needAdd = true;}
            } else {needAdd = true;gad = new GoodsApplyDetail();}

            if(needAdd && amount>0) {
                StockGoods sg = stockGoodsService.findOne(goodsId);
                gad.setStatus("0");
                gad.setGoodsId(goodsId);
                gad.setBatchNo(ga.getBatchNo());
                /*gad.setAllowAmount(amount);
                gad.setAmount(amount);
                gad.setAmountTrue(amount);*/
                gad.setCateId(sg.getCateId());
                gad.setCateName(sg.getCateName());
                gad.setLocationType(sg.getLocationType());
                gad.setName(sg.getName());
                gad.setNameFull(sg.getNameFull());
                gad.setNameShort(sg.getNameShort());
                gad.setUnit(sg.getUnit());
            } /*else {
                gad.setAllowAmount(amount);
                gad.setAmount(amount);
                gad.setAmountTrue(amount);
            }*/
            if(isVerify) { //如果是审核
                gad.setAllowAmount(amount);
            } else { //如果是初次申请或修改
                gad.setAllowAmount(amount);
                gad.setAmount(amount);
                gad.setAmountTrue(amount);
            }
            if(amount<=0 && !isVerify) { //如果数量为0，且不是审核
                goodsApplyDetailService.deleteByBatchNoAndGoodsId(batchNo, goodsId);
            } else {
                goodsApplyDetailService.save(gad);
            }
            totalAmount += amount;
            amount2 += amount;
//            goodsApplyDetailService.save(gad);
        }

        for(Integer i=0;i<array3.length; i++) {
            if(array3[i]==null || "".equals(array3[i]) || array3[i].indexOf("_")<=0) {continue;}
            Integer goodsId = Integer.parseInt(array3[i].split("_")[0]);
            Integer amount = Integer.parseInt(array3[i].split("_")[1]);
            GoodsApplyDetail gad;
            boolean needAdd = false;
            if(!isAdd) {
                gad = goodsApplyDetailService.findByBatchNoAndGoodsId(batchNo, goodsId);
                if(gad==null) {gad = new GoodsApplyDetail(); needAdd = true;}
            } else {needAdd = true;gad = new GoodsApplyDetail();}

            if(needAdd && amount>0) {
                StockGoods sg = stockGoodsService.findOne(goodsId);
                gad.setStatus("0");
                gad.setGoodsId(goodsId);
                gad.setBatchNo(ga.getBatchNo());
                /*gad.setAllowAmount(amount);
                gad.setAmount(amount);
                gad.setAmountTrue(amount);*/
                gad.setCateId(sg.getCateId());
                gad.setCateName(sg.getCateName());
                gad.setLocationType(sg.getLocationType());
                gad.setName(sg.getName());
                gad.setNameFull(sg.getNameFull());
                gad.setNameShort(sg.getNameShort());
                gad.setUnit(sg.getUnit());
            } /*else {
                gad.setAllowAmount(amount);
                gad.setAmount(amount);
                gad.setAmountTrue(amount);
            }*/
            if(isVerify) { //如果是审核
                gad.setAllowAmount(amount);
            } else { //如果是初次申请或修改
//                gad.setAllowAmount(amount);
                gad.setAmount(amount);
//                gad.setAmountTrue(amount);
            }
            if(amount<=0 && !isVerify) { //如果数量为0，且不是审核
                goodsApplyDetailService.deleteByBatchNoAndGoodsId(batchNo, goodsId);
            } else {
                goodsApplyDetailService.save(gad);
            }
            totalAmount += amount;
            amount3 += amount;
//            goodsApplyDetailService.save(gad);
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
        ga.setAmount(totalAmount);
        ga.setRemark(sb.toString());
        /*if(isVerify) {
            ga.setStatus("1");
        }*/
        goodsApplyService.save(ga);
    }

    @GetMapping(value = "modifyApply")
    public String modifyApply(Model model, String batchNo, String isVerify, HttpServletRequest request) {
        GoodsApply ga = goodsApplyService.findByBatchNo(batchNo);
        model.addAttribute("apply", ga);
        List<StockGoods> list = stockGoodsService.findAll(SimpleSortBuilder.generateSort("locationType_a", "cateId_a", "amount_a"));
        buildStockGoods(list, model);
        model.addAttribute("isVerify", isVerify);
        return "weixin/stock/goodsApply/apply";
    }

    @GetMapping(value = "apply")
    public String apply(Model model, HttpServletRequest request) {
        List<StockGoods> list = stockGoodsService.findAll(SimpleSortBuilder.generateSort("locationType_a", "cateId_a", "amount_a"));
        buildStockGoods(list, model);
        return "weixin/stock/goodsApply/apply";
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
