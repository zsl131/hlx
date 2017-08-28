package com.zslin.web.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.web.model.DiscountTime;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by 钟述林 393156105@qq.com on 2017/8/28 10:18.
 */
public interface IDiscountTimeService extends BaseRepository<DiscountTime, Integer>, JpaSpecificationExecutor<DiscountTime> {

    @Query("FROM DiscountTime d WHERE d.status='1' AND d.startTime<=?1 AND d.endTime>=?1")
    DiscountTime findByTime(Integer time);
}
