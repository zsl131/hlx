package com.zslin.basic.tasker;

import com.zslin.basic.model.BaseTask;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

@Slf4j
@Data
//@Component
public class SchedulingRunnable implements Runnable {

    private BaseTask task;

    /** 执行规则
     * 如： "0/10 * * * * ?"
     */
//    private String cron;

    public SchedulingRunnable(BaseTask task) {
        this.task = task;
    }

    @Override
    public void run() {
        String params = task.getParams();
        String methodName = task.getMethodName();
        String taskDesc = task.getTaskDesc();
        String beanName = task.getBeanName();

        log.info("定时任务开始执行 - desc:{}, bean：{}，方法：{}，参数：{}", taskDesc, beanName, methodName, params);
        long startTime = System.currentTimeMillis();

        String flag = "1";

        try {
            Object target = SpringContextUtils.getBean(task.getBeanName());
            Method method = null;
            if (null != params && !"".equals(params.trim())) {
                /*Class<?>[] paramCls = new Class[params.length];
                for (int i = 0; i < params.length; i++) {
                    paramCls[i] = params[i].getClass();
                }*/
                method = target.getClass().getDeclaredMethod(methodName, params.getClass());
            } else {
                method = target.getClass().getDeclaredMethod(methodName);
            }

            ReflectionUtils.makeAccessible(method);
            if (null != params && !"".equals(params.trim())) {
                method.invoke(target, params);
            } else {
                method.invoke(target);
            }
        } catch (Exception ex) {
            log.error(String.format("定时任务执行异常 - bean：%s，方法：%s，参数：%s ", beanName, methodName, params), ex);
            flag = "0";
        }

        long times = System.currentTimeMillis() - startTime;
       // log.info("定时任务执行结束 - name:{}, bean：{}，方法：{}，参数：{}，耗时：{} 毫秒", taskDesc, beanName, methodName, params, times);
        //System.out.println("----------------------------------------------------------------");

        processTask(task, flag);
    }

    private void processTask(BaseTask task, String flag) {
        try {
            Object target = SpringContextUtils.getBean("processTaskTools");
            Method method = target.getClass().getDeclaredMethod("handler", task.getClass(), flag.getClass());
            ReflectionUtils.makeAccessible(method);
            method.invoke(target, task, flag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
