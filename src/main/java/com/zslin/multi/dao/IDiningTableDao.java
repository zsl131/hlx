package com.zslin.multi.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.multi.model.DiningTable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IDiningTableDao extends BaseRepository<DiningTable, Integer>, JpaSpecificationExecutor<DiningTable> {
}
