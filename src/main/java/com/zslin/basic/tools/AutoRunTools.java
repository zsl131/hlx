package com.zslin.basic.tools;

import com.zslin.basic.model.BaseTask;
import com.zslin.basic.service.IBaseTaskService;
import com.zslin.basic.tasker.CronTaskRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 加了PostConstruct 则会在启动时自动执行
 */
@Component
public class AutoRunTools {

    @Autowired
    private IBaseTaskService baseTaskService;

    @Autowired
    private CronTaskRegistrar cronTaskRegistrar;

    /**
     * 自动启动所有应该运行的任务
     */
    @PostConstruct
    public void handlerTask() {
        System.out.println("-------------------AutoRunTools 系统已启动-------------------");
        List<BaseTask> taskList = baseTaskService.listByStatus("1"); //获取所有运行的任务
        for(BaseTask task : taskList) {cronTaskRegistrar.addTask(task);}
    }
}
