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
}
