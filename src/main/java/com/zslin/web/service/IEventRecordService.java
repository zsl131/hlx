package com.zslin.web.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.web.model.EventRecord;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/7 11:34.
 */
public interface IEventRecordService extends BaseRepository<EventRecord, Integer>, JpaSpecificationExecutor<EventRecord> {
}
