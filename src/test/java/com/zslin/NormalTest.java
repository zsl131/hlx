package com.zslin;

import com.zslin.kaoqin.tools.GetJsonTools;
import com.zslin.kaoqin.tools.KaoqinFileTools;
import com.zslin.kaoqin.tools.PicTools;
import com.zslin.sms.tools.RandomTools;
import com.zslin.web.model.WeixinConfig;
import com.zslin.web.tools.CommonTools;
import com.zslin.wx.tools.JsonTools;
import com.zslin.wx.tools.WxConfig;
import org.json.JSONArray;
import org.json.JSONObject;
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

    @Autowired
    private KaoqinFileTools kaoqinFileTools;

    @Test
    public void test() {
        WeixinConfig config = wxConfig.getConfig();
        System.out.println(config.getAppid()+"======"+config.getSecret()+"========="+config.getToken());
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

    @Test
    public void test03() {
        String str = PicTools.getPicBase64("D:\\temp\\upload\\hlx\\deviceAdvert\\01bc98e3-c0c1-4ec3-bb1a-369cfc1061bb.jpg");
        System.out.println(str);
    }

    @Test
    public void test04() {
        String str = "{status:1,info:\"ok\",data:[{id:\"0\",do:\"update\",data:\"config\",name:\"玉盘珍\",company:\"昭通市玉盘珍餐饮有限公司\",companyid:1,max:3000,function:65535,delay:28,errdelay:10,timezone:+8,encrypt:0,expired:\"2055-12-10 12:10:10\"}}]}\n";
        System.out.println(str);
        System.out.println(str.substring(0, str.length()-2));
        System.out.println("ab\"c\n"+"===="+"ab\"c\n".length());
        String s = "\n";
        System.out.println("".equals(s.trim()));
    }

    @Test
    public void test05() {
        String con = GetJsonTools.buildCleanJson();
        kaoqinFileTools.setChangeContext(GetJsonTools.buildDataJson(con), false);
    }

    @Test
    public void test06() {
        String con = GetJsonTools.buildDeleteWorkerJson(3);
        kaoqinFileTools.setChangeContext(GetJsonTools.buildDataJson(con), false);
    }

    @Test
    public void test07() {
        String con = GetJsonTools.buildRebootDeviceJson();
        kaoqinFileTools.setChangeContext(GetJsonTools.buildDataJson(con), false);
    }

    @Test
    public void test08() {
        for(int i=0;i<20;i++) {
            System.out.println(RandomTools.randomNum4());
        }
    }

    @Test
    public void test09() {
        System.out.println(CommonTools.keep2Point(33d/10));
        System.out.println(CommonTools.daysOfTwo("2017-03-09", "2017-03-18"));
    }

    @Test
    public void test10() {
        String str = "{\n" +
                "  \"errcode\": 0,\n" +
                "  \"data\": {\n" +
                "    \"totalcount\": 16,\n" +
                "    \"pageindex\": 1,\n" +
                "    \"pagecount\": 8,\n" +
                "    \"records\": [\n" +
                "      {\n" +
                "        \"shop_id\": 429620,\n" +
                "        \"shop_name\": \"南山店\",\n" +
                "        \"ssid\": \"WX123\",\n" +
                "        \"ssid_list\": [\n" +
                "          \"WX123\",\n" +
                "          \"WX456\"\n" +
                "        ],\n" +
                "        \"protocol_type\": 4,\n" +
                "        \"sid\": \"\",\n" +
                "        \"poi_id\":\"285633617\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"shop_id\": 7921527,\n" +
                "        \"shop_name\": \"宝安店\",\n" +
                "        \"ssid\": \"\",\n" +
                "        \"ssid_list\": [],\n" +
                "        \"protocol_type\": 0,\n" +
                "        \"sid\": \"\",\n" +
                "        \"poi_id\":\"285623614\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";

        JSONObject jsonObj = new JSONObject(str);
        JSONObject data = jsonObj.getJSONObject("data");
        System.out.println(data.getInt("totalcount"));
        JSONArray array = data.getJSONArray("records");
        for(int i=0;i<array.length();i++) {
            JSONObject single = array.getJSONObject(i);
            System.out.println("single===="+single.getString("shop_name")+"===="+single.getInt("shop_id"));
        }
    }
}
