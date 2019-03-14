package com.zslin.web.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.web.model.DiscountConfig;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by zsl on 2019/3/12.
 */
public interface IDiscountConfigService extends BaseRepository<DiscountConfig, Integer>, JpaSpecificationExecutor<DiscountConfig> {

    @Query("FROM DiscountConfig")
    DiscountConfig loadOne();
}
