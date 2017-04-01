package com.zslin.web.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.web.model.MemberCharge;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/7 10:50.
 */
public interface IMemberChargeService extends BaseRepository<MemberCharge, Integer>, JpaSpecificationExecutor<MemberCharge> {

    @Query("SELECT MAX(orderNo) FROM MemberCharge WHERE accountId=?1")
    Integer findMaxOrderNo(Integer accountId);

    @Query("FROM MemberCharge m WHERE m.accountId=?1 AND m.balance>0 ORDER BY m.orderNo ASC")
    List<MemberCharge> find2Record(Integer accountId);

    @Query("UPDATE MemberCharge mc SET mc.status='-1' WHERE mc.id=?1 AND mc.openid=?2")
    @Modifying
    @Transactional
    void cancelCharge(Integer id, String openid);

    @Query("UPDATE MemberCharge mc SET mc.status='-1' WHERE mc.id=?1")
    @Modifying
    @Transactional
    void cancelCharge(Integer id);

    @Query("UPDATE MemberCharge mc SET mc.status='1' WHERE mc.id=?1")
    @Modifying
    @Transactional
    void verifyCharge(Integer id);

    @Query("SELECT mc.status FROM MemberCharge mc WHERE mc.id=?1")
    String findStatus(Integer id);

    @Query("SELECT COUNT(id) FROM MemberCharge mc WHERE mc.openid=?1")
    Integer findCount(String openid);
}
