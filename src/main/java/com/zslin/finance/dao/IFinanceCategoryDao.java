package com.zslin.finance.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.finance.model.FinanceCategory;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IFinanceCategoryDao extends BaseRepository<FinanceCategory, Integer>, JpaSpecificationExecutor {

    @Query("FROM FinanceCategory f WHERE f.storeSns like %?1% AND f.name LIKE %?2%")
    List<FinanceCategory> listByName(String storeSn, String name);

    @Query("FROM FinanceCategory f WHERE f.storeSns LIKE %?1%")
    List<FinanceCategory> queryCategoryByStoreSn(String storeSn);
}
