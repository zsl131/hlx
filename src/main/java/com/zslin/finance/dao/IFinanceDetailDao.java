package com.zslin.finance.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.finance.model.FinanceDetail;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IFinanceDetailDao extends BaseRepository<FinanceDetail, Integer>, JpaSpecificationExecutor<FinanceDetail> {

    @Query("FROM FinanceDetail f WHERE f.id IN (?1)")
    List<FinanceDetail> findByIds(Integer[] ids);

    @Query("UPDATE FinanceDetail f SET f.printFlag=?1 WHERE f.id IN (?2)")
    @Modifying
    @Transactional
    void updatePrintFlag(String flag, Integer [] ids);

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

    /** 获取待审核条数 */
    @Query("SELECT COUNT(f.id) FROM FinanceDetail f WHERE f.status = '1'")
    Integer findVerifyCount();

    /** 获取待财务审核条数 */
    @Query("SELECT COUNT(f.id) FROM FinanceDetail f WHERE f.voucherStatus = '1'")
    Integer findVoucherCount();

    /** 获取待确认收货条数 */
    @Query("SELECT COUNT(f.id) FROM FinanceDetail f WHERE f.confirmStatus = '1' AND f.confirmOpenid=?1")
    Integer findConfirmCount(String openid);

    /** 获取待审核 */
    @Query("FROM FinanceDetail f WHERE f.status = '1'")
    List<FinanceDetail> findVerify();

    /** 获取待财务审核 */
    @Query("FROM FinanceDetail f WHERE f.voucherStatus = '1'")
    List<FinanceDetail> findVoucher();

    /** 获取待确认收货 */
    @Query("FROM FinanceDetail f WHERE f.confirmStatus = '1' AND f.confirmOpenid=?1")
    List<FinanceDetail> findConfirm(String openid);
}
