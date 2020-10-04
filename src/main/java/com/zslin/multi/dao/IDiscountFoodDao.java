package com.zslin.multi.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.multi.model.DiscountFood;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IDiscountFoodDao extends BaseRepository<DiscountFood, Integer>, JpaSpecificationExecutor<DiscountFood> {

}
