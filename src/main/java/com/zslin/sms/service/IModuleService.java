package com.zslin.sms.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.sms.model.Module;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/14 10:18.
 */
public interface IModuleService extends BaseRepository<Module, Integer>, JpaSpecificationExecutor<Module> {

    Module findByIid(Integer iid);

    @Query("UPDATE Module m SET m.status=?2 WHERE m.iid=?1")
    @Modifying
    @Transactional
    void updateStatus(Integer iid, String status);

    /**
     * 获取可以使用的短信模板
     * @return
     */
    @Query("FROM Module m WHERE m.status='1' AND m.iid>0")
    List<Module> findCanUse();
}
