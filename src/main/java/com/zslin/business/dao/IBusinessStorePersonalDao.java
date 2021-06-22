package com.zslin.business.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.business.model.BusinessStorePersonal;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IBusinessStorePersonalDao extends BaseRepository<BusinessStorePersonal, Integer>, JpaSpecificationExecutor<BusinessStorePersonal> {
}
