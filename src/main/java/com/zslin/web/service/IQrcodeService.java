package com.zslin.web.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.web.model.Qrcode;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/6 0:22.
 */
public interface IQrcodeService extends BaseRepository<Qrcode, Integer>, JpaSpecificationExecutor<Qrcode> {

    Qrcode findByAccountId(Integer accountId);

    Qrcode findByOpenid(String openid);

    @Query("UPDATE Qrcode q SET q.nickname=?1, q.headimg=?2 WHERE q.openid=?3")
    @Modifying
    @Transactional
    void update(String name, String headimg, String openid);

    @Query("UPDATE Qrcode q SET q.name=?1 WHERE q.openid=?2")
    @Modifying
    @Transactional
    void updateName(String name, String openid);
}
