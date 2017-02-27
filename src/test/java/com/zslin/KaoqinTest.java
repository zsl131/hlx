package com.zslin;

import com.zslin.basic.tools.SecurityUtil;
import com.zslin.kaoqin.model.Worker;
import com.zslin.kaoqin.tools.GetJsonTools;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/27 0:09.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("zsl")
public class KaoqinTest {

    @Test
    public void test0() throws Exception {
        Worker w = new Worker();
        w.setName("钟述林");
        w.setAuth(14);
        w.setDepId(2);
        w.setId(1);
        w.setPassword(SecurityUtil.md5("123456"));
        List<Worker> list = new ArrayList<>();
        list.add(w);
        String json = GetJsonTools.buildDataJson(GetJsonTools.buildWorkerJson(list));
        System.out.println(json);
    }
}
