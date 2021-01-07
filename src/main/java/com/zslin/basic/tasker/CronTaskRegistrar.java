package com.zslin.basic.tasker;

import com.zslin.basic.model.BaseTask;
import com.zslin.basic.tools.NormalTools;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CronTaskRegistrar implements DisposableBean {

    ///之前这里初始化16->private final Map<Runnable, ScheduledTask> scheduledTasks = new ConcurrentHashMap<>(16);
//    private final Map<Runnable, ScheduledTask> scheduledTasks = new ConcurrentHashMap<>();
//    private final Map<String, ScheduledTask> scheduledTasks = new HashMap<>();
    private final Map<String, TaskDto> scheduledTasks = new HashMap<>();
//    private final Map<String, BaseTask> scheduledTasks = new HashMap<>();

    @Autowired
    private TaskScheduler taskScheduler;

    public TaskScheduler getScheduler() {
        return this.taskScheduler;
    }

    public void show() {
        //System.out.println("----------size: "+ this.scheduledTasks.size());
        Set<String> set = this.scheduledTasks.keySet();
        for(String s : set) {
            //System.out.println("--------->" + s);
            System.out.println(this.scheduledTasks.remove(s));
        }
    }

    public Map<String, TaskDto> list() {
        return this.scheduledTasks;
    }

    public List<BaseTask> listTask() {
        List<BaseTask> res = new ArrayList<>();
        for(String key:this.scheduledTasks.keySet()) {
            TaskDto dto = this.scheduledTasks.get(key);
            res.add(dto.getRunner());
        }
        return res;
    }

    public void addTask(BaseTask task) {
        String taskUuid = task.getTaskUuid();
        removeByUuid(taskUuid); //先删除，再新增
        this.scheduledTasks.put(taskUuid, new TaskDto(task, scheduleCronTask(task)));
    }

    /** 通过名称获取对象 */
    public TaskDto findByUuid(String taskUuid) {
        TaskDto dto = this.scheduledTasks.get(taskUuid);
        return dto;
    }

    public void removeByUuid(String taskUuid) {
//        ScheduledTask scheduledTask = this.scheduledTasks.remove(taskName);
        TaskDto dto = this.scheduledTasks.remove(taskUuid);
        //System.out.println("删除--------->"+scheduledTask);
        if (dto != null) {
            ScheduledTask scheduledTask = dto.getTask();
            scheduledTask.cancel();
        }
    }

    private ScheduledTask scheduleCronTask(BaseTask task) {
        ScheduledTask scheduledTask = new ScheduledTask();

        SchedulingRunnable runner = new SchedulingRunnable(task);

        String type = task.getType();
        Date startTime = NormalTools.getDate(task.getStartTime(), "yyyy-MM-dd HH:mm:ss");
        if(type.equals(BaseTask.TYPE_SINGLE)) { //单次
            scheduledTask.future = this.taskScheduler.schedule(runner, startTime==null?(new Date()):startTime);
        } else if(type.equals(BaseTask.TYPE_LOOP)) { //循环
            Long period = task.getPeriod() * 1000;
            if("1".equals(task.getIsWait())) { //等待上次任务完成后执行
                if(startTime==null) {
                    scheduledTask.future = this.taskScheduler.scheduleWithFixedDelay(runner, period);
                } else {
                    scheduledTask.future = this.taskScheduler.scheduleWithFixedDelay(runner, startTime, period);
                }
            } else { //间隔时长后执行新任务，不管上一次是否执行完
                if(startTime==null) {
                    scheduledTask.future = this.taskScheduler.scheduleAtFixedRate(runner, period);
                } else {
                    scheduledTask.future = this.taskScheduler.scheduleAtFixedRate(runner, startTime, period);
                }
            }
        } else if(type.equals(BaseTask.TYPE_CRON)) { //CRON规则
            CronTask cronTask = new CronTask(runner, task.getCron());
            scheduledTask.future = this.taskScheduler.schedule(runner, cronTask.getTrigger());
        }
        return scheduledTask;
    }

    @Override
    public void destroy() {
        for (TaskDto dto : this.scheduledTasks.values()) {
            ScheduledTask task = dto.getTask();
            task.cancel();
        }
        this.scheduledTasks.clear();
    }
}
