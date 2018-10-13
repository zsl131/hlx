package com.zslin.card.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.card.model.ApplyReason;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by zsl on 2018/10/13.
 */
public interface IApplyReasonService extends BaseRepository<ApplyReason, Integer>, JpaSpecificationExecutor<ApplyReason> {
}
