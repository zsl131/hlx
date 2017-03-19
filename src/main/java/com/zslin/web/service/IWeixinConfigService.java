package com.zslin.web.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.web.model.WeixinConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/18 16:55.
 */
public interface IWeixinConfigService extends BaseRepository<WeixinConfig, Integer>, JpaSpecificationExecutor<WeixinConfig> {

    @Query("FROM WeixinConfig ")
    @Cacheable(cacheNames = "weixinConfigCache")
    WeixinConfig loadOne();

    @Override
    @CacheEvict(cacheNames = "weixinConfigCache", allEntries = true)
    <S extends WeixinConfig> S save(S s);
}
