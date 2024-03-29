package com.zslin.multi.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.multi.model.MoneybagDetail;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IMoneybagDetailDao extends BaseRepository<MoneybagDetail, Integer>, JpaSpecificationExecutor<MoneybagDetail> {

    @Query("SELECT COUNT(m.id) FROM MoneybagDetail m WHERE m.flag=?1 AND m.optStoreSn=?2 AND m.createDate=?3")
    Integer queryCount(String flag, String sn, String day);

    @Query("SELECT SUM(m.money) FROM MoneybagDetail m WHERE m.flag=?1 AND m.optStoreSn=?2 AND m.createDate=?3")
    Float queryMoney(String flag, String sn, String day);

    @Query("FROM MoneybagDetail m WHERE m.createDate=?1 AND m.optStoreSn=?2")
    List<MoneybagDetail> listByDay(String day, String sn, Sort sort);

    /** 获取冻结的数据详情 */
    @Query("FROM MoneybagDetail m WHERE m.freezeFlag='1'")
    List<MoneybagDetail> listAllFreezeDetail();

    @Query("FROM MoneybagDetail m WHERE m.phone=?1 ")
    List<MoneybagDetail> findByPhone(String phone, Sort sort);
}
