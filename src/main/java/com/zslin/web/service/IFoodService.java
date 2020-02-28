package com.zslin.web.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.web.model.Food;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/24 22:36.
 */
public interface IFoodService extends BaseRepository<Food, Integer>, JpaSpecificationExecutor<Food> {

    @Query("UPDATE Food f SET f.goodCount=f.goodCount+1 WHERE f.id=?1")
    @Modifying
    @Transactional
    void plusGoodCount(Integer id);

    @Query("UPDATE Food f SET f.commentCount=f.commentCount+1 WHERE f.id=?1")
    @Modifying
    @Transactional
    void plusCommentCount(Integer id);

    @Query("SELECT MAX(f.snNo) FROM Food f WHERE f.storeId=?1")
    Integer maxSnNo(Integer storeId);
}
