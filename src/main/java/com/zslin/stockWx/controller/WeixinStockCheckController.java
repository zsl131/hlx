package com.zslin.stockWx.controller;

import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.repository.SpecificationOperator;
import com.zslin.basic.tools.DateTools;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.kaoqin.model.Worker;
import com.zslin.kaoqin.service.IWorkerService;
import com.zslin.multi.dao.IStoreDao;
import com.zslin.multi.model.Store;
import com.zslin.stock.dto.GoodsDto;
import com.zslin.stock.model.StockCheck;
import com.zslin.stock.model.StockCheckDetail;
import com.zslin.stock.model.StockGoods;
import com.zslin.stock.service.IStockCheckDetailService;
import com.zslin.stock.service.IStockCheckService;
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
 * Created by zsl on 2018/5/28.
 * 库存盘点
 */
@Controller
@RequestMapping(value = "wx/stock/stockCheck")
public class WeixinStockCheckController {

    @Autowired
    private IStockGoodsService stockGoodsService;

    @Autowired
    private IStockCheckService stockCheckService;

    @Autowired
    private IStockCheckDetailService stockCheckDetailService;

    @Autowired
    private IWorkerService workerService;

    @Autowired
    private StockWxTools stockWxTools;

    @Autowired
    private GoodsNoTools goodsNoTools;

    @Autowired
    private StockNoticeTools stockNoticeTools;

    @Autowired
    private IStoreDao storeDao;

