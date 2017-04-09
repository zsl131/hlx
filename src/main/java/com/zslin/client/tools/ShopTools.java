package com.zslin.client.tools;

import com.zslin.web.model.BuffetOrder;
import com.zslin.web.model.WalletDetail;
import com.zslin.web.service.IWalletDetailService;
import com.zslin.wx.dbtools.MoneyTools;
import com.zslin.wx.dbtools.ScoreAdditionalDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by 钟述林 393156105@qq.com on 2017/4/6 22:25.
 * 会员消息处理工具类
 */
@Component
public class ShopTools {

    @Autowired
    private IWalletDetailService walletDetailService;

    @Autowired
    private MoneyTools moneyTools;

    public void onShopping(BuffetOrder order) {
        if("5".equals(order.getType())) { //如果是会员订单则需要做相应处理
            Integer orderType = Integer.parseInt(order.getType());
            String orderNo = order.getNo();
            if (orderType >= 0) { //表示需要扣款
                WalletDetail wd = walletDetailService.findByShop(orderNo, "-1");
                if (wd == null) {
                    moneyTools.processScoreByPhone(order.getDiscountReason(), 0 - (int) (order.getDiscountMoney() * 100), "消费扣款", orderNo,
                            new ScoreAdditionalDto("订单编号", orderNo),
                            new ScoreAdditionalDto("订单时间", order.getCreateTime()));
                }
            } else { //需要退款
                WalletDetail wd = walletDetailService.findByShop(order.getNo(), "1");
                if (wd == null) {
                    moneyTools.processScoreByPhone(order.getDiscountReason(), 0 - (int) (order.getDiscountMoney() * 100), "订单退款", orderNo,
                            new ScoreAdditionalDto("订单编号", orderNo),
                            new ScoreAdditionalDto("订单时间", order.getCreateTime()));
                }
            }
        }
    }
}
