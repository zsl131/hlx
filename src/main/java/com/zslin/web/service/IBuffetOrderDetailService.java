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

//    @Query("SELECT COUNT(id) from BuffetOrderDetail")
    @Query("SELECT COUNT(d.id) FROM BuffetOrderDetail d , BuffetOrder o WHERE o.no=d.orderNo AND (o.status>'0')")
    Integer queryCount();

//    @Query("SELECT COUNT(id) FROM BuffetOrderDetail WHERE createDay=?1")
    @Query("SELECT COUNT(d.id) FROM BuffetOrderDetail d , BuffetOrder o WHERE o.no=d.orderNo AND d.createDay=?1 AND (o.status>'0')")
    Integer queryCount(String day);

    @Query("SELECT COUNT(d.id) FROM BuffetOrderDetail d , BuffetOrder o WHERE o.no=d.orderNo AND d.createDay=?1 and d.commodityNo=?2 AND (o.status>'0')")
    Integer queryCount(String day, String commodityNo);
}
