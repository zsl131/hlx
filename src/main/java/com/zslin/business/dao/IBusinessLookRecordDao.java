package com.zslin.business.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.business.model.BusinessLookRecord;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IBusinessLookRecordDao extends BaseRepository<BusinessLookRecord, Long>, JpaSpecificationExecutor<BusinessLookRecord> {
}
