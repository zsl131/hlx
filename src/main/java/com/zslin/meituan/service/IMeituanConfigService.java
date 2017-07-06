package com.zslin.meituan.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.meituan.model.MeituanConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by 钟述林 393156105@qq.com on 2017/7/5 14:05.
 */
public interface IMeituanConfigService extends BaseRepository<MeituanConfig, Integer> {

    @Query("FROM MeituanConfig ")
    @Cacheable(cacheNames = "meituanConfig")
    MeituanConfig loadOne();

    @Override
    @CacheEvict(cacheNames = "meituanConfig", allEntries = true)
    <S extends MeituanConfig> S save(S s);
}
