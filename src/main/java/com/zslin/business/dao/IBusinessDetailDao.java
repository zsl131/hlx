package com.zslin.business.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.business.model.BusinessDetail;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IBusinessDetailDao extends BaseRepository<BusinessDetail, Integer>, JpaSpecificationExecutor<BusinessDetail> {

    BusinessDetail findByStoreSnAndTargetMonth(String storeSn, String targetMonth);
}
