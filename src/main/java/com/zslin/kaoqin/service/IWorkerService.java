package com.zslin.kaoqin.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.kaoqin.model.Worker;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/27 16:49.
 */
public interface IWorkerService extends BaseRepository<Worker, Integer>, JpaSpecificationExecutor<Worker> {

    Worker findByPhone(String phone);

    Worker findByOpenid(String openid);

    @Query("SELECT w.openid FROM Worker w WHERE w.isCashier='1'")
    List<String> findCashierOpenid();

    @Query("SELECT openid FROM Worker WHERE operator LIKE %?1%")
    List<String> findOpenidByOperator(String str);

    @Query("UPDATE Worker w SET w.password=?1 WHERE w.id=?2")
    @Modifying
    @Transactional
    void updatePwd(String password, Integer id);

    @Query("UPDATE Worker w SET w.status=?1 WHERE w.id=?2")
    @Modifying
    @Transactional
    void updateStatus(String status, Integer id);

    /** 查询拥有库存权限的员工 */
    @Query("FROM Worker WHERE status='1' AND operator LIKE '%-%'")
    List<Worker> findOperators();

    @Query("FROM Worker WHERE canSendCard='1' AND status='1'")
    List<Worker> findCanSendCard();
}
