package com.zslin;

import com.zslin.fire.tools.FireRequestTools;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 二维火
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles(value = "zsl")
public class FireTest {

    @Autowired
    private FireRequestTools fireRequestTools;

    @Test
    public void test01() {
//        fireRequestTools.paymentFlow();
        fireRequestTools.balance();
    }
}
