package com.zslin.basic.db.model;

import lombok.Data;

import javax.persistence.*;

/**
 * 数据库备份
 */
@Entity
@Table(name="base_db_backup")
@Data
public class DBBackup {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String createDay;

    private String createTime;

    private String name;
}
