package com.zslin.stockWx.controller;

import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.repository.SpecificationOperator;
import com.zslin.basic.tools.DateTools;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.kaoqin.model.Worker;
import com.zslin.stock.dto.GoodsDto;
import com.zslin.stock.model.Preenter;
import com.zslin.stock.model.PreenterDetail;
import com.zslin.stock.model.StockGoods;
import com.zslin.stock.service.IPreenterDetailService;
import com.zslin.stock.service.IPreenterService;
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
 * 预录入管理
 */
@Controller
@RequestMapping(value = "wx/stock/preenter")
public class WeixinStockPreenterController {

    @Autowired
    private IStockCategoryService stockCategoryService;

    @Autowired
    private IStockGoodsService stockGoodsService;

    @Autowired
    private IPreenterDetailService preenterDetailService;

    @Autowired
    private IPreenterService preenterService;

    @Autowired
    private StockWxTools stockWxTools;

    @Autowired
    private GoodsNoTools goodsNoTools;

    @Autowired
    private StockNoticeTools stockNoticeTools;

    @GetMapping(value = "listApply")
    public String listApply(Model model, Integer page, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        Page<Preenter> datas = preenterService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request,
                new SpecificationOperator("operatorOpenid", "=", openid)),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id_d")));
        model.addAttribute("datas", datas);
        return "weixin/stock/preenter/listApply";
    }

    @GetMapping(value="list")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<Preenter> datas = preenterService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id_d")));
        model.addAttribute("datas", datas);
        return "weixin/stock/preenter/listApply";
    }

    @PostMapping(value = "updateStatus")
    public @ResponseBody String updateStatus(String batchNo, String status, HttpServletRequest request) {
        preenterService.updateStatus(batchNo, status); //2-驳回申购申请,1-通过申购申请
        return "1";
    }

    @GetMapping(value = "show")
    public String show(Model model, String batchNo, HttpServletRequest request) {
        Preenter p = preenterService.findByBatchNo(batchNo);
        List<PreenterDetail> detailList = preenterDetailService.listByBatchNo(batchNo);
        List<GoodsDto> dtoList = stockGoodsService.listByIds(buildGoodsIds(detailList));
        model.addAttribute("detailList", detailList);
        model.addAttribute("dtoList", dtoList);
        model.addAttribute("preenter", p);
        return "weixin/stock/preenter/show";
    }

    private Integer [] buildGoodsIds(List<PreenterDetail> detailList) {
        Integer [] res = new Integer[detailList.size()];
        for(int i =0;i<detailList.size();i++) {
            res[i] = detailList.get(i).getGoodsId();
        }
        return res;
    }

    @PostMapping(value = "applyPost")
    public @ResponseBody String applyPost(String datas, Integer days, String batchNo, String isCheck, HttpServletRequest request) {
        boolean isAdd = (batchNo == null || "".equals(batchNo));
        String openid = SessionTools.getOpenid(request);
        Worker worker = stockWxTools.getLoginWorker(openid);
        Preenter ga ;
        if(isAdd) {
            ga = new Preenter();
            ga.setCreateDate(new Date());
            ga.setOperatorName(worker.getName());
            ga.setOperatorOpenid(openid);
            ga.setOperatorPhone(worker.getPhone());
            ga.setCreateDay(DateTools.date2Str(new Date()));
            ga.setCreateLong(System.currentTimeMillis());
            ga.setCreateTime(DateTools.date2Str(new Date(), "HH:mm:ss"));
            Integer no = goodsNoTools.generatePreenterNo();
            ga.setNo(no);
            ga.setBatchNo(goodsNoTools.buildApplyBatchNo(no));
            ga.setStatus("0");
        } else {
            ga = preenterService.findByBatchNo(batchNo);
            if(!"0".equals(ga.getStatus())) {
                return "redirect:/wx/stock/preenter/show?batchNo="+batchNo;
            }
        }
        if(days!=null && days>=0) {
            ga.setNeedDays(days);
            ga.setMaybeDay(DateTools.plusDay(days, "MM-dd EEEE"));
        }
        boolean isCheckOpt = (isCheck!=null && "1".equals(isCheck));
        if(isCheckOpt) {
            ga.setCheckDatas(datas);
            ga.setCheckName(worker.getName());
            ga.setCheckOpenid(worker.getOpenid());
            ga.setCheckPhone(worker.getPhone());
        } else {
            ga.setDatas(datas);
        }
        buildPreentDetail(ga, datas, isAdd, isCheckOpt);
        return "1";
    }

    private void buildPreentDetail(Preenter ga, String datas, boolean isAdd, boolean isCheck) {
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

            PreenterDetail gad;
            boolean needAdd = false;
            if(!isAdd) {
                gad = preenterDetailService.findByBatchNoAndGoodsId(batchNo, goodsId);
                if(gad==null) {gad = new PreenterDetail(); needAdd = true;}
            } else {needAdd = true;gad = new PreenterDetail();}

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
                stockGoodsService.plusAmount(goodsId, amount); //入库
            } else { //如果是初次申请或修改
                gad.setAmount(amount);
            }
            if(amount<=0 && !isCheck) { //如果数量为0，且不是审核
                preenterDetailService.deleteByBatchNoAndGoodsId(batchNo, goodsId);
            } else {
                if(amount>0 || preenterDetailService.findByBatchNoAndGoodsId(batchNo, goodsId) != null) {
                    preenterDetailService.save(gad);
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
            ga.setAmountTrue(totalAmount);
            ga.setCheckRemark(sb.toString());
            ga.setStatus("1"); //如果是核对，则修改状态为1
        } else {
            ga.setAmount(totalAmount);
            ga.setRemark(sb.toString());
        }

        if(isAdd) {
            //TODO 通知
            stockNoticeTools.noticeNewPreenter(ga); //新增时通知
        }

        preenterService.save(ga);
    }

    /** 收货入库 */
    @GetMapping(value = "checkGoods")
    public String checkGoods(Model model, String batchNo, HttpServletRequest request) {
        Preenter ga = preenterService.findByBatchNo(batchNo);
        if(!"0".equals(ga.getStatus())) {
            return "redirect:/wx/stock/preenter/show?batchNo="+batchNo;
        }
        List<PreenterDetail> detailList = preenterDetailService.listByBatchNo(batchNo);
        model.addAttribute("preenter", ga);
        model.addAttribute("detailList", detailList);
        return "weixin/stock/preenter/checkGoods";
    }

    @GetMapping(value = "modifyApply")
    public String modifyApply(Model model, String batchNo, String isCheck, HttpServletRequest request) {
        Preenter ga = preenterService.findByBatchNo(batchNo);
        if(!"0".equals(ga.getStatus())) {
            return "redirect:/wx/stock/preenter/show?batchNo="+batchNo;
        }
        model.addAttribute("preenter", ga);
        List<StockGoods> list = stockGoodsService.findAll(SimpleSortBuilder.generateSort("locationType_a", "cateId_a"));
        buildStockGoods(list, model);
        model.addAttribute("isCheck", isCheck);
        return "weixin/stock/outerApply/apply";
    }

    @GetMapping(value = "apply")
    public String apply(Model model, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
//        Worker w = stockWxTools.getLoginWorker(openid);
        if(stockWxTools.hasAuth(StockWxTools.BUYER, openid)) {
            List<StockGoods> list = stockGoodsService.findAll(SimpleSortBuilder.generateSort("locationType_a", "cateId_a"));
            buildStockGoods(list, model);
            return "weixin/stock/preenter/apply";
        } else {
            return "redirect:/wx/stock/preenter/list";
        }
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
