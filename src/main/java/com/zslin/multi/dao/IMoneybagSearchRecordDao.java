package com.zslin.multi.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.multi.model.MoneybagSearchRecord;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IMoneybagSearchRecordDao extends BaseRepository<MoneybagSearchRecord, Integer>, JpaSpecificationExecutor<MoneybagSearchRecord> {
}
