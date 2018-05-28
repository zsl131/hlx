package com.zslin.stockWx.tools;

import com.zslin.basic.tools.DateTools;
import com.zslin.stock.model.*;
import com.zslin.stock.service.IGoodsApplyDetailService;
import com.zslin.stock.service.IGoodsApplyService;
import com.zslin.stock.service.IOuterApplyDetailService;
import com.zslin.stock.service.IOuterApplyService;
import com.zslin.wx.tools.EventTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by zsl on 2018/5/25.
 * 库存管理通知工具类
 */
@Component
public class StockNoticeTools {

    private static final String NOTICE_NAME = "库存管理通知";

    @Autowired
    private StockWxTools stockWxTools;

    @Autowired
    private EventTools eventTools;

    @Autowired
    private IGoodsApplyDetailService goodsApplyDetailService;

    @Autowired
    private IGoodsApplyService goodsApplyService;

    @Autowired
    private IOuterApplyDetailService outerApplyDetailService;

    @Autowired
    private IOuterApplyService outerApplyService;

    /**
     * 收货入库通知
     * @param batchNo 批次编号
     */
    public void noticeCheckGoodsApply(String batchNo) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> openids = stockWxTools.getBuyerOpenids(); //采购员
                GoodsApply ga = goodsApplyService.findByBatchNo(batchNo);
                if(!openids.contains(ga.getApplyOpenid())) {openids.add(ga.getApplyOpenid());} //把申购人也加进去
                List<GoodsApplyDetail> list = goodsApplyDetailService.listByBatchNo(batchNo);
                StringBuffer sb = new StringBuffer();

                sb.append("申购数量：").append(ga.getAmount()).append(" \\n")
                        .append("申购人员：").append(ga.getApplyName()).append(" \\n")
                        .append("申购日期：").append(ga.getCreateDay()).append(" \\n");

                for(GoodsApplyDetail gad : list) {
                    if(gad.getAllowAmount() != gad.getAmountTrue()) { //如果数量不对
                        Integer amount = gad.getAllowAmount() - gad.getAmountTrue(); //差额
                        sb.append(gad.getName()).append(amount>0?"差":"多").append("：").append(amount).append(gad.getUnit()).append("\\n");
                    }
                }

                eventTools.eventRemind(openids, "收货入库通知", NOTICE_NAME, DateTools.date2Str(new Date(), "yyyy-MM-dd"), sb.toString(), "/wx/stock/goodsApply/show?batchNo="+batchNo);
            }
        }).start();
    }

    /**
     * 收货入库通知
     * @param p 预录入数据
     */
    public void noticeNewPreenter(Preenter p) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> openids = stockWxTools.getOpenid(StockWxTools.AUDITOR, StockWxTools.ADMIN, StockWxTools.INNER, StockWxTools.OUTER); //采购员
                StringBuffer sb = new StringBuffer();

                sb.append("物品数量：").append(p.getAmount()).append(" \\n")
                        .append("采购人员：").append(p.getOperatorName()).append(" \\n")
                        .append("采购日期：").append(p.getCreateDay()).append(" \\n")
                        .append("预计到达：").append(DateTools.plusDay(p.getNeedDays(), "MM-dd EEEE")).append("(").append(p.getNeedDays()).append(" 天)").append(" \\n");

                eventTools.eventRemind(openids, "预收货入库通知", NOTICE_NAME, DateTools.date2Str(new Date(), "yyyy-MM-dd"), sb.toString(), "/wx/stock/preenter/show?batchNo="+p.getBatchNo());
            }
        }).start();
    }

    /**
     * 出库核对通知
     * @param batchNo 批次编号
     */
    public void noticeCheckOuterApply(String batchNo) {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                List<String> openids = stockWxTools.getBuyerOpenids(); //采购员
                OuterApply ga = outerApplyService.findByBatchNo(batchNo);
                List<OuterApplyDetail> list = outerApplyDetailService.listByBatchNo(batchNo);
                StringBuffer sb = new StringBuffer();

                sb.append("出库数量：").append(ga.getApplyRemark()).append(" \\n")
                        .append("核对信息：").append(ga.getCheckRemark()).append(" \\n");

                for(OuterApplyDetail gad : list) {
                    if(gad.getAmount() != gad.getAmountTrue()) { //如果数量不对
                        Integer amount = gad.getAmount() - gad.getAmountTrue(); //差额
                        sb.append(gad.getName()).append(amount>0?"多":"差").append("：").append(amount).append(gad.getUnit()).append("\\n");
                    }
                }

                eventTools.eventRemind(ga.getApplyOpenid(), "出库核对通知", NOTICE_NAME, DateTools.date2Str(new Date(), "yyyy-MM-dd"), sb.toString(), "/wx/stock/outerApply/show?batchNo="+batchNo);
            }
        }).start();
    }

    public void noticeNewApply(GoodsApply ga) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> openids = stockWxTools.getBuyerOpenids(); //采购员
                StringBuffer sb = new StringBuffer();

                sb.append("申购数量：").append(ga.getAmount()).append(" \\n")
                        .append("申购人员：").append(ga.getApplyName()).append(" \\n")
                        .append(ga.getRemark());

                eventTools.eventRemind(openids, "申购申请通知", NOTICE_NAME, DateTools.date2Str(new Date(), "yyyy-MM-dd"), sb.toString(), "/wx/stock/goodsApply/show?batchNo="+ga.getBatchNo());
            }
        }).start();
    }

    public void noticeNewOuterApply(OuterApply ga) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> openids = stockWxTools.getAuditorOpenIds(); //核对员
                StringBuffer sb = new StringBuffer();

                sb.append("出库数量：").append(ga.getApplyAmount()).append(" \\n")
                        .append("出库人员：").append(ga.getApplyName()).append(" \\n")
                        .append(ga.getApplyRemark()).append("，请注意核对！");

                eventTools.eventRemind(openids, "出库申请通知", NOTICE_NAME, DateTools.date2Str(new Date(), "yyyy-MM-dd"), sb.toString(), "/wx/stock/outerApply/show?batchNo="+ga.getBatchNo());
            }
        }).start();
    }

    public void noticeApplyStatus(GoodsApply ga) {
        new Thread(new Runnable() {
            @Override
            public void run() {
//        List<String> openids = stockWxTools.getBuyerOpenids(); //采购员
                String openid = ga.getApplyOpenid();
                StringBuffer sb = new StringBuffer();
                String status = ga.getStatus();
                String statusStr = "申请中";
                if("-1".equals(status)) { statusStr = "被驳回";}
                else if("1".equals(status)) { statusStr = "审核通过"; }
                else if("2".equals(status)) { statusStr = "货已入库"; }

                sb.append("申购数量：").append(ga.getAmount()).append(" \\n")
                        .append("当前状态：变更为【").append(statusStr).append("】 \\n")
                        .append(ga.getRemark());

                eventTools.eventRemind(openid, "申购申请状态变更通知", NOTICE_NAME, DateTools.date2Str(new Date(), "yyyy-MM-dd"), sb.toString(), "/wx/stock/goodsApply/show?batchNo="+ga.getBatchNo());
            }
        }).start();
    }
}
