package com.zslin.wx.controller;

import com.zslin.basic.annotations.Token;
import com.zslin.basic.exception.WeixinException;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.repository.SimpleSpecificationBuilder;
import com.zslin.basic.tools.NormalTools;
import com.zslin.basic.tools.SecurityUtil;
import com.zslin.basic.tools.TokenTools;
import com.zslin.client.tools.ClientFileTools;
import com.zslin.client.tools.ClientJsonTools;
import com.zslin.kaoqin.model.Workday;
import com.zslin.kaoqin.model.Worker;
import com.zslin.kaoqin.service.IWorkdayService;
import com.zslin.kaoqin.service.IWorkerService;
import com.zslin.kaoqin.tools.GetJsonTools;
import com.zslin.kaoqin.tools.KaoqinFileTools;
import com.zslin.web.model.Account;
import com.zslin.web.service.IAccountService;
import com.zslin.wx.tools.AccountTools;
import com.zslin.wx.tools.EventTools;
import com.zslin.wx.tools.SessionTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by zsl on 2018/10/13.
 */
@Controller
@RequestMapping(value = "wx/worker")
public class WeixinWorkerController {

    private static final String REDIRECT = "redirect:/wx/account/me";

    @Autowired
    private IWorkerService workerService;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private EventTools eventTools;

    @Autowired
    private KaoqinFileTools kaoqinFileTools;

    @Autowired
    private ClientFileTools clientFileTools;

    @Autowired
    private IWorkdayService workdayService;

    @GetMapping(value = "list")
    public String list(Model model, Integer page, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        Account a = accountService.findByOpenid(openid);
        if(a==null || (!AccountTools.ADMIN.equals(a.getType()) && !AccountTools.PARTNER.equals(a.getType()) && !AccountTools.MANAGER.equals(a.getType()))) {
            return REDIRECT;
        }
        SimpleSpecificationBuilder builder = new SimpleSpecificationBuilder();
        Page<Worker> datas = workerService.findAll(builder.generate(),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("status_a", "id_d")));
        model.addAttribute("datas", datas);
        return "weixin/worker/list";
    }

    @Token(flag= Token.READY)
    @GetMapping(value = "add")
    public String add(Model model, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        Account a = accountService.findByOpenid(openid);
        if(a==null || (!AccountTools.ADMIN.equals(a.getType()) && !AccountTools.PARTNER.equals(a.getType()) && !AccountTools.MANAGER.equals(a.getType()))) {
            return REDIRECT;
        }
        model.addAttribute("worker", new Worker());
        return "weixin/worker/add";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="add", method= RequestMethod.POST)
    public String add(Model model, Worker worker, HttpServletRequest request) throws WeixinException {
        if(TokenTools.isNoRepeat(request)) { //不是重复提交
            Worker w = workerService.findByPhone(worker.getPhone());
            worker.setStatus("1"); //添加时表示都为在职
            if(w!=null) {
                model.addAttribute("errorMessage", "手机号码【"+worker.getPhone()+"】已经存在");
                return "weixin/worker/add";
//                throw new WeixinException("手机号码【"+worker.getPhone()+"】已经存在");
            }
            if(NormalTools.isNull(worker.getPhone()) || NormalTools.isNull(worker.getName()) || NormalTools.isNull(worker.getIdentity())) {
                model.addAttribute("errorMessage", "姓名、手机号、身份证号不能为空");
                return "weixin/worker/add";
            }
            try {
                worker.setPassword(SecurityUtil.md5("123456789")); //所有员工默认密码为123456789
            } catch (NoSuchAlgorithmException e) {
            }

            bind(worker);

            workerService.save(worker);
            buildWorkday(worker);
            sendWorker2Device(worker);
            sendWorker2Client("update", worker);
        }
        return "redirect:/wx/worker/list";
    }

    @PostMapping(value = "updateStatus")
    public @ResponseBody String updateStatus(Integer id, String status, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        Account a = accountService.findByOpenid(openid);
        if(a==null || (!AccountTools.ADMIN.equals(a.getType()) && !AccountTools.PARTNER.equals(a.getType()) && !AccountTools.MANAGER.equals(a.getType()))) {
            return REDIRECT;
        }
        workerService.updateStatus(status, id);
        sendWorker2Client("update", workerService.findOne(id));
        return "1";
    }

    @Token(flag= Token.READY)
    @GetMapping(value = "update")
    public String update(Model model, Integer id, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        Account a = accountService.findByOpenid(openid);
        if(a==null || (!AccountTools.ADMIN.equals(a.getType()) && !AccountTools.PARTNER.equals(a.getType()) && !AccountTools.MANAGER.equals(a.getType()))) {
            return REDIRECT;
        }
        model.addAttribute("worker", workerService.findOne(id));
        return "weixin/worker/update";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="update", method= RequestMethod.POST)
    public String update(Model model, Worker worker, HttpServletRequest request) throws WeixinException {
        if(TokenTools.isNoRepeat(request)) { //不是重复提交
            if(NormalTools.isNull(worker.getPhone()) || NormalTools.isNull(worker.getName()) || NormalTools.isNull(worker.getIdentity())) {
                model.addAttribute("errorMessage", "姓名、手机号、身份证号不能为空");
                return "weixin/worker/update";
            }

            Worker w = workerService.findOne(worker.getId());
            w.setPhone(worker.getPhone());
            w.setName(worker.getName());
            w.setIdentity(worker.getIdentity());
            w.setIsCashier(worker.getIsCashier());
            w.setCanSendCard(worker.getCanSendCard());

            workerService.save(w);
            bind(w);

            buildWorkday(w);
            sendWorker2Device(w);
            sendWorker2Client("update", w);
        }
        return "redirect:/wx/worker/list";
    }

    private void bind(Worker w) {
        try {
            Account a = accountService.findByPhone(w.getPhone());
            if(a!=null && (w.getOpenid()==null || "".equals(w.getOpenid()))) {
                w.setAccountId(a.getId());
                w.setOpenid(a.getOpenid());
                w.setHeadimgurl(a.getHeadimgurl());
                eventTools.eventRemind(a.getOpenid(), "员工绑定提醒", "您已成功绑定员工信息", NormalTools.curDate(), "姓名："+w.getName()+"\\n电话："+w.getPhone(), "");
            }
        } catch (Exception e) {
        }
    }

    private void buildWorkday(Worker worker) {
        Workday w = workdayService.findByWorkerId(worker.getId());
        if(w==null) {
            w = new Workday();
        }
        w.setWorkerName(worker.getName());
        w.setWorkerId(worker.getId());
        w.setWorkerIdentity(worker.getIdentity());
        workdayService.save(w);
    }

    //写员工数据到设备
    private void sendWorker2Device(Worker w) {
        String content = GetJsonTools.buildDataJson(GetJsonTools.buildWorkerJson(w));
        kaoqinFileTools.setChangeContext(content, true);
    }

    private void sendWorker2Client(String action, Worker w) {
        String content = ClientJsonTools.buildDataJson(ClientJsonTools.buildWorker(action, w));
        clientFileTools.setChangeContext(content, true);
    }
}
