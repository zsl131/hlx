package com.zslin.basic.db.tools;

import com.zslin.basic.db.dto.DBConfig;
import com.zslin.basic.db.dto.MySqlInfo;
import com.zslin.basic.qiniu.tools.QiniuTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;

@Component
public class ExportDBTools {

    /** 数据库配置信息 */
    @Autowired
    private DBConfig config;

    @Autowired
    private QiniuTools qiniuTools;

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
            f.delete(); //上传完成后，删除文件
            f.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
