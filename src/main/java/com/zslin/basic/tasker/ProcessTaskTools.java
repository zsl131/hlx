package com.zslin.basic.tasker;

import com.zslin.basic.model.BaseTask;
import com.zslin.basic.service.IBaseTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 处理任务工具类
 */
@Component("processTaskTools")
@Slf4j
public class ProcessTaskTools {

    @Autowired
    private IBaseTaskService baseTaskService;

    /**
     *
     * @param task 执行的具体任务对象
     * @param flag 执行结果标识，1-成功；0-异常
     */
    public void handler(BaseTask task, String flag) {
        log.info("执行结果：{}, 对象：{}", flag, task);
        if(BaseTask.TYPE_SINGLE.equals(task.getType())) { //如果是单次运行，则需要修改状态
            baseTaskService.updateStatus("0", task.getTaskUuid());
        }
        Integer sucCount = "1".equals(flag)?1:0;
        Integer errCount = "1".equals(flag)?0:1;
        baseTaskService.updateCount(sucCount, errCount, task.getId());
    }
}
