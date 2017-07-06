package com.zslin.meituan.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.meituan.model.MeituanShop;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by 钟述林 393156105@qq.com on 2017/7/5 14:24.
 */
public interface IMeituanShopService extends BaseRepository<MeituanShop, Integer>, JpaSpecificationExecutor<MeituanShop> {

    MeituanShop findByPoiId(String poiId);

    @Modifying
    @Transactional
    @Query("UPDATE MeituanShop set token=?2 WHERE poiId=?1")
    void updateToken(String poiId, String token);
}
