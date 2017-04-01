package com.zslin.web.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.web.model.Article;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/3 8:38.
 */
public interface IArticleService extends BaseRepository<Article, Integer>, JpaSpecificationExecutor<Article> {

    @Query("FROM Article a WHERE a.status='1' AND a.isFirst=1 ORDER BY a.orderNo ASC")
    List<Article> findFirst();
}
