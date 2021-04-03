package com.zslin.finance.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.finance.model.FinanceVoucher;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface IFinanceVoucherDao extends BaseRepository<FinanceVoucher, Integer>, JpaSpecificationExecutor<FinanceVoucher> {

    List<FinanceVoucher> findByDetailId(Integer detailId);
}
