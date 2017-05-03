package com.zslin;

import com.zslin.web.service.IBuffetOrderService;
import com.zslin.wx.datadto.DateDto;
import com.zslin.wx.datatools.DateHandlerTools;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by 钟述林 393156105@qq.com on 2017/4/21 14:06.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("zsl")
public class ServiceTest {

    @Autowired
    private IBuffetOrderService buffetOrderService;

    @Test
    public void test01() {
        String date = "2017-04-21";
        Integer cashCount = buffetOrderService.findFinishSelfCount(date, date, "1");
        Integer outCount = buffetOrderService.findFinishOutCount(date, date, "1");
        Integer count = buffetOrderService.findCount(date, date, "1", "2", "4");
        System.out.println(cashCount+"==="+outCount+"==="+count);
    }

    @Test
    public void test02() {
        System.out.println(DateHandlerTools.buildDate("1"));
        System.out.println(DateHandlerTools.buildDate("2"));
        System.out.println(DateHandlerTools.buildDate("3"));
        System.out.println(DateHandlerTools.buildDate("4"));
        System.out.println(DateHandlerTools.buildDate("5"));
    }

    @Test
    public void test03() {
        DateDto dto = DateHandlerTools.buildDate("4");
        Double m1 = buffetOrderService.sumMoney(dto.getStartDay(), dto.getEndDay(), "1", "1", new String[]{"4", "5"});
        Double m2 = buffetOrderService.sumMoney(dto.getStartDay(), dto.getEndDay(), "1", "2", new String[]{"4", "5"});
        Double m3 = buffetOrderService.sumMoney(dto.getStartDay(), dto.getEndDay(), "1", "3", new String[]{"4", "5"});
        Double m4 = buffetOrderService.sumMoney(dto.getStartDay(), dto.getEndDay(), "1", "4", new String[]{"4", "5"});

        Double m5 = buffetOrderService.sumMoney(dto.getStartDay(), dto.getEndDay(), "0", "1", "2");
        Double m6 = buffetOrderService.sumMoney(dto.getStartDay(), dto.getEndDay(), "0", "2", "2");
        Double m7 = buffetOrderService.sumMoney(dto.getStartDay(), dto.getEndDay(), "0", "3", "2");
        Double m8 = buffetOrderService.sumMoney(dto.getStartDay(), dto.getEndDay(), "0", "4", "2");

        m1=m1==null?0:m1;
        m2=m2==null?0:m2;
        m3=m3==null?0:m3;
        m4=m4==null?0:m4;
        m5=m5==null?0:m5;
        m6=m6==null?0:m6;
        m7=m7==null?0:m7;
        m8=m8==null?0:m8;

        System.out.println("="+m1+","+m2+","+m3+","+m4+","+m5+","+m6+","+m7+","+m8);
    }
}
