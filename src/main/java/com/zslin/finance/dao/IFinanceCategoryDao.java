package com.zslin.finance.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.finance.model.FinanceCategory;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IFinanceCategoryDao extends BaseRepository<FinanceCategory, Integer>, JpaSpecificationExecutor {
}
