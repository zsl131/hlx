package com.zslin.basic.db.dao;

import com.zslin.basic.db.model.DBBackup;
import com.zslin.basic.repository.BaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IDBBackupDao extends BaseRepository<DBBackup, Integer>, JpaSpecificationExecutor<DBBackup> {
}
