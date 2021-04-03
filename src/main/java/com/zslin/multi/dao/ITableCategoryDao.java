package com.zslin.multi.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.multi.model.TableCategory;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ITableCategoryDao extends BaseRepository<TableCategory, Integer>, JpaSpecificationExecutor<TableCategory> {

    List<TableCategory> findByStoreSn(String storeSn);
}
