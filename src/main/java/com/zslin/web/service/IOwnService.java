package com.zslin.web.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.web.model.Own;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/24 22:36.
 */
public interface IOwnService extends BaseRepository<Own, Integer>, JpaSpecificationExecutor<Own> {
}
