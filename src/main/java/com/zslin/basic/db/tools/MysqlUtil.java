package com.zslin.basic.db.tools;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MysqlUtil {
    static final String SQL_START_PATTERN = "-- start";
    static final String SQL_END_PATTERN = "-- end";


    /**
     * 通过jdbcurl连接数据库
     * @param username database username
     * @param password database password
     * @param jdbcURL the user supplied JDBC URL. It's used as is. So ensure you supply the right parameters
     * @param driverName the user supplied mysql connector driver class name
     * @return Connection
     * @throws ClassNotFoundException exception
     * @throws SQLException exception
     */
    static Connection connectWithURL(String username, String password, String jdbcURL, String driverName) throws ClassNotFoundException, SQLException {
        String driver = (Objects.isNull(driverName) || driverName.isEmpty()) ? "com.mysql.cj.jdbc.Driver" : driverName;
        return doConnect(driver, jdbcURL, username, password);
    }

    /**
     * 连接数据库
     * @param driver the class name for the mysql driver to use
     * @param url the url of the database
     * @param username database username
     * @param password database password
     * @return Connection
     * @throws SQLException exception
     * @throws ClassNotFoundException exception
     */
    private static Connection doConnect(String driver, String url, String username, String password) throws SQLException, ClassNotFoundException {
        Class.forName(driver);
        Connection connection = DriverManager.getConnection(url, username, password);
        System.out.println("DB Connected Successfully");
        return  connection;
    }


    /**
     * 获取所有表
     * @param database the database name
     * @param stmt Statement object
     * @return List\<String\>
     * @throws SQLException exception
     */
    static List<String> getAllTables(String database, Statement stmt) throws SQLException {
        List<String> table = new ArrayList<>();
        ResultSet rs;
        rs = stmt.executeQuery("SHOW TABLE STATUS FROM `" + database + "`;");
        while ( rs.next() ) {
            table.add(rs.getString("Name"));
        }
        return table;
    }
}
