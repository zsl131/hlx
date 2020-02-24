package com.zslin.multi.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.multi.model.Store;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IStoreDao extends BaseRepository<Store, Integer>, JpaSpecificationExecutor<Store> {

    Store findBySn(String sn);
}
