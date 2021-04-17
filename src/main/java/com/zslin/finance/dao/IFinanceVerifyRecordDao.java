package com.zslin.finance.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.finance.model.FinanceVerifyRecord;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface IFinanceVerifyRecordDao extends BaseRepository<FinanceVerifyRecord, Integer>, JpaSpecificationExecutor<FinanceVerifyRecord> {

    List<FinanceVerifyRecord> findByDetailId(Integer detailId);
}
