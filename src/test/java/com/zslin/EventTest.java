package com.zslin;

import com.zslin.web.service.IBuffetOrderService;
import com.zslin.wx.dto.EventRemarkDto;
import com.zslin.wx.dto.TemplateParamDto;
import com.zslin.wx.tools.EventTools;
import com.zslin.wx.tools.HlxTools;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/20 0:23.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("zsl")
public class EventTest {

    @Autowired
    private EventTools eventTools;

    @Autowired
    private HlxTools hlxTools;

    @Test
    public void test02() {
        String res = hlxTools.queryFinanceByTimer();
        System.out.println(res);
    }

    @Test
    public void test01() {
        eventTools.sendTemplateMessage("8JtjA6PaHT9eYXn8Mb5CKJpjbl6mZnGoP-qEccIAzKY", "orLIDuFuyeOygL0FBIuEilwCF1lU",
                "测试标题", "#FF0000", "http://www.zslin.com",
                new TemplateParamDto("第一字段", "第一个"),
                new TemplateParamDto("第二字段", "第二个"),
                new TemplateParamDto("第三字段", "第三个"),
                new TemplateParamDto(new EventRemarkDto("备注一个", "这里备注一"),new EventRemarkDto("备注二个", "这里备注二")));
    }
}
