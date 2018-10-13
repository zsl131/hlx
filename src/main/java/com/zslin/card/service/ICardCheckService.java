package com.zslin.card.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.card.model.CardCheck;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by zsl on 2018/10/13.
 */
public interface ICardCheckService extends BaseRepository<CardCheck, Integer>, JpaSpecificationExecutor<CardCheck> {
}
