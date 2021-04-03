package com.zslin;

import com.zslin.finance.imgTools.BufferedImgBean;
import com.zslin.finance.imgTools.ImageTextTools;
import com.zslin.finance.imgTools.ImgBean;
import com.zslin.finance.imgTools.ImgJoinUtil;
import com.zslin.wx.tools.PictureTools;
import net.coobird.thumbnailator.Thumbnails;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("zsl")
public class PictureTest {

    @Test
    public void test06() throws Exception {
        ImageTextTools.writeText("D:/temp/test/head-rebuild.jpg", "ID:123456");
    }

    @Test
    public void test05() {
        String str1 = "D:/temp/test/head.jpg";
        String suffix = str1.substring(str1.lastIndexOf(".")+1);
        String res = str1.substring(0, str1.lastIndexOf("."))+"-rebuild."+suffix;
        System.out.println(suffix);
        System.out.println(res);
    }

    @Test
    public void test04() throws Exception {
        String str1 = "D:/temp/test/head.jpg";
        String str2 = "D:/temp/test/share1.jpg";
        String str3 = "D:/temp/test/share2.jpg";
        String str4 = "D:/temp/test/share4.jpg";
        ImgJoinUtil.createColorImg("D:/temp/test/pintu.jpg", str1, str2, str3, str4);
    }

    @Test
    public void test02() throws Exception {
        Thumbnails.of("D:/temp/test/head.jpg").size(400, 200).toFile("D:/temp/test/pro-resize.jpg");
        Thumbnails.of("D:/temp/test/head.jpg").forceSize(400, 200).toFile("D:/temp/test/pro-resize2.jpg");
    }

    @Test
    public void test01() throws Exception {
        BufferedImage image = PictureTools.resize("D:/temp/pro.jpg", 794, 1123);
        ImageIO.write(image, "jpg", new File("D:/temp/pro-resize.jpg"));
    }

    @Test
    public void test03() {
        java.util.List<BufferedImgBean> imgList = new ArrayList<>();
        String str1 = "D:/temp/test/head.jpg";
        String str2 = "D:/temp/test/share1.jpg";
        String str3 = "D:/temp/test/share2.jpg";
        String str4 = "D:/temp/test/share4.jpg";
//        File f2 = new File("D:/temp/test/share1.jpg");
//        File f3 = new File("D:/temp/test/share2.jpg");
//        File f4 = new File("D:/temp/test/share4.jpg");
        imgList.add(new BufferedImgBean(str1, 0, 0));
        imgList.add(new BufferedImgBean(str2, 397, 0));
        imgList.add(new BufferedImgBean(str3, 0, 551));
        imgList.add(new BufferedImgBean(str4, 397, 551));
        ImgJoinUtil.createColorImg(794, 1123, new Color(255, 255, 255), "D:/temp/test/result.jpg", imgList, null);
    }
}
