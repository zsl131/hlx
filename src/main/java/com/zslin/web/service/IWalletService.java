package com.zslin.web.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.web.model.Wallet;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/25 0:20.
 */
public interface IWalletService extends BaseRepository<Wallet, Integer>, JpaSpecificationExecutor<Wallet> {

    Wallet findByOpenid(String openid);

    Wallet findByPhone(String phone);

    @Query("UPDATE Wallet w SET w.accountName=?1 WHERE w.openid=?2")
    @Modifying
    @Transactional
    void update(String name, String openid);

    @Query("UPDATE Wallet w SET w.money=w.money+?1 WHERE w.openid=?2")
    @Modifying
    @Transactional
    void plusMoney(Integer money, String openid);

    @Query("UPDATE Wallet w SET w.money=w.money+?1 WHERE w.phone=?2")
    @Modifying
    @Transactional
    void plusMoneyByPhone(Integer money, String phone);

    @Query("UPDATE Wallet w SET w.score=w.score+?1 WHERE w.openid=?2")
    @Modifying
    @Transactional
    void plusScore(Integer score, String openid);

    @Query("UPDATE Wallet w SET w.phone=?1 WHERE w.openid=?2")
    @Modifying
    @Transactional
    void modifyPhone(String phone, String openid);

    @Query("SELECT w.score FROM Wallet w WHERE w.openid=?1")
    Integer queryScore(String openid);

    @Query("SELECT w.money FROM Wallet w WHERE w.openid=?1")
    Integer queryMoney(String openid);

}
