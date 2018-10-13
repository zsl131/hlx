package com.zslin.card.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.card.model.CardUnder;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by zsl on 2018/10/13.
 */
public interface ICardUnderService extends BaseRepository<CardUnder, Integer>, JpaSpecificationExecutor<CardUnder> {
}
