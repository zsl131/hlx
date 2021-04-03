package com.zslin;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.print.PrintService;
import java.awt.print.PrinterJob;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("zsl")
public class PrintTest {

    @Test
    public void test01() {
        PrintService[] printServices = PrinterJob.lookupPrintServices();
        for(PrintService ps : printServices) {
            System.out.println(ps.getName());
        }
    }
}
