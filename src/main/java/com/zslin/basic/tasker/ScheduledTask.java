package com.zslin.basic.tasker;

import java.util.concurrent.ScheduledFuture;

public class ScheduledTask {

    public volatile ScheduledFuture<?> future;
    /**
     * 取消定时任务
     */
    public void cancel() {
        ScheduledFuture<?> future = this.future;
        if (future != null) {
            future.cancel(true);
        }
    }
}
