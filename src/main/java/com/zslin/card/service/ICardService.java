package com.zslin.card.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.card.model.Card;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zsl on 2018/10/13.
 */
public interface ICardService extends BaseRepository<Card, Integer>, JpaSpecificationExecutor<Card> {

    Card findByNo(Integer no);

    @Query("UPDATE Card c SET c.status=?1 WHERE c.id=?2")
    @Modifying
    @Transactional
    void updateStatus(String status, Integer id);

    @Query("SELECT MAX(c.no) FROM Card c WHERE c.type=?1")
    Integer findMaxNo(String type);
}
