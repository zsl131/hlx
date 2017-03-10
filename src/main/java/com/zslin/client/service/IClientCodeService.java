package com.zslin.client.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.client.model.ClientCode;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/6 10:31.
 */
@Service("ucService")
public interface IClientCodeService extends BaseRepository<ClientCode, Integer>, JpaSpecificationExecutor<ClientCode> {

    ClientCode findByTokenAndCode(String token, String code);

    ClientCode findByUidAndCid(Integer uid, Integer cid);
}
