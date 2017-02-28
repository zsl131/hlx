package com.zslin.kaoqin.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.kaoqin.model.DeviceAdvert;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/27 22:24.
 */
public interface IDeviceAdvertService extends BaseRepository<DeviceAdvert, Integer>, JpaSpecificationExecutor<DeviceAdvert> {

    @Query("FROM DeviceAdvert da WHERE da.status='1' ORDER BY da.orderNo ASC")
    List<DeviceAdvert> findUse();
}