    @GetMapping(value = "list")
    public String list(Model model, String storeSn, Integer page, HttpServletRequest request) {
        Page<StockCheck> datas = stockCheckService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request,
                new SpecificationOperator("storeSn", "eq", storeSn)),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id_d")));
        model.addAttribute("datas", datas);
        return "weixin/stock/stockCheck/list";
    }

    /** 发起盘点 */
    @GetMapping(value = "addCheck")
    public String addCheck(Model model, HttpServletRequest request) {
        List<Worker> workerList = workerService.findOperators();
        model.addAttribute("workerList", workerList);
        return "weixin/stock/stockCheck/addCheck";
    }

    @PostMapping(value = "postAddCheck")
    public @ResponseBody String postAddCheck(Integer workerId, String storeSn, HttpServletRequest request) {
        Worker w = workerService.findOne(workerId);
        Worker worker = stockWxTools.getLoginWorker(SessionTools.getOpenid(request));
        StockCheck sc = new StockCheck();

        sc.setCreateDate(new Date());
        sc.setCheckName(worker.getName());
        sc.setCheckOpenid(worker.getOpenid());
        sc.setCheckPhone(worker.getPhone());
        sc.setCreateDay(DateTools.date2Str(new Date()));
        sc.setCreateLong(System.currentTimeMillis());
        sc.setCreateTime(DateTools.date2Str(new Date(), "HH:mm:ss"));
        Integer no = goodsNoTools.generateGoodsCheckNo(storeSn);
        sc.setNo(no);
        sc.setBatchNo(goodsNoTools.buildApplyBatchNo(no));
        sc.setStatus("0");
        sc.setVerifyName(w.getName());
        sc.setVerifyPhone(w.getPhone());
        sc.setVerifyOpenid(w.getOpenid());

        Store store = storeDao.findBySn(storeSn);
        if(store!=null) {
            sc.setStoreId(store.getId());
            sc.setStoreName(store.getName());
            sc.setStoreSn(storeSn);
        }

        stockCheckService.save(sc);
        stockNoticeTools.noticeStockCheckSubmit(sc.getBatchNo(), w.getOpenid()); //发起通知
        return "1";
    }

    /** 修改状态 */
    @PostMapping(value = "updateStatus")
    public @ResponseBody String updateStatus(String batchNo, String status) {
        stockCheckService.updateStatus(batchNo, status);
        if("3".equals(status)) { //完成盘点时修改库存
            updateGoodsAmount(batchNo);
        }
        return "1";
    }

    /** 修改库存数量 */
    private void updateGoodsAmount(String batchNo) {
        List<StockCheckDetail> list = stockCheckDetailService.listByBatchNo(batchNo);
        for(StockCheckDetail scd : list) {
            stockGoodsService.updateAmount(scd.getGoodsId(), scd.getAmountNow());
        }

        List<String> openids = stockWxTools.getAuditorOpenIds();
        String [] openidArray = new String[openids.size()];
        for(int i=0;i<openids.size();i++) {openidArray[i] = openids.get(i);}
        stockNoticeTools.noticeStockCheckSubmit(batchNo, openidArray);
    }

    /** 收货入库，POST提交 */
    @PostMapping(value = "postVerify")
    public @ResponseBody String postVerify(String datas, String batchNo, HttpServletRequest request) {
        String status = stockCheckService.findStatusByBatchNo(batchNo);
        if(status==null || (!"1".equals(status) && !"2".equals(status))) {
            return "redirect:/wx/stock/stockCheck/show?batchNo="+batchNo;
        }
        StockCheck sc = stockCheckService.findByBatchNo(batchNo);
        sc.setDatas(datas);
        //datas = 1-23_2-30_3-45_
        String [] array = datas.split("_");
        for(String str : array) {
            if(str==null || "".equals(str) || str.indexOf("-")<0) {continue;}
            Integer goodsId = Integer.parseInt(str.split("-")[0]);
            Integer amount = Integer.parseInt(str.split("-")[1]);
            StockCheckDetail scd = stockCheckDetailService.findByBatchNoAndGoodsId(batchNo, goodsId);
            if(scd==null) {
                scd = new StockCheckDetail();
                StockGoods sg = stockGoodsService.findOne(goodsId);
                scd.setUnit(sg.getUnit());
                scd.setNameShort(sg.getNameShort());
                scd.setCateId(sg.getCateId());
                scd.setNameFull(sg.getNameFull());
                scd.setBatchNo(batchNo);
                scd.setName(sg.getName());
                scd.setCateName(sg.getCateName());
                scd.setGoodsId(goodsId);
                scd.setLocationType(sg.getLocationType());
                scd.setNo(sg.getNo());
                scd.setAmountOld(sg.getAmount());
            }
            scd.setAmountNow(amount);
            scd.setFlag(amount==scd.getAmountOld()?0:(amount>scd.getAmountOld()?1:-1));
            stockCheckDetailService.save(scd);
//            preenterDetailService.updateAmountTrue(batchNo, goodsId, amount);
//            stockGoodsService.plusAmount(goodsId, amount);
        }
//        stockCheckService.updateStatus(batchNo, "2"); //修改状态为已提交
        sc.setStatus("2");
        stockCheckService.save(sc);
        //TODO 通知
        stockNoticeTools.noticeStockCheckSubmit(batchNo, sc.getCheckOpenid());
        return "1";
    }

    @GetMapping(value = "verify")
    public String verify(Model model, String batchNo, HttpServletRequest request) {
        StockCheck sc = stockCheckService.findByBatchNo(batchNo);
        if(!"0".equals(sc.getStatus()) && !"1".equals(sc.getStatus()) && !"2".equals(sc.getStatus())) {
            return "redirect:/wx/stock/stockCheck/show?batchNo="+batchNo;
        }

        List<StockGoods> list = stockGoodsService.findAll(SimpleSortBuilder.generateSort("locationType_a", "cateId_a", "amount_a"));
        buildStockGoods(list, model);
        model.addAttribute("stockCheck", sc);
        return "weixin/stock/stockCheck/verify";
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

    @GetMapping(value = "show")
    public String show(Model model, String batchNo, HttpServletRequest request) {
        StockCheck sc = stockCheckService.findByBatchNo(batchNo);
        List<StockCheckDetail> detailList = stockCheckDetailService.listByBatchNo(batchNo);
//        List<GoodsDto> dtoList = stockGoodsService.listByIds(buildGoodsIds(detailList));
        model.addAttribute("detailList", detailList);
//        model.addAttribute("dtoList", dtoList);
        model.addAttribute("stockCheck", sc);
        return "weixin/stock/stockCheck/show";
    }

    private Integer [] buildGoodsIds(List<StockCheckDetail> detailList) {
        Integer [] res = new Integer[detailList.size()];
        for(int i =0;i<detailList.size();i++) {
            res[i] = detailList.get(i).getGoodsId();
        }
        return res;
    }
}
