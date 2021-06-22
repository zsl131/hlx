package com.zslin.finance.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.finance.model.Supplier;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ISupplierDao extends BaseRepository<Supplier, Integer>, JpaSpecificationExecutor<Supplier> {
}
