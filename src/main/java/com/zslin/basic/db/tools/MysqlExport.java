package com.zslin.basic.db.tools;

import com.zslin.basic.db.dto.MySqlInfo;
import org.zeroturnaround.zip.ZipUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MysqlExport {

    private Statement stmt;
    private String database;
    private String generatedSql = "";
    private String sqlFileName = "";
    private String zipFileName = "";
    private File generatedZipFile;
    private MySqlInfo mySqlInfo = null;


    public MysqlExport(MySqlInfo mySqlInfo) {
        this.mySqlInfo = mySqlInfo;
    }


    /**
     * 生成create语句
     *
     * @param table the table concerned
     * @return String
     * @throws SQLException exception
     */
    private String getTableInsertStatement(String table) throws SQLException {

        StringBuilder sql = new StringBuilder();
        ResultSet rs;
        boolean addIfNotExists = false;


        if (table != null && !table.isEmpty()) {
            rs = stmt.executeQuery("SHOW CREATE TABLE " + "`" + table + "`;");
            while (rs.next()) {
                String qtbl = rs.getString(1);
                String query = rs.getString(2);
                sql.append("\n\n--");
                sql.append("\n").append(MysqlUtil.SQL_START_PATTERN).append("  table dump : ").append(qtbl);
                sql.append("\n--\n\n");

                sql.append("DROP TABLE IF EXISTS `").append(table).append("`;\n");
                if (addIfNotExists) {
                    query = query.trim().replace("CREATE TABLE", "CREATE TABLE IF NOT EXISTS");
                }

                //替换longtext的编码
                query = query.replaceAll("longtext", "longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL");
                //替换字符编码
                //query = query.replaceAll("varchar(255) DEFAULT NULL", "varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL");
                query = replaceStr(query);

                query = query.replaceAll("DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT", "CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact");

                sql.append(query).append(";\n\n");
            }

            sql.append("\n\n--");
            sql.append("\n").append(MysqlUtil.SQL_END_PATTERN).append("  table dump : ").append(table);
            sql.append("\n--\n\n");
        }

        return sql.toString();
    }

    private String replaceStr(String str) {
        String [] array = str.split("\n");
        StringBuffer sb = new StringBuffer();
        for(String s : array) {
            if(s.contains("varchar") && s.contains("DEFAULT NULL") && !s.contains("utf8_general_ci")) {
                s = s.replace("DEFAULT NULL", "CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL");
            }
            sb.append(s).append("\n");
        }
        return sb.toString();
    }


    /**
     * 生成insert语句
     *
     * @param table the table to get inserts statement for
     * @return String generated SQL insert
     * @throws SQLException exception
     */
    private String getDataInsertStatement(String table) throws SQLException {

        StringBuilder sql = new StringBuilder();

        ResultSet rs = stmt.executeQuery("SELECT * FROM " + "`" + table + "`;");

        rs.last();
        int rowCount = rs.getRow();

        if (rowCount <= 0) {
            return sql.toString();
        }

        sql.append("\n--").append("\n-- Inserts of ").append(table).append("\n--\n\n");

        sql.append("\n/*!40000 ALTER TABLE `").append(table).append("` DISABLE KEYS */;\n");

        sql.append("\n--\n")
                .append(MysqlUtil.SQL_START_PATTERN).append(" table insert : ").append(table)
                .append("\n--\n");

        sql.append("INSERT INTO `").append(table).append("`(");

        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        for (int i = 0; i < columnCount; i++) {
            sql.append("`")
                    .append(metaData.getColumnName(i + 1))
                    .append("`, ");
        }

        sql.deleteCharAt(sql.length() - 1).deleteCharAt(sql.length() - 1).append(") VALUES \n");

        rs.beforeFirst();
        while (rs.next()) {
            sql.append("(");
            for (int i = 0; i < columnCount; i++) {

                int columnType = metaData.getColumnType(i + 1);
                int columnIndex = i + 1;

                if (Objects.isNull(rs.getObject(columnIndex))) {
                    sql.append("").append(rs.getObject(columnIndex)).append(", ");
                } else if (columnType == Types.INTEGER || columnType == Types.TINYINT || columnType == Types.BIT) {
                    sql.append(rs.getInt(columnIndex)).append(", ");
                } else {

                    String val = rs.getString(columnIndex);
                    val = val.replace("'", "\\'");

                    sql.append("'").append(val).append("', ");
                }
            }


            sql.deleteCharAt(sql.length() - 1).deleteCharAt(sql.length() - 1);

            if (rs.isLast()) {
                sql.append(")");
            } else {
                sql.append("),\n");
            }
        }

        sql.append(";");

        sql.append("\n--\n")
                .append(MysqlUtil.SQL_END_PATTERN).append(" table insert : ").append(table)
                .append("\n--\n");

        //enable FK constraint
        sql.append("\n/*!40000 ALTER TABLE `").append(table).append("` ENABLE KEYS */;\n");

        return sql.toString();
    }


    /**
     * 导出所有表的结构和数据
     *
     * @return String
     * @throws SQLException exception
     */
    private String exportToSql() throws SQLException {

        StringBuilder sql = new StringBuilder();
        sql.append("--");
        sql.append("\n-- Generated by 钟述林");
        sql.append("\n-- Date: ").append(new SimpleDateFormat("d-M-Y H:m:s").format(new Date()));
        sql.append("\n--");

        //these declarations are extracted from HeidiSQL
        sql.append("\n\n/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;")
                .append("\n/*!40101 SET NAMES utf8 */;")
                .append("\n/*!50503 SET NAMES utf8mb4 */;")
                .append("\n/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;")
                .append("\n/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;")
                .append("\nCREATE DATABASE /*!32312 IF NOT EXISTS*/`")
                .append(database).append("` /*!40100 DEFAULT CHARACTER SET utf8 */;")
                .append("\n\n\nUSE `").append(database).append("`;\n");


        List<String> tables = MysqlUtil.getAllTables(database, stmt);

        for (String s : tables) {
            try {
                sql.append(getTableInsertStatement(s.trim()));
                sql.append(getDataInsertStatement(s.trim()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        sql.append("\n/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;")
                .append("\n/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;")
                .append("\n/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;");

        this.generatedSql = sql.toString();
        return sql.toString();
    }

    /**
     * 从jdbcUrl中提取数据库名
     *
     * @param jdbcUrl
     * @return
     */
    public String findDBName(String jdbcUrl) {
        String dbName = "";
        int s = jdbcUrl.lastIndexOf("/") + 1;
        int e = jdbcUrl.lastIndexOf("?");
        dbName = jdbcUrl.substring(s, e);
        return dbName;
    }

    /**
     * 导出入口
     *
     * @throws IOException            exception
     * @throws SQLException           exception
     * @throws ClassNotFoundException exception
     */
    public String export() throws IOException, SQLException, ClassNotFoundException {

        //connect to the database
        database = findDBName(this.mySqlInfo.getJdbcUrl());
        String driverName = "";

        Connection connection;

        connection = MysqlUtil.connectWithURL(this.mySqlInfo.getUser(),
                this.mySqlInfo.getPassword(), this.mySqlInfo.getJdbcUrl(),
                driverName
        );

        stmt = connection.createStatement();

        //generate the final SQL
        String sql = exportToSql();

        //create a temp dir to store the exported file for processing
        String dirName = this.mySqlInfo.getExportPath();
        File file = new File(dirName);
        if (!file.exists()) {
            boolean res = file.mkdir();
            if (!res) {
                System.out.println("Unable to create temp dir: " + file.getAbsolutePath());
                throw new IOException("Unable to create temp dir: " + file.getAbsolutePath());
            }
        }

        //write the sql file out
        File sqlFolder = new File(dirName + "/sql");
        if (!sqlFolder.exists()) {
            boolean res = sqlFolder.mkdir();
            if (!res) {
                throw new IOException(": Unable to create temp dir: " + file.getAbsolutePath());
            }
        }

        sqlFileName = getSqlFilename();
        FileOutputStream outputStream = new FileOutputStream(sqlFolder + "/" + sqlFileName);
        outputStream.write(sql.getBytes());
        outputStream.close();

        //zip the file
        zipFileName = dirName + "/" + sqlFileName.replace(".sql", ".zip");
        generatedZipFile = new File(zipFileName);
        ZipUtil.pack(sqlFolder, generatedZipFile);

        //clear the generated temp files
        clearTempFiles(true);
        return zipFileName;
    }

    /**
     * 清理临时文件
     *
     * @param preserveZipFile bool
     */
    public void clearTempFiles(boolean preserveZipFile) {

        String dirName = this.mySqlInfo.getExportPath();

        //delete the temp sql file
        File sqlFile = new File(dirName + "/sql/" + sqlFileName);
        if (sqlFile.exists()) {
            boolean res = sqlFile.delete();
            System.out.println(": " + sqlFile.getAbsolutePath() + " deleted successfully? " + (res ? " TRUE " : " FALSE "));
        } else {
            System.out.println(": " + sqlFile.getAbsolutePath() + " DOES NOT EXIST while clearing Temp Files");
        }

        File sqlFolder = new File(dirName + "/sql");
        if (sqlFolder.exists()) {
            boolean res = sqlFolder.delete();
            System.out.println(": " + sqlFolder.getAbsolutePath() + " deleted successfully? " + (res ? " TRUE " : " FALSE "));
        } else {
            System.out.println(": " + sqlFolder.getAbsolutePath() + " DOES NOT EXIST while clearing Temp Files");
        }


        if (!preserveZipFile) {

            //delete the zipFile
            File zipFile = new File(zipFileName);
            if (zipFile.exists()) {
                boolean res = zipFile.delete();
                System.out.println(": " + zipFile.getAbsolutePath() + " deleted successfully? " + (res ? " TRUE " : " FALSE "));
            } else {
                System.out.println(": " + zipFile.getAbsolutePath() + " DOES NOT EXIST while clearing Temp Files");
            }

            //delete the temp folder
            File folder = new File(dirName);
            if (folder.exists()) {
                boolean res = folder.delete();
                System.out.println(": " + folder.getAbsolutePath() + " deleted successfully? " + (res ? " TRUE " : " FALSE "));
            } else {
                System.out.println(": " + folder.getAbsolutePath() + " DOES NOT EXIST while clearing Temp Files");
            }
        }

        System.out.println(": generated temp files cleared successfully");
    }

    /**
     * 生成文件名
     * sql file name.
     *
     * @return String
     */
    public String getSqlFilename() {
        return new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date()) + "_" + database + "_database_dump.sql";
    }


    /**
     * 获取备份的sql zip文件
     *
     * @return
     */
    public File getGeneratedZipFile() {
        if (generatedZipFile != null && generatedZipFile.exists()) {
            return generatedZipFile;
        }
        return null;
    }
}
