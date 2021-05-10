package com.zslin.finance.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.finance.dao.IFinanceCategoryDao;
import com.zslin.finance.dao.IFinanceDetailDao;
import com.zslin.finance.model.FinanceDetail;
import com.zslin.finance.tools.FinanceCancelTools;
import com.zslin.finance.tools.PDFTools;
import com.zslin.multi.dao.IStoreDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Controller
@RequestMapping(value="admin/financeDetail")
@AdminAuth(name="财务报账管理", orderNum=10, psn="财务管理", pentity=0, porderNum=20)
public class AdminFinanceDetailController {

    @Autowired
    private IFinanceDetailDao financeDetailDao;

    @Autowired
    private IFinanceCategoryDao financeCategoryDao;

    @Autowired
    private IStoreDao storeDao;

    @Autowired
    private PDFTools pdfTools;

    @Autowired
    private FinanceCancelTools financeCancelTools;

    /** 生成PDF文件 */
    @GetMapping(value = "printDetail")
    @AdminAuth(name = "打印财务信息", orderNum = 2, icon = "fa fa-print")
    public void printDetail(String ids, HttpServletResponse response) {
        try {
            response.setContentType("application/pdf"); // 设置返回内容格式
            OutputStream os = response.getOutputStream();
            List<FinanceDetail> detailList = financeDetailDao.findByIds(buildIds(ids));
            financeDetailDao.updatePrintFlag("1", buildIds(ids));
            pdfTools.buildPDF(os, detailList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Integer [] buildIds(String ids) {
        String [] array = ids.split(",");
        Integer [] res = new Integer[array.length];
        for(int i=0;i<array.length;i++) {
            res[i] = Integer.parseInt(array[i]);
        }
        return res;
    }

    @GetMapping(value = "list")
    @AdminAuth(name = "财务报账管理", orderNum = 1, type = "1", icon = "fa fa-cny")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<FinanceDetail> datas = financeDetailDao.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id_d")));
        model.addAttribute("datas", datas);
        model.addAttribute("categoryList", financeCategoryDao.findAll());
        model.addAttribute("storeList", storeDao.findByStatus("1"));
        return "admin/finance/financeDetail/list";
    }

    @AdminAuth(name="删除食品", orderNum=4, icon = "fa fa-remove")
    @RequestMapping(value="delete/{id}", method= RequestMethod.POST)
    public @ResponseBody
    String delete(@PathVariable Integer id) {
        try {
            FinanceDetail f = financeDetailDao.findOne(id);
            financeCancelTools.cancel(id); //处理凭证
            financeDetailDao.delete(f);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }
}
