package com.zslin;

import com.zslin.basic.db.tools.ExportDBTools;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("zsl")
public class DBTest {

    @Autowired
    private ExportDBTools exportDBTools;

    @Test
    public void test01() {
        exportDBTools.exportDb();
    }
}
