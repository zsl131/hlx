package com.zslin.web.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.web.model.Goods;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/8/29 16:53.
 */
public interface IGoodsService extends BaseRepository<Goods, Integer>, JpaSpecificationExecutor<Goods> {

    @Query("FROM Goods g WHERE g.spell=?1")
    List<Goods> listBySpell(String spell);

    @Query("FROM Goods g WHERE g.firstSpell=?1")
    List<Goods> listByFirstSpell(String firstSpell);
}
