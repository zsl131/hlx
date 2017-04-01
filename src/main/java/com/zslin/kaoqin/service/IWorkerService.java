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

    @Query("SELECT w.openid FROM Worker w WHERE w.isCashier='1'")
    List<String> findCashierOpenid();

    @Query("UPDATE Worker w SET w.password=?1 WHERE w.id=?2")
    @Modifying
    @Transactional
    void updatePwd(String password, Integer id);
}
