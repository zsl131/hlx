package com.zslin.web.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.web.model.MemberLevel;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/7 10:50.
 */
public interface IMemberLevelService extends BaseRepository<MemberLevel, Integer>, JpaSpecificationExecutor<MemberLevel> {

}
