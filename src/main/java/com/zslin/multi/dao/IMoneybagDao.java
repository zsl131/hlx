package com.zslin.multi.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.multi.model.Moneybag;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface IMoneybagDao extends BaseRepository<Moneybag, Integer>, JpaSpecificationExecutor<Moneybag> {

    Moneybag findByPhone(String phone);

    @Query("UPDATE Moneybag m SET m.password=?1 WHERE m.phone=?2")
    @Modifying
    @Transactional
    void updatePasswordByPhone(String password, String phone);

    @Query("UPDATE Moneybag m SET m.money=m.money+?1 WHERE m.phone=?2")
    @Modifying
    @Transactional
    void plusMoneyByPhone(Float money, String phone);

    /** 会员总金额 */
    @Query("SELECT SUM(m.money) FROM Moneybag m ")
    Float findAllMoney();

    /** 余额不为0的会员人数 */
    @Query("SELECT COUNT(m.id) FROM Moneybag m WHERE m.money>0")
    Integer findAllBag();

    @Query("UPDATE Moneybag m SET m.birthday=?1 WHERE m.phone=?2")
    @Modifying
    @Transactional
    void updateBirthday(String birthday, String phone);
}
