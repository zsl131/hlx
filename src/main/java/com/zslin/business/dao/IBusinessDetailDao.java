package com.zslin.business.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.business.model.BusinessDetail;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface IBusinessDetailDao extends BaseRepository<BusinessDetail, Integer>, JpaSpecificationExecutor<BusinessDetail> {

    BusinessDetail findByStoreSnAndTargetMonth(String storeSn, String targetMonth);

    @Query("SELECT d.surplusMoney FROM BusinessDetail d WHERE d.storeSn=?1 AND d.targetMonth=?2")
    Double findPreMonthMoney(String storeSn, String targetMonth);
}
