package com.zslin.web.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.web.model.WxMenu;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/26 0:06.
 */
public interface IWxMenuService extends BaseRepository<WxMenu, Integer>, JpaSpecificationExecutor<WxMenu> {

    @Query("FROM WxMenu m WHERE m.pid IS NULL OR m.pid<=0 ORDER BY m.orderNo ASC")
    List<WxMenu> findParent();

    @Query("FROM WxMenu m WHERE m.pid=?1 ORDER BY m.orderNo ASC")
    List<WxMenu> findByPid(Integer pid);
}
