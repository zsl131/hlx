package com.zslin;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;

/**
 * 替换原来主键生成策略AUTO-IDENTITY
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("zsl")
public class ReplaceJavaTest {

    private boolean hasReplace = false;

    @Test
    public void test01() {
        String path = "E:\\idea\\2017_own\\hlx\\src\\main\\java";
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
        if(fileName.contains(".java")) {
            System.out.println(f.getAbsolutePath());
            BufferedReader br = null;
            try {
                StringBuffer sb = new StringBuffer();
                br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
                String str = null;
                while ((str = br.readLine()) != null) {
                    sb.append(replace(str));
                }
                //System.out.println(sb.toString());
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
        return res+"\n";
    }

    private String replace1(String str) {
        String rep = "GenerationType.AUTO";
        if(str.contains(rep)) {str = str.replace(rep, "GenerationType.IDENTITY"); this.hasReplace = true;}
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
