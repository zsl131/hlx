package com.zslin.finance.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.finance.model.FinanceVoucher;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IFinanceVoucherDao extends BaseRepository<FinanceVoucher, Integer>, JpaSpecificationExecutor<FinanceVoucher> {

    List<FinanceVoucher> findByTargetTypeAndDetailId(String targetType, Integer detailId);

    @Query("FROM FinanceVoucher f WHERE f.detailId IN (?1)")
    List<FinanceVoucher> findByIds(Integer [] ids);

    /** 通过MD5获取对象 */
    @Query("FROM FinanceVoucher f WHERE f.fileMd5=?1")
    FinanceVoucher findByMd5(String md5);

    List<FinanceVoucher> findByTargetToken(String targetToken);
}
