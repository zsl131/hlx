package com.zslin;

import com.zslin.basic.tools.NormalTools;
import com.zslin.weixin.tools.SendTemplateMessageTools;
import com.zslin.weixin.tools.TemplateMessageAnnotationTools;
import com.zslin.weixin.tools.TemplateMessageTools;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("zsl")
public class TemplateMessageTest {

    @Autowired
    private TemplateMessageAnnotationTools templateMessageAnnotationTools;

    @Autowired
    private SendTemplateMessageTools sendTemplateMessageTools;

    @Test
    public void test01() {
        List<String> openids = new ArrayList<>();
        openids.add("o_TZkwbz0pzuCTmrWqMGNHriMHTo");
        sendTemplateMessageTools.send2Wx(openids, "催促审批提醒", "/wx/finance/show?id=1", "title",
                TemplateMessageTools.field("申请人", "钟述林"),
                TemplateMessageTools.field("申请类型", "汉丽轩"),
                TemplateMessageTools.field("日期", NormalTools.curDate()),
                TemplateMessageTools.field("申请事由", "测试事由"),
                TemplateMessageTools.field("备注", "备注信息"));

    }
}
