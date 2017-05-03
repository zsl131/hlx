package com.zslin.kaoqin.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.tools.NormalTools;
import com.zslin.kaoqin.dto.MonthDto;
import com.zslin.kaoqin.model.Clockin;
import com.zslin.kaoqin.service.IClockinService;
import com.zslin.kaoqin.tools.ClockinShowTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/4/20 14:39.
 */
@Controller
@RequestMapping(value="admin/clockin")
@AdminAuth(name="员工考勤统计", orderNum=10, psn="考勤管理", pentity=0, porderNum=20)
public class AdminClockinController {

    @Autowired
    private IClockinService clockinService;

    @Autowired
    private ClockinShowTools clockinShowTools;

    @GetMapping(value = "index")
    @AdminAuth(name = "员工考勤统计", type = "1", orderNum = 1, icon = "fa fa-history")
    public String index(Model model, String month, HttpServletRequest request) {
        if(month==null || "".equalsIgnoreCase(month)) {month = NormalTools.curDate("yyyy-MM");}
        String [] array = month.split("-");
        Integer year = Integer.parseInt(array[0]);
        Integer m = Integer.parseInt(array[1]);
        List<Clockin> workers = clockinService.findByGroup(year, m);
        List<MonthDto> datas = new ArrayList<>();
        for(Clockin c : workers) {
            datas.add(clockinShowTools.buildWorkerClockin(year,m ,c.getWorkerId()));
        }
        model.addAttribute("datas", datas);
        model.addAttribute("month", month);
        return "admin/clockin/index";
    }
}
