package com.zslin;

import com.zslin.wx.tools.PictureTools;
import com.zslin.wx.tools.QrTools;
import com.zslin.wx.tools.QrsceneTools;
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

    @Autowired
    private QrsceneTools qrsceneTools;

    @Test
    public void test04() {
        String openid = "o_TZkwbz0pzuCTmrWqMGNHriMHTo";
        String value = "ticket_69_hlx_2021042610005";
        qrsceneTools.processQrscene(openid, value);
    }

    @Test
    public void test03() {
        String url = qrTools.genTicketQr("69_hlx_2021042610002", false);
        System.out.println(url);
    }

    @Test
    public void test() {
        String icon = "http://wx.qlogo.cn/mmopen/72VK6XRwaSb6k1tjN84DqIciboPu0BvJypdia9TfFBUOmWeYYy4Q0MhxvGZdAVdoRr6Rr9oU6U3cpP4ZiaqV2heJnPvfW4y8Qgw/0";
        qrTools.genUserQr("111", icon);
    }

    @Test
    public void test02() {
        String icon = "http://wx.qlogo.cn/mmopen/72VK6XRwaSb6k1tjN84DqIciboPu0BvJypdia9TfFBUOmWeYYy4Q0MhxvGZdAVdoRr6Rr9oU6U3cpP4ZiaqV2heJnPvfW4y8Qgw/0";
        String source = "D:\\temp\\upload\\hlx\\wxqr\\user\\user_111.jpg";
        String target = "D:\\temp\\upload\\hlx\\wxqr\\user\\user_111_new.jpg";
        PictureTools.markImageByUrl(icon, source, target);
    }
}
