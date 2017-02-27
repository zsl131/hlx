package com.zslin;

import com.zslin.cache.CacheTools;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/24 8:59.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("zsl")
public class CacheTest {

    @Autowired
    private CacheTools cacheTools;

    @Test
    public void setKeys() {
        cacheTools.putKey("aaa", 6, -1);
    }

    @Test
    public void getKey() {
        String name = (String) cacheTools.getKey("aaa");
        System.out.println("========"+name);
    }

    @Test
    public void getAndSet() {
        Integer age = (Integer) cacheTools.setAndGet("aaa", 9, -1);
        System.out.println(age);
        cacheTools.insc("aaa", -2d);
    }

    @Test
    public void testKey() {
        System.out.println(cacheTools.exists("aaa"));
    }
}
