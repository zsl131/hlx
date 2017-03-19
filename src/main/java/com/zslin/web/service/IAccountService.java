package com.zslin.web.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.web.model.Account;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/24 22:27.
 */
public interface IAccountService extends BaseRepository<Account, Integer>, JpaSpecificationExecutor<Account> {

    @Query("UPDATE Account a SET a.status=?2 WHERE a.id=?1")
    @Modifying
    @Transactional
    void updateStatus(Integer id, String status);

    @Query("UPDATE Account a SET a.type=?2 WHERE a.id=?1")
    @Modifying
    @Transactional
    void updateType(Integer id, String type);

    @Query("UPDATE Account a SET a.phone=?1, a.bindPhone='1' WHERE a.openid=?2")
    @Modifying
    @Transactional
    void modifyPhone(String phone, String openid);

    @Query("UPDATE Account a SET a.status=?2 WHERE a.openid=?1")
    @Modifying
    @Transactional
    void updateStatus(String openid, String status);

    @Query("UPDATE Account a SET a.hasCard=?2 WHERE a.openid=?1")
    @Modifying
    @Transactional
    void updateCard(String openid, String hasCard);

    Account findByOpenid(String openid);

    @Query("UPDATE Account a SET a.followUserId=?2, a.followUserName=?3 WHERE a.id=?1")
    @Modifying
    @Transactional
    void updateFollow(Integer id, Integer followId, String followName);

    Account findByPhone(String phone);

    @Query("SELECT a.openid FROM Account a WHERE a.type=?1")
    List<String> findOpenid(String type);

    //获取用户所拉进来的用户数量
    @Query("SELECT COUNT(id) FROM Account a WHERE a.followUserId=?1")
    Integer findPullCount(Integer id);

    @Query("SELECT COUNT(id) FROM Account ")
    Integer findAllCount();

    @Query("SELECT COUNT(id) FROM Account a WHERE a.status='1'")
    Integer findFollowCount();

    @Query("SELECT COUNT(id) FROM Account a WHERE a.createDay=?1 AND a.status='1'")
    Integer findFollowCountByDay(String day);

    @Query("SELECT a.openid FROM Account a WHERE a.phone=?1")
    String findOpenidByPhone(String phone);

    @Query("SELECT a.phone FROM Account a WHERE a.openid=?1")
    String findPhoneByOpenid(String openid);
}
