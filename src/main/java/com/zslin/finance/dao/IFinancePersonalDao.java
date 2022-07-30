package com.zslin.finance.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.finance.model.FinancePersonal;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IFinancePersonalDao extends BaseRepository<FinancePersonal, Integer>, JpaSpecificationExecutor<FinancePersonal> {

    FinancePersonal findByOpenid(String openid);

    /** 获取所有审核人员，需要把自己除外 */
    @Query("SELECT f.openid FROM FinancePersonal f WHERE f.openid!=?1 AND f.type='2'")
    List<String> findVerifyOpenid(String openid);

    @Query("SELECT f.openid FROM FinancePersonal f WHERE f.storeSns LIKE %?1% AND f.type=?2")
    List<String> findByType(String storeSn, String type);

    @Query("UPDATE FinancePersonal f SET f.signPath=?1 WHERE f.id=?2")
    @Modifying
    @Transactional
    void updateSignPath(String path, Integer id);

    @Query("UPDATE FinancePersonal f SET f.markFlag=?1 WHERE f.id=?2")
    @Modifying
    @Transactional
    void updateMarkFlag(String markFlag, Integer id);

    List<FinancePersonal> findByMarkFlag(String markFlag);

    @Query("FROM FinancePersonal f WHERE f.markFlag=?1 AND f.storeSns LIKE %?2%")
    List<FinancePersonal> findByMarkFlag(String markFlag, String storeSn);

    @Query("FROM FinancePersonal f WHERE f.markFlag=?1 AND (f.storeSn=?2 OR f.storeSn IS NULL OR f.storeSn='')")
    List<FinancePersonal> findByMarkFlagAndStoreSn(String markFlag, String storeSn);
}
