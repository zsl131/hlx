package com.zslin.web.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.web.model.BuffetOrderDetail;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/4/2 23:28.
 */
public interface IBuffetOrderDetailService extends BaseRepository<BuffetOrderDetail, Integer>, JpaSpecificationExecutor<BuffetOrderDetail> {

    @Query("FROM BuffetOrderDetail bod WHERE bod.orderNo=?1 ORDER BY bod.commodityNo ASC")
    List<BuffetOrderDetail> listByOrderNo(String orderNo);
}
