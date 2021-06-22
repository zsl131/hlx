package com.zslin.business.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.business.model.BusinessStore;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IBusinessStoreDao extends BaseRepository<BusinessStore, Integer>, JpaSpecificationExecutor<BusinessStore> {
}
