package com.zslin.store.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.store.model.StoreFood;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IStoreFoodDao extends BaseRepository<StoreFood, Integer>, JpaSpecificationExecutor<StoreFood> {
}
