package com.zslin.web.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.web.model.WalletDetail;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/24 22:37.
 */
public interface IWalletDetailService extends BaseRepository<WalletDetail, Integer>, JpaSpecificationExecutor<WalletDetail> {

    @Query("UPDATE WalletDetail w SET w.accountName=?1 WHERE w.openid=?2")
    @Modifying
    @Transactional
    void update(String name, String openid);

    @Query("SELECT COUNT(id) FROM WalletDetail wd WHERE wd.type='2' AND wd.tranType=?1 AND wd.createDay=?2 AND wd.openid=?3")
    Integer queryCount(String tranType, String date, String openid);
}
