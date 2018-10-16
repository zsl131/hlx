package com.zslin.card.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.card.model.GrantCard;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zsl on 2018/10/13.
 */
public interface IGrantCardService extends BaseRepository<GrantCard, Integer>, JpaSpecificationExecutor<GrantCard> {

    @Query("SELECT MAX(c.cardNo) FROM GrantCard c WHERE c.cardType=?1")
    Integer findMaxNo(String type);

    @Query("SELECT MIN(c.cardNo) FROM GrantCard c WHERE c.cardType=?1 AND c.status='0'")
    Integer findMinNo(String type);

    @Query("SELECT MAX(c.orderNo) FROM GrantCard c WHERE c.createDay=?1")
    Integer findMaxOrderNo(String createDay);

    @Query("UPDATE GrantCard c SET c.status=?1 WHERE c.cardNo=?2")
    @Modifying
    @Transactional
    void updateStatus(String status, Integer no);

    @Query("UPDATE GrantCard c SET c.status=?1 WHERE c.cardNo in ?2")
    @Modifying
    @Transactional
    void updateStatus(String status, Integer... nos);

    @Query("FROM GrantCard c WHERE c.cardNo in ?1")
    List<GrantCard> findByNos(Integer... nos);

    GrantCard findByCardNo(Integer cardNo);
}
