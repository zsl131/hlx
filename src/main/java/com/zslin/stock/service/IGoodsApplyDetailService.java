package com.zslin.stock.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.stock.model.GoodsApplyDetail;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by zsl on 2018/5/21.
 */
public interface IGoodsApplyDetailService extends BaseRepository<GoodsApplyDetail, Integer>, JpaSpecificationExecutor<GoodsApplyDetail> {
}
