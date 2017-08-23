package com.zslin.meituan.tools;

import com.sankuai.sjst.platform.developer.domain.RequestSysParams;
import com.zslin.meituan.model.MeituanConfig;
import com.zslin.meituan.model.MeituanShop;
import com.zslin.meituan.service.IMeituanConfigService;
import com.zslin.meituan.service.IMeituanShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by 钟述林 393156105@qq.com on 2017/7/5 15:58.
 */
@Component
public class MeituanParamsTools {

    @Autowired
    private IMeituanConfigService meituanConfigService;

    @Value("${poiId}")
    private String poiId;

    @Autowired
    private IMeituanShopService meituanShopService;

    public RequestSysParams getSysParams() {
        MeituanConfig config = meituanConfigService.loadOne();
        MeituanShop shop = meituanShopService.findByPoiId(poiId);
        RequestSysParams requestSysParams = new RequestSysParams(config.getSignKey(), shop.getToken());
        return requestSysParams;
    }
}
