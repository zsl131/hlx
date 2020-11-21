package com.zslin.store.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.store.model.StoreFoodCategory;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IStoreFoodCategoryDao extends BaseRepository<StoreFoodCategory, Integer>, JpaSpecificationExecutor<StoreFoodCategory> {
}
