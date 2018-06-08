package com.zslin.stock.tools;

import com.zslin.basic.tools.DateTools;
import com.zslin.stock.model.StockGoods;
import com.zslin.stock.service.IStockGoodsService;
import com.zslin.stockWx.tools.StockWxTools;
import com.zslin.wx.tools.EventTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by zsl on 2018/5/29.
 */
@Component
public class StockNoticeTimer {

    @Autowired
    private IStockGoodsService stockGoodsService;

    @Autowired
    private EventTools eventTools;

    @Autowired
    private StockWxTools stockWxTools;

    /** 每天早上9点通知预警物品 */
    @Scheduled(cron = "0 0 9 * * ?")
    public void noticeWarnGoods() {
        List<StockGoods> list = stockGoodsService.findByWarn();
        if(list!=null && list.size()>0) {
            String spe = "\\n";
            List<String> openids = stockWxTools.getBuyerOpenids();
            StringBuffer sb = new StringBuffer();
            for(StockGoods sg : list) {
                sb.append(sg.getName()).append("：").append(sg.getAmount()).append(sg.getUnit()).append(spe);
                stockGoodsService.updateHasWarn("1", sg.getId()); //通知过后就需要设置为已通知，防止重复通知
            }
            eventTools.eventRemind(openids, "库存预警通知", "库存预警通知", DateTools.date2Str(new Date(), "yyyyMMdd"), sb.toString(), "/wx/stock/listWarn");
        }
    }
}
