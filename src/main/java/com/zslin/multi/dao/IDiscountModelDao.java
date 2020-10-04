package com.zslin.multi.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.multi.model.DiscountModel;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface IDiscountModelDao extends BaseRepository<DiscountModel, Integer>, JpaSpecificationExecutor<DiscountModel> {

    @Query("FROM DiscountModel d WHERE d.storeSn=?1 ")
    DiscountModel loadOne(String storeSn);
}
