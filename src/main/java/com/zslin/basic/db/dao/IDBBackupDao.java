package com.zslin.basic.db.dao;

import com.zslin.basic.db.model.DBBackup;
import com.zslin.basic.repository.BaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IDBBackupDao extends BaseRepository<DBBackup, Integer>, JpaSpecificationExecutor<DBBackup> {

    @Query("FROM DBBackup b WHERE b.createDay<?1")
    List<DBBackup> findByMouth(String minMouth);
}
