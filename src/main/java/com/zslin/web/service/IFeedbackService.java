package com.zslin.web.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.web.model.Feedback;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/24 22:35.
 */
public interface IFeedbackService extends BaseRepository<Feedback, Integer>, JpaSpecificationExecutor<Feedback> {

    @Query("UPDATE Feedback f SET f.nickname=?1, f.headimgurl=?2 WHERE f.openid=?3")
    @Modifying
    @Transactional
    void update(String name, String headimg, String openid);
}
