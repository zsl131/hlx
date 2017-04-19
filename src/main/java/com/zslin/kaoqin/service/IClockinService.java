package com.zslin.kaoqin.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.kaoqin.model.Clockin;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by 钟述林 393156105@qq.com on 2017/4/18 11:37.
 */
public interface IClockinService extends BaseRepository<Clockin, Integer>, JpaSpecificationExecutor<Clockin> {
}
