package com.zslin.card.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.card.dto.CardCheckDto;
import com.zslin.card.model.CardCheck;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by zsl on 2018/10/13.
 */
public interface ICardCheckService extends BaseRepository<CardCheck, Integer>, JpaSpecificationExecutor<CardCheck> {

    CardCheck findByCardNo(Integer cardNo);

    @Query("SELECT new com.zslin.card.dto.CardCheckDto(underName, COUNT(id), checkDay, checkMonth) FROM CardCheck WHERE underName IS NOT NULL AND checkDay=?1 GROUP BY underKey")
    List<CardCheckDto> findCheckDtoByDay(String day);

    @Query("SELECT new com.zslin.card.dto.CardCheckDto(underName, COUNT(id), checkDay, checkMonth) FROM CardCheck WHERE underName IS NOT NULL AND checkMonth=?1 GROUP BY underKey")
    List<CardCheckDto> findCheckDtoByMonth(String monty);
}
