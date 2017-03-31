package com.zslin.web.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.web.model.GamePrize;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/30 14:46.
 */
public interface IGamePrizeService extends BaseRepository<GamePrize, Integer>, JpaSpecificationExecutor<GamePrize> {

    GamePrize findByCode(String code);

    @Query("FROM GamePrize g GROUP BY g.batchNo")
    Page<GamePrize> findByBatch(Pageable pageable);
}
