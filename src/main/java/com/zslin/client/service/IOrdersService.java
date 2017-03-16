package com.zslin.client.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.client.model.Orders;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/13 17:26.
 */
public interface IOrdersService extends BaseRepository<Orders, Integer>, JpaSpecificationExecutor<Orders> {

    Orders findByNo(String no);
}
