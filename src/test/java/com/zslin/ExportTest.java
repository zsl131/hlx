package com.zslin;

import com.zslin.admin.dto.MyTicketDto;
import com.zslin.web.model.BuffetOrder;
import com.zslin.web.service.IBuffetOrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/24 10:35.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("zsl")
public class ExportTest {

    @Autowired
    private IBuffetOrderService buffetOrderService;

    @Test
    public void test() {
        String [] days = new String[]{"2017-06-26"};
        for(String day : days) {
            buildSingleDay(day);
        }
    }

    private void buildSingleDay(String day) {
        List<BuffetOrder> list = buffetOrderService.listByHql("FROM BuffetOrder WHERE discountType='6' AND createDay=?", new String[]{day});
        System.out.println(list.size());
        StringBuffer sb = new StringBuffer();
        for(BuffetOrder b : list) {
            sb.append(buildNo(b.getDiscountReason()));
        }
        exportFile(day, sb.toString());
    }

    public String buildNo(String no) {
        StringBuffer sb = new StringBuffer();
        String [] array = no.split(",");
        for(String n : array) {
            if(n!=null && !"".equals(n) && !n.startsWith("999999")) {
                sb.append(n).append("\n");
            }
        }
        return sb.toString();
    }

    public void exportFile(String day, String str) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("D:/hlx-orders-"+day+".txt"))));
            bw.write(str);
            bw.flush();
            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(bw!=null) {bw.close();}
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
