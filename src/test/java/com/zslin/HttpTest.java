package com.zslin;

import com.zslin.wx.tools.JsonTools;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/27 10:00.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("zsl")
public class HttpTest {

    //[{"data":"user","ccid":1,"passwd":"123456","auth":15,"name":"张三","depid":1,"id":1,"card":123},{"data":"fingerprint","ccid":1,"fingerprint":"[\"base64\",\"base64\"]","id":2}]

    @Test
    public void testJson() {
        String json = "[{\"data\":\"user\",\"ccid\":1,\"passwd\":\"123456\",\"auth\":15,\"name\":\"张三\",\"depid\":1,\"id\":1,\"card\":123},{\"data\":\"fingerprint\",\"ccid\":1,\"fingerprint\":\"[\\\"base64\\\",\\\"base64\\\"]\",\"id\":2}]";
        JSONArray array = new JSONArray(json);
        for(int i=0; i<array.length(); i++) {
            JSONObject jsonObj = new JSONObject(array.get(i).toString());
//            String idStr = JsonTools.getJsonParam(jsonObj.toString(), "id");
//            Integer id = Integer.parseInt(idStr);
            Integer id = jsonObj.getInt("id");
            System.out.println("======="+id);
            if(id==2) {
                System.out.println(jsonObj.getString("fingerprint"));
            }
        }
    }

    @Test
    public void testPost() {
        String url = "http://192.168.1.110:81/api/data/post?sn=123456";

        HttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);

        JSONArray array = new JSONArray();
        JSONObject json = new JSONObject();
        json.put("id",1);
        json.put("data", "user");
        json.put("ccid", 1);
        json.put("name", "张三");
        json.put("passwd", "123456");
        json.put("auth", 15);
        json.put("depid", 1);
        json.put("card", 123);

        array.put(json);

        JSONObject json2 = new JSONObject();
        json2.put("id", 2);
        json2.put("data", "fingerprint");
        json2.put("ccid", 1);
        json2.put("fingerprint", "[\"base64\",\"base64\"]");

        array.put(json2);
        try {
            post.addHeader("Content-type", "application/json; charset=utf-8");
            post.setHeader("Accept", "application/json");
            post.setEntity(new StringEntity(array.toString(), Charset.forName("UTF-8")));
            HttpResponse httpResponse = client.execute(post);

            HttpEntity entity = httpResponse.getEntity();
            System.out.println("status:" + httpResponse.getStatusLine());
            System.out.println("response content:" + EntityUtils.toString(entity));
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
