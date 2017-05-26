package com.zslin.kaoqin.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.kaoqin.model.Company;
import com.zslin.kaoqin.service.ICompanyService;
import com.zslin.kaoqin.tools.GetJsonTools;
import com.zslin.kaoqin.tools.KaoqinFileTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/27 16:29.
 */
@Controller
@RequestMapping(value="admin/company")
@AdminAuth(name="公司信息维护", orderNum=10, psn="考勤管理", pentity=0, porderNum=20)
public class AdminCompanyController {

    @Autowired
    private ICompanyService companyService;

    @Autowired
    private KaoqinFileTools kaoqinFileTools;

    @AdminAuth(name="公司信息维护", orderNum=1, icon="fa fa-cog", type="1")
    @RequestMapping(value="index", method= RequestMethod.GET)
    public String index(Model model, HttpServletRequest request) {
        Company company = companyService.loadOne();
        if(company==null) {company = new Company();}
        model.addAttribute("company", company);
        return "admin/company/index";
    }

    @RequestMapping(value="index", method=RequestMethod.POST)
    public String index(Model model, Company company, HttpServletRequest request) {

        Company c = companyService.loadOne();
        if(c==null) {
            companyService.save(company);
            send2Device(company);
        } else {
            MyBeanUtils.copyProperties(company, c, new String[]{"id", "errdelay"});
            companyService.save(c);
            send2Device(c);
        }

        return "redirect:/admin/company/index";
    }

    //将公司数据发送到设备
    private void send2Device(Company c) {
        String content = GetJsonTools.buildDataJson(GetJsonTools.buildConfigJson(c));
        kaoqinFileTools.setConfigContext(content);
    }
}
