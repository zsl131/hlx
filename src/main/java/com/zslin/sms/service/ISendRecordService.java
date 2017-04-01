package com.zslin.sms.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.sms.model.SendRecord;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/14 10:19.
 */
public interface ISendRecordService extends BaseRepository<SendRecord, Integer>, JpaSpecificationExecutor<SendRecord> {
}
