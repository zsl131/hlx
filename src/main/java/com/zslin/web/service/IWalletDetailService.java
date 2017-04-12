package com.zslin.web.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.web.model.WalletDetail;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Query("SELECT COUNT(id) FROM WalletDetail wd WHERE wd.type='2' AND wd.tranType=?1 AND wd.openid=?2")
    Integer queryCount(String tranType, String openid);

    /** 获取过期的积分 */
    @Query("FROM WalletDetail w WHERE w.type='2' AND w.flag='1' AND (w.status='0' OR w.status='1') AND w.surplus>0 AND w.createLong<=?1 ORDER BY w.accountId ASC")
    List<WalletDetail> findOverdue(Long createLong);

    @Query("FROM WalletDetail w WHERE w.type='1' AND w.reason=?1 AND w.flag=?2 ")
    WalletDetail findByShop(String orderNo, String flag);
}
