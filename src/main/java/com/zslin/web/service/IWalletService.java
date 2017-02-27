package com.zslin.web.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.web.model.Wallet;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/25 0:20.
 */
public interface IWalletService extends BaseRepository<Wallet, Integer>, JpaSpecificationExecutor<Wallet> {
}
