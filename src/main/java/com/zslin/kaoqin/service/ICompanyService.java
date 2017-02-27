package com.zslin.kaoqin.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.kaoqin.model.Company;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/27 0:00.
 */
public interface ICompanyService extends BaseRepository<Company, Integer>, JpaSpecificationExecutor<Company> {

    @Query("FROM Company ")
    @Cacheable(value = "company")
    Company loadOne();
}
