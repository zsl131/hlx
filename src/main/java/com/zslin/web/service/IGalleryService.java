package com.zslin.web.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.web.model.Gallery;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/26 16:41.
 */
public interface IGalleryService extends BaseRepository<Gallery, Integer>, JpaSpecificationExecutor<Gallery> {

    @Query("FROM Gallery g WHERE g.status='1' ORDER BY g.orderNo ASC")
    @Cacheable(cacheNames = "findShow")
    List<Gallery> findShow();

    @Override
    @CacheEvict(cacheNames = "findShow", allEntries = true)
    <S extends Gallery> S save(S s);
}
