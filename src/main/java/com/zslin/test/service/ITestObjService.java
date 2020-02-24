package com.zslin.test.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.test.TestObj;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

@Service("testObjService")
public interface ITestObjService extends BaseRepository<TestObj, Integer>, JpaSpecificationExecutor<TestObj> {

    @Query("FROM TestObj ")
    TestObj loadOne();
}
