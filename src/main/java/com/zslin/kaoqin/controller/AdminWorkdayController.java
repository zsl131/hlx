package com.zslin.kaoqin.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.annotations.Token;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.basic.tools.TokenTools;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.kaoqin.model.Workday;
import com.zslin.kaoqin.model.Worker;
import com.zslin.kaoqin.service.IWorkdayService;
import com.zslin.kaoqin.service.IWorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/4/17 16:57.
 * 员工作息时间
 */
@Controller
@RequestMapping(value="admin/workday")
@AdminAuth(name="员工作息时间维护", orderNum=10, psn="考勤管理", pentity=0, porderNum=20)
public class AdminWorkdayController {

    @Autowired
    private IWorkerService workerService;

    @Autowired
    private IWorkdayService workdayService;

    @GetMapping(value = "list")
    @AdminAuth(name = "作息时间列表", type = "1", orderNum = 1, icon = "fa fa-clock-o")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<Workday> datas = workdayService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id_d")));
        model.addAttribute("datas", datas);
        return "admin/workday/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name="修改员工作息时间", orderNum=3, icon = "fa fa-pencil")
    @RequestMapping(value="update/{id}", method= RequestMethod.GET)
    public String update(Model model, @PathVariable Integer id, HttpServletRequest request) {
        Workday w = workdayService.findOne(id);
        model.addAttribute("workday", w);
        return "admin/workday/update";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="update/{id}", method=RequestMethod.POST)
    public String update(Model model, @PathVariable Integer id, Workday workday, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) {
            Workday w = workdayService.findOne(id);

            MyBeanUtils.copyProperties(workday, w, new String[]{"id", "workerId", "workerName", "workerIdentity"});

            workdayService.save(w);
        }
        return "redirect:/admin/workday/list";
    }

    @AdminAuth(name="初始化员工作息时间", orderNum=4, icon = "fa fa-bolt")
    @RequestMapping(value="init", method=RequestMethod.POST)
    public @ResponseBody
    String init() {
        try {
            List<Worker> list = workerService.findAll();
            for(Worker w : list) {
                Workday day = workdayService.findByWorkerId(w.getId());
                if(day==null) {
                    day = new Workday();
                }
                day.setWorkerId(w.getId());
                day.setWorkerIdentity(w.getIdentity());
                day.setWorkerName(w.getName());
                workdayService.save(day);
            }
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }
}
