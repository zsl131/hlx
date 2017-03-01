package com.zslin.web.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.web.model.Comment;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/24 22:35.
 */
public interface ICommentService extends BaseRepository<Comment, Integer>, JpaSpecificationExecutor<Comment> {

    @Query("UPDATE Comment c SET c.nickname=?1, c.headimgurl=?2 WHERE c.openid=?3")
    @Modifying
    @Transactional
    void update(String name, String headimg, String openid);
}
