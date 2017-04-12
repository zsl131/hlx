package com.zslin.web.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.web.model.WalletRemoved;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by 钟述林 393156105@qq.com on 2017/4/9 19:09.
 */
public interface IWalletRemovedService extends BaseRepository<WalletRemoved, Integer>, JpaSpecificationExecutor<WalletRemoved> {
}
