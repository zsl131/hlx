package com.zslin.web.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.web.model.Rules;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/9 10:31.
 */
public interface IRulesService extends BaseRepository<Rules, Integer>, JpaSpecificationExecutor<Rules> {

    Rules findByStoreSn(String storeSn);

    @Query("FROM Rules r WHERE r.storeSn='hlx'")
    Rules findByStoreSn();

    /*@Query("FROM Rules ")
    @Cacheable(cacheNames = "publicRules")
    Rules loadOne();

    @Override
    @CacheEvict(cacheNames = "publicRules", allEntries = true)
    <S extends Rules> S save(S s);*/
}
