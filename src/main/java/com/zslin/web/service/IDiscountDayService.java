package com.zslin.web.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.web.model.DiscountDay;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by zsl on 2018/1/13.
 */
public interface IDiscountDayService extends BaseRepository<DiscountDay, Integer>, JpaSpecificationExecutor<DiscountDay> {

    DiscountDay findByYearMonth(String yearMonth);
}
