package com.zslin.multi.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.multi.model.Store;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IStoreDao extends BaseRepository<Store, Integer>, JpaSpecificationExecutor<Store> {

    Store findBySn(String sn);

    List<Store> findByStatus(String status);

    @Query("FROM Store s WHERE s.status=?1 AND find_in_set(s.sn, ?2)>0")
    List<Store> findByStatus(String status, String storeSns);

    @Query("FROM Store s WHERE s.id IN ?1 AND s.status='1'")
    List<Store> findByIds(List<Integer> ids);

    @Query("FROM Store s WHERE s.sn IN ?1")
    List<Store> findBySns(String [] sns);
}
