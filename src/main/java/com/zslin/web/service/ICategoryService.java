package com.zslin.web.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.web.model.Category;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/24 22:34.
 */
public interface ICategoryService extends BaseRepository<Category, Integer>, JpaSpecificationExecutor<Category> {

    @Query("FROM Category c ORDER BY c.orderNo ASC")
    @Cacheable(cacheNames = "category-list")
    List<Category> findByOrder();

    @Override
    @CacheEvict(cacheNames = "category-list", allEntries = true)
    <S extends Category> S save(S s);

}
