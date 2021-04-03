package com.zslin.finance.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.finance.model.FinanceDetail;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface IFinanceDetailDao extends BaseRepository<FinanceDetail, Integer>, JpaSpecificationExecutor<FinanceDetail> {

    /** 只有status为0或3时才能取消，即状态为申请或驳回状态 */
    @Query("UPDATE FinanceDetail f SET f.status='-1' WHERE f.status IN ('0', '3') AND f.id=?1")
    @Modifying
    @Transactional
    void cancel(Integer id);

    /** 获取未完成的申请 */
    @Query("FROM FinanceDetail f WHERE f.userOpenid=?1 AND f.status='0'")
    FinanceDetail findOne(String openid);

    @Query("UPDATE FinanceDetail f SET f.status=?1 WHERE f.id=?2")
    @Modifying
    @Transactional
    void updateStatus(String status, Integer id);
}
