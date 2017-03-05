package com.zslin.web.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.web.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

    @Query("SELECT COUNT(id) FROM Comment c WHERE c.isGood=1 AND c.foodId=?1")
    Integer queryGoodCount(Integer foodId);

    @Query("FROM Comment c WHERE c.foodId=?2 AND (c.status='1' OR c.openid=?1)")
    Page<Comment> findAll(String openid, Integer foodId, Pageable pageable);

    @Query("SELECT COUNT(id) FROM Comment c WHERE c.openid=?1")
    Integer findCount(String opeind);
}
