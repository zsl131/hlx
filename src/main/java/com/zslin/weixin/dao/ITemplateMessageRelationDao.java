package com.zslin.weixin.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.weixin.model.TemplateMessageRelation;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by 钟述林 on 2020-04-14.
 */
public interface ITemplateMessageRelationDao extends BaseRepository<TemplateMessageRelation, Integer>, JpaSpecificationExecutor<TemplateMessageRelation> {

    TemplateMessageRelation findByTemplatePinyin(String templatePinyin);

    @Query("SELECT templateId FROM TemplateMessageRelation WHERE templatePinyin=?1 ")
    String findTemplateIdByTemplatePinyin(String templatePinyin);
}
