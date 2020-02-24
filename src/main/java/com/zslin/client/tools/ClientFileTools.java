package com.zslin.client.tools;

import com.zslin.basic.tools.ConfigTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/26 23:51.
 */
@Component
public class ClientFileTools {

    //汉丽轩的sn
    public static final String HLX_SN = "hlx";

    private static final String GET_FILE = "client-get.txt";
    private static final String CONF_FILE = "client-config.txt";

    @Autowired
    private ConfigTools configTools;

    private File getConfigFile(String storeSn) {
        return getFile(storeSn, CONF_FILE);
    }

    private File getChangeFile(String storeSn) {
        return getFile(storeSn, GET_FILE);
    }

    /**
     * 默认是汉丽轩
     */
    public void setConfigContext(String content) {
        setConfigContext(HLX_SN, content);
    }

    /**
     * 默认是汉丽轩
     */
    public void setChangeContext(String content, boolean isAppend) {
        setChangeContext(HLX_SN, content, isAppend);
    }

    public void setConfigContext(String storeSn, String content) {
        setFileContext(getConfigFile(storeSn), content);
    }

    public void setChangeContext(String storeSn, String content, boolean isAppend) {
        if(isAppend) { //如果是追加则需要修改里面的内容
            String con = getChangeContext(storeSn);
            if(con!=null && !"".equals(con.trim())) {
                int minus =2;
                if(con.endsWith("\n")) {minus+=1;}
                String temp = "{status:1,info:\"ok\",data:[";
                content = con.substring(0, con.length() - minus) + "," + content.replace(temp, "");
            }
        }
        setFileContext(getChangeFile(storeSn), content);
    }

    /**
     * 默认是汉丽轩
     * @return
     */
    /*public String getConfigContext() {
        return getConfigContext(HLX_SN);
    }*/

    /**
     * 默认是汉丽轩
     */
    /*public String getChangeContext() {
        return getChangeContext(HLX_SN);
    }*/

    public String getConfigContext(String storeSn) {
        return getFileContext(getConfigFile(storeSn));
    }

    public String getChangeContext(String storeSn) {
        return getFileContext(getChangeFile(storeSn));
    }

    private File getFile(String storeSn, String fileName) {
        File file = new File(configTools.getFilePath("client"+File.separator+storeSn)+fileName);
        if(!file.exists()) {createFile(file);}
        return file;
    }

    private void createFile(File file) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
            bw.write("");
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(bw!=null) {
                    bw.close();
                }
            } catch (IOException e) {
            }
        }
    }

    private String getFileContext(File file) {
        BufferedReader br = null;
//        String res = null;
        StringBuffer sb = new StringBuffer();
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String str = null;
            while((str=br.readLine())!=null) {
                sb.append(str).append("\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(br!=null) {
                    br.close();
                }
            } catch (IOException e) {
            }
        }
        return sb.toString();
    }

    private void setFileContext(File file, String content) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
            bw.write(content);
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(bw!=null) {
                    bw.close();
                }
            } catch (IOException e) {
            }
        }
    }
}
