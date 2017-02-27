package com.zslin.kaoqin.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.kaoqin.model.Worker;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/27 16:49.
 */
public interface IWorkerService extends BaseRepository<Worker, Integer>, JpaSpecificationExecutor<Worker> {
}
