package com.zslin.basic.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.qiniu.dao.IQiniuConfigDao;
import com.zslin.basic.qiniu.model.QiniuConfig;
import com.zslin.basic.qiniu.tools.QiniuConfigTools;
import com.zslin.basic.tools.MyBeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * 七牛配置
 * @author zslin.com 20160519
 *
 */
@Controller
@RequestMapping(value="admin/qiniuConfig")
@AdminAuth(name="七牛配置", orderNum=10, psn="系统管理", pentity=0, porderNum=20)
public class AdminQiniuController {

    @Autowired
    private IQiniuConfigDao qiniuConfigDao;

    @Autowired
    private QiniuConfigTools qiniuConfigTools;

    @AdminAuth(name="七牛配置", orderNum=1, icon="fa fa-cog", type="1")
    @RequestMapping(value="index", method= RequestMethod.GET)
    public String index(Model model, HttpServletRequest request) {
        QiniuConfig config = qiniuConfigDao.loadOne();
        if(config==null) {config = new QiniuConfig();}
        model.addAttribute("qiniuConfig", config);
        return "admin/basic/qiniuConfig/index";
    }

    @RequestMapping(value="index", method=RequestMethod.POST)
    public String index(Model model, QiniuConfig qiniuConfig, HttpServletRequest request) {

        QiniuConfig q = qiniuConfigDao.loadOne();
        if(q==null) {
            qiniuConfigDao.save(qiniuConfig);
            qiniuConfigTools.setConfig(qiniuConfig);
        } else {
            MyBeanUtils.copyProperties(qiniuConfig, q, new String[]{"id"});
            qiniuConfigDao.save(q);
            qiniuConfigTools.setConfig(q);
        }

        request.getSession().setAttribute("qiniuConfig", qiniuConfig); //修改后需要修改一次Session中的值
        return "redirect:/admin/qiniuConfig/index";
    }
}