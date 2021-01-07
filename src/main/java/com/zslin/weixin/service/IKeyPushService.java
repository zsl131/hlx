package com.zslin.weixin.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.weixin.model.KeyPush;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IKeyPushService extends BaseRepository<KeyPush, Integer>, JpaSpecificationExecutor<KeyPush> {

}
