package com.zslin.client.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.client.model.Member;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/17 22:53.
 */
public interface IMemberService extends BaseRepository<Member, Integer>, JpaSpecificationExecutor<Member> {

    Member findByPhone(String phone);

    @Query("DELETE Member m WHERE m.phone=?1")
    @Modifying
    @Transactional
    void deleteByPhone(String phone);

    @Query("UPDATE Member m SET m.password=?1 WHERE m.phone=?2")
    @Modifying
    @Transactional
    void updatePassword(String password, String phone);

    @Query("UPDATE Member m SET m.surplus=m.surplus+?1 WHERE m.phone=?2")
    @Modifying
    @Transactional
    void plusMoneyByPhone(Integer money, String phone);
}
