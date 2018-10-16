package com.zslin.client.tools;

import com.alibaba.fastjson.JSON;
import com.zslin.card.model.*;
import com.zslin.client.dto.GrantCardStatusDto;
import com.zslin.client.dto.JsonDto;
import com.zslin.client.dto.NormalDto;
import com.zslin.client.model.ClientConfig;
import com.zslin.client.model.Orders;
import com.zslin.client.model.Restday;
import com.zslin.kaoqin.model.Worker;
import com.zslin.meituan.model.MeituanConfig;
import com.zslin.meituan.model.MeituanShop;
import com.zslin.web.dto.AdminPhoneDto;
import com.zslin.web.model.*;

import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/10 11:29.
 */
public class ClientJsonTools {

    /**
     * 构建能发送的Json数据
     * @param dataJson
     * @return
     */
    public static String buildDataJson(String dataJson) {
        StringBuffer sb = new StringBuffer();
        sb.append("{status:1,info:\"ok\",data:[");
        sb.append(dataJson);
        sb.append("]}");
        return sb.toString();
    }

    public static String buildConfig(ClientConfig cc) {
        JsonDto jd = new JsonDto("config", "update", cc.getId(), cc);
        return JSON.toJSONString(jd);
    }

    public static String buildRules(Rules r) {
        JsonDto jd = new JsonDto("rules", "update", r.getId(), r);
        return JSON.toJSONString(jd);
    }

    public static String buildWorker(String action, Worker w) {
        JsonDto jd = new JsonDto("worker", action, w.getId(), w);
        return JSON.toJSONString(jd);
    }

    public static String buildPrice(Price p) {
        JsonDto jd = new JsonDto("price", "update", p.getId(), p);
        return JSON.toJSONString(jd);
    }

    public static String buildAdminPhone(String action, Account account) {
        JsonDto jd = new JsonDto("adminPhone", action, account.getId(),
                new AdminPhoneDto(account.getPhone(), account.getName(), account.getOpenid(), account.getId()));
        return JSON.toJSONString(jd);
    }

    public static String buildOrders(Orders orders) {
        JsonDto jd = new JsonDto("orders", "update", orders.getId(), orders);
        return JSON.toJSONString(jd);
    }

    public static String buildBuffetOrders(BuffetOrder orders) {
        JsonDto jd = new JsonDto("buffetOrder", "update", orders.getId(), orders);
        return JSON.toJSONString(jd);
    }

    public static String buildMemberLevel(MemberLevel level, String action){
        JsonDto jd = new JsonDto("memberLevel", action, level.getId(), level);
        return JSON.toJSONString(jd);
    }

    public static String buildCommodity(Commodity commodity, String action) {
        JsonDto jd = new JsonDto("commodity", action, commodity.getId(), commodity);
        return JSON.toJSONString(jd);
    }

    public static String buildPrize(Prize prize, String action) {
        JsonDto jd = new JsonDto("prize", action, prize.getId(), prize);
        return JSON.toJSONString(jd);
    }

    public static String buildWalletDetail(WalletDetail wd) {
        wd.setCreateDate(null); //先移除日期
        JsonDto jd = new JsonDto("walletDetail", "add", wd.getId(), wd);
        return JSON.toJSONString(jd);
    }

    public static String buildMeituanConfig(String action, MeituanConfig config) {
        JsonDto jd = new JsonDto("meituanConfig", action, config.getId(), config);
        return JSON.toJSONString(jd);
    }

    public static String buildMeituanShop(String action, MeituanShop shop) {
        JsonDto jd = new JsonDto("meituanShop", action, shop.getId(), shop);
        return JSON.toJSONString(jd);
    }

    public static String buildDiscountTime(DiscountTime discountTime, String action) {
        JsonDto jd = new JsonDto("discountTime", action, discountTime.getId(), discountTime);
        return JSON.toJSONString(jd);
    }

    public static String buildRestday(Restday restday) {
        JsonDto jd = new JsonDto("restday", "save", restday.getId(), restday);
        return JSON.toJSONString(jd);
    }

    public static String buildUpdatePassword(String phone, String password) {
        JsonDto jd = new JsonDto("password", "update",0, new NormalDto(phone, password));
        return JSON.toJSONString(jd);
    }

    public static String buildWallet(Wallet w) {
        w.setCreateDate(null); //不要传日期
        JsonDto jd = new JsonDto("wallet", "update", w.getId(), w);
        return JSON.toJSONString(jd);
    }

    /** 卡券相关 */
    public static String buildCardApplyReason(String action, ApplyReason obj) {
        JsonDto jd = new JsonDto("cardApplyReason", action, obj.getId(), obj);
        return JSON.toJSONString(jd);
    }
    public static String buildGrantCard(String action, GrantCard obj) {
        JsonDto jd = new JsonDto("cardGrantCard", action, obj.getId(), obj);
        return JSON.toJSONString(jd);
    }
    public static String buildGrantCardStatus(String status, List<Integer> nos) {
        JsonDto jd = new JsonDto("cardGrantCardStatus", "updateStatus", 0, new GrantCardStatusDto(status, nos));
        return JSON.toJSONString(jd);
    }
    public static String buildCard(String action, Card obj) {
        JsonDto jd = new JsonDto("card", action, obj.getId(), obj);
        return JSON.toJSONString(jd);
    }
    public static String buildCardApply(CardApply obj) {
        JsonDto jd = new JsonDto("cardApply", "update", obj.getId(), obj);
        return JSON.toJSONString(jd);
    }
    public static String buildCardUnder(CardUnder obj) {
        JsonDto jd = new JsonDto("cardUnder", "add", obj.getId(), obj);
        return JSON.toJSONString(jd);
    }
    /** 以上是卡券相关 */
}
