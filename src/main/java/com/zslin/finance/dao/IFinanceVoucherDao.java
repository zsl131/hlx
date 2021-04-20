package com.zslin.finance.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.finance.model.FinanceVoucher;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IFinanceVoucherDao extends BaseRepository<FinanceVoucher, Integer>, JpaSpecificationExecutor<FinanceVoucher> {

    List<FinanceVoucher> findByDetailId(Integer detailId);

    @Query("FROM FinanceVoucher f WHERE f.detailId IN (?1)")
    List<FinanceVoucher> findByIds(Integer [] ids);
}
