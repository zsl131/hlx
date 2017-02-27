package com.zslin;

import com.zslin.wx.tools.QrTools;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/21 17:14.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("zsl")
public class QRTest {

    @Autowired
    private QrTools qrTools;

    @Test
    public void test() {
        qrTools.genUserQr("111");
    }
}
