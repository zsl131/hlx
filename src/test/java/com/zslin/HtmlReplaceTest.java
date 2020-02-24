package com.zslin;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("zsl")
public class HtmlReplaceTest {

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
        if(!fileName.contains("fragments")) {
            System.out.println(f.getAbsolutePath());
            BufferedReader br = null;
            try {
                StringBuffer sb = new StringBuffer();
                br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
                String str = null;
                List<String> list = new ArrayList<>();
                while ((str = br.readLine()) != null) {
                    list.add(replace(str));
                }
                int divIndex = 0, index=0;
                String divStr = null;
                for(String s : list) {
                    if(s.contains("</div>")) {divIndex = index; divStr = s;}
                    if(s.contains("</body>") && this.hasReplace) {
//                        bodyIndex = index;
                        list.set(divIndex, divStr.replace("</div>", "</section>"));
                    }
                    //System.out.println(s);
                    index ++;
                }
                //System.out.println("--------------------------");
                for(String s : list) {
//                    System.out.println(s);
                    if(!s.contains("xmlns:layout=\"http://www.ultraq.net.nz/thymeleaf/layout\"")) {
                        sb.append(s);
                    }
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
        if(this.hasReplace) {
            res = replace2(res);
        }
//        res = replace3(res);
        return res+"\n";
    }

    private String replace1(String str) {
        String rep = "layout:decorator=\"fragments/adminModel\">";
        if(str.contains(rep)) {str = str.replace(rep, "th:replace=\"~{fragments/adminModel :: layout(~{::title}, ~{::section})}\">");
            this.hasReplace = true;
        }
        return str;
    }

    private String replace2(String str) {
        String rep = "th:fragment=\"content\"";
        if(str.contains(rep)) {str = "\t\t<section>";}
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
