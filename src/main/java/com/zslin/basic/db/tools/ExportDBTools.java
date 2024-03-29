package com.zslin.basic.db.tools;

import com.zslin.basic.db.dao.IDBBackupDao;
import com.zslin.basic.db.dto.DBConfig;
import com.zslin.basic.db.dto.MySqlInfo;
import com.zslin.basic.db.model.DBBackup;
import com.zslin.basic.qiniu.tools.QiniuTools;
import com.zslin.basic.tools.DateTools;
import com.zslin.basic.tools.NormalTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@Component
public class ExportDBTools {

    /** 数据库配置信息 */
    @Autowired
    private DBConfig config;

    @Autowired
    private QiniuTools qiniuTools;

    @Autowired
    private IDBBackupDao dbBackupDao;

    /**
     * 导出数据库并上传到七牛
     * @throws Exception
     */
    public void exportDb() {
        try {
            MySqlInfo mySqlInfo = new MySqlInfo(config.getUrl(), config.getUsername(), config.getPassword(), config.getPath());
            MysqlExport mysqlExport = new MysqlExport(mySqlInfo);
            String path = mysqlExport.export();
            File f = new File(path);
            FileInputStream fis = new FileInputStream(f);
            qiniuTools.upload(fis, f.getName());
            save(f.getName()); //保存到数据库
            f.delete(); //上传完成后，删除文件
            f.deleteOnExit();
            deleteOldData();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /** 只保留5天的数据 */
    private void deleteOldData() {
        String month = DateTools.plusDay(-5, "yyyy-MM-dd");
        List<DBBackup> list = dbBackupDao.findByMouth(month);
        for(DBBackup back: list) {
            qiniuTools.deleteFile(back.getName());
            dbBackupDao.delete(back);
        }
    }

    private void save(String name) {
        DBBackup backup = new DBBackup();
        backup.setCreateDay(NormalTools.curDate());
        backup.setCreateTime(NormalTools.curDatetime());
        backup.setName(name);
        dbBackupDao.save(backup);
    }
}
