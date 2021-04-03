package com.zslin.multi.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.multi.model.WaitTable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface IWaitTableDao extends BaseRepository<WaitTable, Integer>, JpaSpecificationExecutor<WaitTable> {

    List<WaitTable> findByStoreSnAndCreateDay(String storeSn, String createDay);

    List<WaitTable> findByStoreSnAndCreateDayAndCateFlag(String storeSn, String createDay, String cateFlag);
}
