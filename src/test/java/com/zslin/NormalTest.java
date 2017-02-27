package com.zslin;

import com.zslin.wx.tools.WxConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ClassUtils;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/24 10:35.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("zsl")
public class NormalTest {

    @Autowired
    private WxConfig wxConfig;

    @Test
    public void test() {
        System.out.println(wxConfig.getAppid()+"======"+wxConfig.getSecret()+"========="+wxConfig.getToken());
    }

    @Test
    public void test1() {
        String url = "http://www.zslin.com?id=1#zsl";
        System.out.println(url.substring(0, url.indexOf("#")));
    }

    @Test
    public void test2() {
        String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        System.out.println(path);
    }
}
