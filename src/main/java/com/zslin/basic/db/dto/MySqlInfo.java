package com.zslin.basic.db.dto;

import lombok.Data;

@Data
public class MySqlInfo {

    private String jdbcUrl="";
    private String user="";
    private String password="";
    private String exportPath= "dbback";

    public MySqlInfo(String jdbcUrl, String user, String password, String basePath) {
        this.jdbcUrl = jdbcUrl;
        this.user = user;
        this.password = password;
        this.exportPath = basePath + exportPath;
    }
}
