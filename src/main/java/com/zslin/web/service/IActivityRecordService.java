package com.zslin.web.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.web.model.ActivityRecord;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/24 22:34.
 */
public interface IActivityRecordService extends BaseRepository<ActivityRecord, Integer>, JpaSpecificationExecutor<ActivityRecord> {

    @Query("UPDATE ActivityRecord a SET a.accountName=?1, a.headimgurl=?2 WHERE a.openid=?3")
    @Modifying
    @Transactional
    void update(String name, String headimg, String openid);
}
