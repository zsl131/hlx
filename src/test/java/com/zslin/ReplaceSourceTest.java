package com.zslin;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;

/**
 * 替换原来的资源
 * 之前在static目录下的admin无法访问，改成其他名称即可
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("zsl")
public class ReplaceSourceTest {

    private boolean hasReplace = false;

    @Test
    public void test01() {
        String path = "E:\\idea\\2017_own\\hlx\\src\\main\\resources\\templates";
        handler(path);
    }

    private void handler(String path) {
        File file = new File(path);
        File [] files = file.listFiles();
        for(File f : files) {
            if(f.isDirectory()) {
                handler(f.getAbsolutePath());
            } else {
                process(f);
            }
        }
    }

    private void process(File f) {
        String fileName = f.getAbsolutePath();
        if(fileName.contains(".html")) {
            System.out.println(f.getAbsolutePath());
            BufferedReader br = null;
            try {
                StringBuffer sb = new StringBuffer();
                br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
                String str = null;
                while ((str = br.readLine()) != null) {
                    sb.append(replace(str));
                }
                System.out.println(sb.toString());
                if(this.hasReplace) {
                    rewrite(f, sb.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    br.close();
                    this.hasReplace = false; //完成一个则初始化
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String replace(String temp) {
        String res = replace1(temp);
        res = replace2(res);
        return res+"\n";
    }

    private String replace1(String str) {
        String rep = "href=\"/admin/";
        if(str.contains("<link") && str.contains(rep)) {str = str.replace(rep, "href=\"/back-admin/"); this.hasReplace = true;}
        rep = "href='/admin/";
        if(str.contains("<link") && str.contains(rep)) {str = str.replace(rep, "href='/back-admin/"); this.hasReplace = true;}
        return str;
    }

    private String replace2(String str) {
        String rep = "src=\"/admin/";
        if(str.contains("<script") && str.contains(rep)) {str = str.replace(rep, "src=\"/back-admin/"); this.hasReplace = true;}

        rep = "src='/admin/";
        if(str.contains("<script") && str.contains(rep)) {str = str.replace(rep, "src='/back-admin/"); this.hasReplace = true;}
        return str;
    }

    private void rewrite(File f, String con) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));
            bw.write(con);
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
