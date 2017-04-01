package com.zslin.client.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.client.model.ClientConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/10 10:20.
 */
public interface IClientConfigService extends BaseRepository<ClientConfig, Integer>, JpaSpecificationExecutor<ClientConfig> {

    @Query("SELECT c.status FROM ClientConfig c WHERE c.token=?1")
    String queryStatus(String token);

    @Query("FROM ClientConfig ")
    @Cacheable(cacheNames = "client-one")
    ClientConfig loadOne();

    @Override
    @CacheEvict(cacheNames = "client-one", allEntries = true)
    <S extends ClientConfig> S save(S s);
}
