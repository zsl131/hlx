package com.zslin.web.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.web.model.ActivityRecord;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/24 22:34.
 */
public interface IActivityRecordService extends BaseRepository<ActivityRecord, Integer>, JpaSpecificationExecutor<ActivityRecord> {
}
