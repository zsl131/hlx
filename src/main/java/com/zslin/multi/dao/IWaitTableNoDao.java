package com.zslin.multi.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.multi.model.WaitTableNo;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface IWaitTableNoDao extends BaseRepository<WaitTableNo, Integer>, JpaSpecificationExecutor<WaitTableNo> {

    @Query("FROM WaitTableNo o WHERE o.storeSn=?1 AND o.days=?2 AND o.cateFlag=?3")
    WaitTableNo findByDays(String storeSn, String days, String cateFlag);

    @Query("SELECT o.curNo FROM WaitTableNo o WHERE o.storeSn=?1 AND o.days=?2 AND o.cateFlag=?3")
    Integer findNo(String storeSn, String days, String cateFlag);

    @Query("UPDATE WaitTableNo o SET o.curNo=o.curNo+1 WHERE o.storeSn=?1 AND o.days=?2 AND o.cateFlag=?3")
    @Modifying
    @Transactional
    void updateNo(String storeSn, String days, String cateFlag);
}
