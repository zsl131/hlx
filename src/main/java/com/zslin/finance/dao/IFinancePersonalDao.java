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

    @Query("UPDATE FinancePersonal f SET f.signPath=?1 WHERE f.id=?2")
    @Modifying
    @Transactional
    void updateSignPath(String path, Integer id);
}
