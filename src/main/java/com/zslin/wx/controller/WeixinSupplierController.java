package com.zslin.wx.controller;

import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.repository.SpecificationOperator;
import com.zslin.basic.tools.NormalTools;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.finance.dao.IFinancePersonalDao;
import com.zslin.finance.dao.ISupplierDao;
import com.zslin.finance.model.FinancePersonal;
import com.zslin.finance.model.Supplier;
import com.zslin.wx.tools.SessionTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "wx/supplier")
public class WeixinSupplierController {

    @Autowired
    private ISupplierDao supplierDao;

    @Autowired
    private IFinancePersonalDao financePersonalDao;

    @GetMapping(value = "list")
    public String list(Model model, Integer page, String condition, HttpServletRequest request) {
        if(condition!=null) {
            condition = condition.replace("eq-", "");
        }

        Page<Supplier> datas = supplierDao.findAll(ParamFilterUtil.getInstance().buildSearch(model, request,
                buildConditions(condition)),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id_d")));
        model.addAttribute("datas", datas);
        model.addAttribute("condition", condition);
        return "weixin/supplier/list";
    }

    private SpecificationOperator[] buildConditions(String condition) {
        List<SpecificationOperator> list = new ArrayList<>();
        if(condition!=null && !"".equals(condition.trim())) {
            Supplier supplier = new Supplier();
            Field [] fields = supplier.getClass().getDeclaredFields();
            for(Field field : fields) {
                String fieldName = field.getName();
                if(!isIgnore(fieldName)) {
                    list.add(new SpecificationOperator(fieldName, "like", condition, "or"));
                }
            }
        }
        return list.toArray(new SpecificationOperator[list.size()]);
    }

    /** 判断是否忽略 */
    private boolean isIgnore(String field) {
        boolean res = false;
        String [] fields = new String[]{"id", "createDay", "createTime", "createLong",
                "optOpenid", "optPhone"};
        for(String f : fields) {
            if(field.equals(f)) {res = true; break;}
        }
        return res;
    }

    @GetMapping(value = "add")
    public String add(Model model, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        FinancePersonal personal = financePersonalDao.findByOpenid(openid);

        if(personal==null || !"1".equals(personal.getMarkFlag())) { //如果收货人员
            return "redirect:/wx/supplier/list";
        }

        model.addAttribute("personal", personal);
        model.addAttribute("supplier", new Supplier());
        return "weixin/supplier/add";
    }

    @PostMapping(value = "add")
    public String add(Supplier supplier, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        FinancePersonal personal = financePersonalDao.findByOpenid(openid);

        supplier.setCreateDay(NormalTools.curDate());
        supplier.setCreateTime(NormalTools.curDatetime());
        supplier.setCreateLong(System.currentTimeMillis());
        supplier.setOptName(personal.getName());
        supplier.setOptOpenid(openid);
        supplier.setOptPhone(personal.getPhone());
        supplierDao.save(supplier);

        return "redirect:/wx/supplier/list";
    }
}
