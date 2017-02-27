package com.zslin.web.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.web.model.Activity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/24 22:34.
 */
public interface IActivityService extends BaseRepository<Activity, Integer>, JpaSpecificationExecutor<Activity> {
}
