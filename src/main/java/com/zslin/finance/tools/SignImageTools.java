package com.zslin.finance.tools;

import com.zslin.basic.tools.ConfigTools;
import com.zslin.finance.dao.IFinancePersonalDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Decoder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 财务人员电子签名工具类
 */
@Component
public class SignImageTools {

    @Autowired
    private ConfigTools configTools;

    @Autowired
    private IFinancePersonalDao financePersonalDao;

    /**
     * 传入Base64，转换成图片 存入数据库
     * @param personalId
     * @param baseStr
     */
    public void saveSign(Integer personalId, String baseStr) {
        try {
            String str = baseStr.substring(baseStr.indexOf(",")+1);
            //System.out.println(baseStr);
            //System.out.println(str);
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] bytes = decoder.decodeBuffer(str);
            for(int i=0;i<bytes.length;i++) {
                if(bytes[i]<0) {bytes[i] += 256;}
            }
            String filePath = configTools.getFilePath("sign")+"sign_"+personalId+".png";
            OutputStream out = new FileOutputStream(filePath);
            out.write(bytes);
            out.flush();
            out.close();

            String path = filePath.replace(configTools.getFilePath(), "/");
            financePersonalDao.updateSignPath(path, personalId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
