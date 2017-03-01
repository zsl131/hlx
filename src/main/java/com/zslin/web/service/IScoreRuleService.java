package com.zslin.web.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.web.model.ScoreRule;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/1 14:25.
 */
public interface IScoreRuleService extends BaseRepository<ScoreRule, Integer>, JpaSpecificationExecutor<ScoreRule> {

    ScoreRule findByCode(String code);
}
