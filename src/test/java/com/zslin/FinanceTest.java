package com.zslin;

import com.zslin.finance.dao.IFinanceDetailDao;
import com.zslin.finance.model.FinanceDetail;
import com.zslin.finance.tools.PDFTools;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("zsl")
public class FinanceTest {

    @Autowired
    private PDFTools pdfTools;

    @Autowired
    private IFinanceDetailDao financeDetailDao;

    @Test
    public void test01() throws Exception {
        List<FinanceDetail> detailList = financeDetailDao.listByHql("FROM FinanceDetail f WHERE f.storeSn='hlx' AND id!=14");
        pdfTools.buildPDF(new FileOutputStream(new File("D:/temp/output.pdf")), detailList);
    }
}
