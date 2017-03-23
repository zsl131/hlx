package com.zslin.weixin.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.weixin.model.Shop;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/21 23:59.
 */
public interface IShopService extends BaseRepository<Shop, Integer>, JpaSpecificationExecutor<Shop> {

    Shop findByShopId(Integer shopId);
}
