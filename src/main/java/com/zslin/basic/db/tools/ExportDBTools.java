package com.zslin.basic.db.tools;

import com.zslin.basic.db.dto.DBConfig;
import com.zslin.basic.service.IUserService;
import com.zslin.basic.tools.NormalTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class ExportDBTools {

    /** 数据库配置信息 */
    @Autowired
    private DBConfig config;

    @Autowired
    private IUserService userService;

    public void exportDb() {
        String url = config.getUrl();
        System.out.println(url);
        String addr = getAddr(url);
        String port = getPort(url);
        String dbName = getDBName(url);
        System.out.println("------------------------------------------------------");
        System.out.println(addr);
        System.out.println(port);
        System.out.println(dbName);
        //mysqldump -h 127.0.0.1 -u root -p666 crm2 >
        StringBuffer sb = new StringBuffer();
        sb.append("mysqldump -h ").append(addr).append(" -P ").append(port).append(" -u ")
                .append(config.getUsername()).append(" -p").append(config.getPassword())
                .append(" ").append(dbName).append(" > ").append(config.getPath())
                .append(NormalTools.curDate("yyyyMMdd")).append(".sql");
        String sql = sb.toString();
//        String sql = "show tables";
        sql = sql.replace(":", "\\:");
        System.out.println(sql);
        userService.updateBySql(sql);
    }

    public void exportDbByJDBC() {
        String url = config.getUrl();
        System.out.println(url);
        String addr = getAddr(url);
        String port = getPort(url);
        String dbName = getDBName(url);
        System.out.println("------------------------------------------------------");
        System.out.println(addr);
        System.out.println(port);
        System.out.println(dbName);
        //mysqldump -h 127.0.0.1 -u root -p666 crm2 >
        StringBuffer sb = new StringBuffer();
        sb.append("mysqldump -h ").append(addr).append(" -P ").append(port).append(" -u ")
                .append(config.getUsername()).append(" -p").append(config.getPassword())
                .append(" ").append(dbName).append(" > ").append(config.getPath())
                .append(NormalTools.curDate("yyyyMMdd")).append(".sql");
        String sql = sb.toString();
//        String sql = "show tables";
//        sql = sql.replace(":", "\\:");
        System.out.println(sql);
//        userService.updateBySql(sql);

        try {
            Connection localCon = getConn(url, config.getUsername(), config.getPassword());
            Statement localState = localCon.createStatement();
//            localState.executeQuery(sql);
            localState.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    private Connection getConn(String url, String username, String password) {
        try {
            String driver = "com.mysql.cj.jdbc.Driver";
            Class.forName(driver);
            Connection con = DriverManager.getConnection(url, username, password);
            return con;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /** 通过URL获取数据库IP地址 */
    private String getAddr(String url) {
        String tempStr = url.split("//")[1];
        tempStr = tempStr.split("/")[0];
        tempStr = tempStr.split(":")[0];
        return tempStr;
    }

    /** 通过端口 */
    private String getPort(String url) {
        String tempStr = url.split("//")[1];
        tempStr = tempStr.split("/")[0];
        tempStr = tempStr.split(":")[1];
        return tempStr;
    }

    public String getDBName(String url) {
        String tempStr = url.split("//")[1];
        tempStr = tempStr.split("/")[1];
        tempStr = tempStr.split("\\?")[0];
        return tempStr;
    }
}
