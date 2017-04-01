package com.zslin.web.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.web.model.TemplateMessage;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/20 15:45.
 */
public interface ITemplateMessageService extends BaseRepository<TemplateMessage, Integer>, JpaSpecificationExecutor<TemplateMessage> {

    TemplateMessage findByCode(String code);
}
