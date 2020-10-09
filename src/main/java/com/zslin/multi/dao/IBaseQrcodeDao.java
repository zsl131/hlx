package com.zslin.multi.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.multi.model.BaseQrcode;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IBaseQrcodeDao extends BaseRepository<BaseQrcode, Integer>, JpaSpecificationExecutor<BaseQrcode> {
}
