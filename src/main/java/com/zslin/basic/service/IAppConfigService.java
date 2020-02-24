package com.zslin.basic.service;

import com.zslin.basic.model.AppConfig;
import com.zslin.basic.repository.BaseRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

/**
 * Created by zsl-pc on 2016/9/7.
 */
@Service("appConfigService")
public interface IAppConfigService extends BaseRepository<AppConfig, Integer> {

    @Query("FROM AppConfig ")
    @Cacheable(cacheNames = "appConfig")
    AppConfig loadOne();

    @Override
    @CacheEvict(cacheNames = "appConfig", allEntries = true)
    <S extends AppConfig> S save(S s);
}
