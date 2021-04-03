package com.zslin.web.controller;

import com.zslin.basic.tools.NormalTools;
import com.zslin.client.tools.ClientFileTools;
import com.zslin.multi.dao.IStoreDao;
import com.zslin.multi.dao.ITableCategoryDao;
import com.zslin.multi.dao.IWaitTableDao;
import com.zslin.multi.model.Store;
import com.zslin.multi.model.TableCategory;
import com.zslin.multi.model.WaitTable;
import com.zslin.multi.tools.WaitTableNoTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 排队
 */
@Controller
@RequestMapping(value = "web/WaitTableController")
public class WebWaitTableController {

    @Autowired
    private IWaitTableDao waitTableDao;

    @Autowired
    private WaitTableNoTools waitTableNoTools;

    @Autowired
    private ITableCategoryDao tableCategoryDao;

    @Autowired
    private IStoreDao storeDao;

    @GetMapping(value = "index")
    public String index(Model model, String storeSn, String cateFlag, HttpServletRequest request) {
        String days = NormalTools.curDate("yyyyMMdd"); //当前日期
        storeSn = (storeSn==null || "".equals(storeSn))? ClientFileTools.QWZW_SN:storeSn;
        List<TableCategory> categoryList = tableCategoryDao.findByStoreSn(storeSn);
        List<WaitTable> waitList = null;
        if(cateFlag==null || "".equals(cateFlag)) {waitList = waitTableDao.findByStoreSnAndCreateDay(storeSn, days);}
        else {waitList = waitTableDao.findByStoreSnAndCreateDayAndCateFlag(storeSn, days, cateFlag);}


        model.addAttribute("waitList", waitList);
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("storeList", storeDao.findAll());

        return "web/waitTable/index";
    }

    /**
     * 排队
     * @param cateId
     * @param storeSn
     * @param peopleCount
     * @param phone
     * @return
     */
    @PostMapping(value = "onWait")
    public @ResponseBody String onWait(Integer cateId, String storeSn, Integer peopleCount, String phone) {
        try {
            WaitTable wt = new WaitTable();
            TableCategory category = tableCategoryDao.findOne(cateId);
            Store store = storeDao.findBySn(storeSn);
            wt.setCateFlag(category.getFlag());
            wt.setCateId(cateId);
            wt.setCateName(category.getName());
            wt.setCreateDay(NormalTools.curDate("yyyyMMdd"));
            wt.setCreateLong(System.currentTimeMillis());
            wt.setCreateTime(NormalTools.curDate("yyyy-MM-dd HH:mm:ss"));
            wt.setPeopleCount(peopleCount==null||peopleCount<0?0:peopleCount);
            wt.setPhone(phone);
            wt.setStatus("0");
            wt.setStoreName(store.getName());
            wt.setStoreSn(storeSn);
            wt.setWaitNo(waitTableNoTools.getWaitTableNo(storeSn, category.getFlag()));
            waitTableDao.save(wt);
            return "1";
        } catch (Exception e) {
            return e.getMessage();
        }

    }
}
