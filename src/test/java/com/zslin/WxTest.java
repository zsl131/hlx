package com.zslin;

import com.zslin.basic.tools.ConfigTools;
import com.zslin.wx.tools.ExchangeTools;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/5 16:14.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("zsl")
public class WxTest {

    @Autowired
    private ExchangeTools exchangeTools;

    @Autowired
    private ConfigTools configTools;

    @Test
    public void test01() {
        String mediaId = "e-olCFDPbLlEA5RHqQ7sScB2sXNpBu5GkiW3SpijVBF_TKaUBjYifK81rBq6-KmV";
        exchangeTools.saveMedia(mediaId, configTools.getUploadPath()+"123");
//        System.out.println(res);
    }
}
