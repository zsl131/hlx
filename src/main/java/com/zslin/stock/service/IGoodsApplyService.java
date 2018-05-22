package com.zslin.stock.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.stock.model.GoodsApply;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by zsl on 2018/5/21.
 */
public interface IGoodsApplyService extends BaseRepository<GoodsApply, Integer>, JpaSpecificationExecutor<GoodsApply> {
}
