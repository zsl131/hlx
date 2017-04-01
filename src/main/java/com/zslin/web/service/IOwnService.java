package com.zslin.web.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.web.model.Own;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/24 22:36.
 */
public interface IOwnService extends BaseRepository<Own, Integer>, JpaSpecificationExecutor<Own> {

    @Query("SELECT COUNT(id) FROM Own o WHERE o.openid=?1")
    Integer findCount(String openid);

    @Query("UPDATE Own o SET o.accountName=?1, o.headimg=?2 WHERE o.openid=?3")
    @Modifying
    @Transactional
    void update(String name, String headimg, String openid);
}
