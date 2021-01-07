package com.zslin.basic.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.annotations.Token;
import com.zslin.basic.model.BaseTask;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.service.IBaseTaskService;
import com.zslin.basic.tasker.BeanCheckTools;
import com.zslin.basic.tasker.CronTaskRegistrar;
import com.zslin.basic.tasker.TaskDto;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.basic.tools.NormalTools;
import com.zslin.basic.tools.TokenTools;
import com.zslin.basic.utils.ParamFilterUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
@RequestMapping(value="admin/baseTask")
@AdminAuth(name = "定时器管理", psn="系统管理", orderNum = 2, pentity=0, porderNum=2)
@Slf4j
public class BaseTaskController {

    @Autowired
    private IBaseTaskService baseTaskService;

    @Autowired
    private CronTaskRegistrar cronTaskRegistrar;

    @Autowired
    private BeanCheckTools beanCheckTools;

    /** 列表 */
    @AdminAuth(name = "定时器管理", orderNum = 1, icon="fa fa-list", type="1")
    @RequestMapping(value="list", method= RequestMethod.GET)
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<BaseTask> datas = baseTaskService.findAll(new ParamFilterUtil<BaseTask>().buildSearch(model, request), SimplePageBuilder.generate(page));
        model.addAttribute("datas", datas);
        return "admin/basic/baseTask/list";
    }

    /** 添加 */
    @Token(flag= Token.READY)
    @AdminAuth(name = "添加定时器", orderNum = 2, icon="icon-plus")
    @RequestMapping(value="add", method=RequestMethod.GET)
    public String add(Model model, HttpServletRequest request) {
        BaseTask task = new BaseTask();
        model.addAttribute("baseTask", task);
        return "admin/basic/baseTask/add";
    }

    /** 添加POST */
    @Token(flag= Token.CHECK)
    @RequestMapping(value="add", method=RequestMethod.POST)
    public String add(Model model, BaseTask baseTask, HttpServletRequest request) {
//		Boolean isRepeat = (Boolean) request.getAttribute("isRepeat");
        if(TokenTools.isNoRepeat(request)) { //不是重复提交
            if(beanCheckTools.checkMethod(baseTask.getBeanName(), baseTask.getMethodName(), baseTask.getParams())) {
                log.info("{}.{} 任务可以使用", baseTask.getBeanName(), baseTask.getMethodName());
            }

            baseTask.setTaskUuid(UUID.randomUUID().toString());
            baseTask.setCreateDay(NormalTools.curDate());
            baseTask.setCreateTime(NormalTools.curDatetime());
            baseTask.setCreateLong(System.currentTimeMillis());
            baseTaskService.save(baseTask);

            if("1".equals(baseTask.getStatus())) { //如果添加的时候status=1，则自动启动
                cronTaskRegistrar.addTask(baseTask); //添加到任务中
            }
        }
        return "redirect:/admin/baseTask/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name="修改定时器", orderNum=3)
    @RequestMapping(value="update/{id}", method=RequestMethod.GET)
    public String update(Model model, @PathVariable Integer id, HttpServletRequest request) {
        BaseTask b = baseTaskService.findOne(id);
        model.addAttribute("baseTask", b);
        return "admin/basic/BaseTask/update";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="update/{id}", method=RequestMethod.POST)
    public String update(Model model, @PathVariable Integer id, BaseTask baseTask, HttpServletRequest request) {
//		Boolean isRepeat = (Boolean) request.getAttribute("isRepeat");
        if(TokenTools.isNoRepeat(request)) {
            BaseTask obj = baseTaskService.findOne(baseTask.getId());
            MyBeanUtils.copyProperties(baseTask, obj, "id", "createDate", "createTime", "createLong", "createDay", "taskUuid", "sucCount", "errCount");
            baseTaskService.save(obj);
        }
        return "redirect:/admin/baseTask/list";
    }

    @AdminAuth(name="删除定时器", orderNum=4)
    @RequestMapping(value="delete/{id}", method=RequestMethod.POST)
    public @ResponseBody
    String delete(@PathVariable Integer id) {
        try {
            BaseTask task = baseTaskService.findOne(id);
            String taskUuid = task.getTaskUuid();
            TaskDto curTask = cronTaskRegistrar.findByUuid(taskUuid);
            if(curTask!=null) {
//                return JsonResult.getInstance().failFlag("【"+task.getTaskDesc()+"】正在运行，请先停止后再删除！");
                return "-1"; //正在运行，请先停止后再删除！
            } else {
                baseTaskService.delete(task);
                return "1";
            }
        } catch (Exception e) {
            return "0";
        }
    }

    /** 启动任务 */
    @PostMapping(value = "start")
    public @ResponseBody String start(Integer id) {
        try {
//            Integer id = JsonTools.getId(params);
            BaseTask task = baseTaskService.findOne(id);
            cronTaskRegistrar.addTask(task); //添加到任务中
            baseTaskService.updateStatus("1", id); //设置状态为启动

            return "1";

        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

    /** 停止运行 */
    @PostMapping(value = "stopRun")
    public @ResponseBody String stopRun(String uuid) {
        try {
//            String taskUuid = JsonTools.getJsonParam(params, "taskUuid");
            cronTaskRegistrar.removeByUuid(uuid);
            baseTaskService.updateStatus("0", uuid); //修改状态为停止
            return "1";
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }
}
