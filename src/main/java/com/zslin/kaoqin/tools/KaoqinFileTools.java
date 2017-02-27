package com.zslin.kaoqin.tools;

import org.springframework.util.ClassUtils;

import java.io.*;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/26 23:51.
 */
public class KaoqinFileTools {

    private static File getFile() {
        String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        path = path.replace("/target/test-classes/", "/");
        File file = new File(path+"kaoqin-get.txt");
        return file;
    }

    public static String getFileContext() {
        BufferedReader br = null;
        String res = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(getFile()), "UTF-8"));
            res = br.readLine();
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
        return res;
    }

    public static void setFileContext(String content) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(getFile()), "UTF-8"));
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
