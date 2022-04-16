package com.zslin.business.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.business.model.BusinessDetail;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface IBusinessDetailDao extends BaseRepository<BusinessDetail, Integer>, JpaSpecificationExecutor<BusinessDetail> {

    BusinessDetail findByStoreSnAndTargetMonth(String storeSn, String targetMonth);

    @Query("SELECT d.surplusMoney FROM BusinessDetail d WHERE d.storeSn=?1 AND d.targetMonth=?2")
    Double findPreMonthMoney(String storeSn, String targetMonth);

    @Query("UPDATE BusinessDetail d SET d.status=?1 WHERE d.id=?2")
    @Modifying
    @Transactional
    void updateStatus(String status, Integer id);
}
