package com.zslin.basic.db.tools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by zsl on 2021/5/7.
 * 定时备份数据库
 */
@Component
public class BackupTimer {

    @Autowired
    private ExportDBTools exportDBTools;

    //每天2点 自动备份数据库
    @Scheduled(cron = "0 0 2 * * ?")
    public void backupDb() {
        exportDBTools.exportDb();
    }
}
