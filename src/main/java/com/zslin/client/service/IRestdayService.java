package com.zslin.client.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.client.model.Restday;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by zsl on 2018/1/13.
 */
public interface IRestdayService extends BaseRepository<Restday, Integer>, JpaSpecificationExecutor<Restday> {

    Restday findByYearMonth(String yearMonth);
}
