package com.zslin.wx.controller;

import com.zslin.basic.qiniu.tools.QiniuTools;
import com.zslin.basic.tools.ConfigTools;
import com.zslin.wx.tools.QrTools;
import com.zslin.wx.tools.SessionTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/7 22:45.
 */
@RestController
@RequestMapping(value = "weixin/test")
public class WxTestController {

    @Autowired
    private QrTools qrTools;

    @Autowired
    private QiniuTools qiniuTools;

    @Autowired
    private ConfigTools configTools;

    /*@GetMapping(value = "image")
    public @ResponseBody String image(String text, Integer x, Integer y, String font) {
        try {
            x = x==null?10:x;
            y = y==null?10:y;
            font = font==null?"微软雅黑":font;
            text = text==null?"测试水印":text;
//            ClassPathResource classPathResource = new ClassPathResource("classpath:temp.jpg");
            File tempFile =  ResourceUtils.getFile("classpath:temp/temp.jpg");
            InputStream is =new FileInputStream(tempFile);
            String path = configTools.getFilePath("temp") + "temp.jpg";
            FileOutputStream fos = new FileOutputStream(new File(path));
            byte[] buffer = new byte[1024];
            int len;

            while ((len = is.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            fos.flush();
            is.close();
            fos.close();
            ImageTextTools.writeText(path, text, font);
            FileInputStream fis = new FileInputStream(new File(path));
            qiniuTools.upload(fis, "temp-"+System.currentTimeMillis()+ MyFileTools.getFileType(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "finished";
    }*/

    /** 查看系统字体，用于给图片加水印，防止中文乱码 */
    //@GetMapping(value = "showFont")
    /*public @ResponseBody String showFont() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

        String[] fontFamilies = ge.getAvailableFontFamilyNames();

        StringBuffer sb = new StringBuffer();
        for (String s : fontFamilies) {
            //System.out.println(s);
            sb.append(s).append("\n\n");
        }
        return sb.toString();
    }*/

    @GetMapping(value = "index")
    public String index(HttpServletRequest request) {

        System.out.println("========="+ SessionTools.getOpenid(request));
        return "Hello Weixin";
    }

    @GetMapping(value = "openid")
    public @ResponseBody String openid(String from, String openid, HttpServletRequest request) {
        if(openid!=null && !"".equals(openid)) {
            SessionTools.setOpenid(request, openid);
        } else {
            //如果from为1，则表示是服务器上的正式Openid，否则则是朱霞的Openid
            String o = "1".equals(from) ? "o_TZkwbz0pzuCTmrWqMGNHriMHTo" : "o_TZkwbH119ZLSM0wGbEA7vvJ1bU";
            SessionTools.setOpenid(request, o);
        }
        return "ok";
    }

    /**
     * 生成卡券二维码
     * @param value
     * @return
     */
    @GetMapping(value = "ticketQr")
    public @ResponseBody String ticketQr(String value) {
        String path = qrTools.genTicketQr(value);
        return path;
    }
}
