package com.zslin.basic.tasker;

import org.springframework.stereotype.Component;

@Component("demoTask")
public class DemoTask {

    public void taskWithParams(String param1, Integer param2) {
        System.out.println("这是有参示例任务：" + param1 + param2);
    }

    public void taskNoParams() {
        System.out.println("这是无参示例任务");
    }
}
