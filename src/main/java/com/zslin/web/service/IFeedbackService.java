package com.zslin.web.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.web.model.Feedback;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/24 22:35.
 */
public interface IFeedbackService extends BaseRepository<Feedback, Integer>, JpaSpecificationExecutor<Feedback> {
}
