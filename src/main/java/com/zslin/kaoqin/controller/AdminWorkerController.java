package com.zslin.kaoqin.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.annotations.Token;
import com.zslin.basic.exception.SystemException;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.*;
import com.zslin.basic.utils.ParamFilterUtil;
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
import com.zslin.wx.tools.EventTools;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/27 16:47.
 */
@Controller
@RequestMapping(value="admin/worker")
@AdminAuth(name="员工信息维护", orderNum=10, psn="考勤管理", pentity=0, porderNum=20)
public class AdminWorkerController {

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

    private static final String PATH_PRE = "worker/";

    @Autowired
    private ConfigTools configTools;

    @GetMapping(value = "list")
    @AdminAuth(name = "员工信息列表", type = "1", orderNum = 1, icon = "fa fa-users")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<Worker> datas = workerService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("status_a","id_d")));
        model.addAttribute("datas", datas);
        return "admin/worker/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name = "添加员工", orderNum = 2, icon="fa fa-plus")
    @RequestMapping(value="add", method= RequestMethod.GET)
    public String add(Model model, HttpServletRequest request) {
        model.addAttribute("worker", new Worker());
        return "admin/worker/add";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="add", method=RequestMethod.POST)
    public String add(Model model, Worker worker, HttpServletRequest request, @RequestParam("file")MultipartFile[] files) {
        if(TokenTools.isNoRepeat(request)) { //不是重复提交
            Worker w = workerService.findByPhone(worker.getPhone());
            worker.setStatus("1"); //添加时表示都为在职
            if(w!=null) {
                throw new SystemException("手机号码【"+worker.getPhone()+"】已经存在");
            }
            try {
                worker.setPassword(SecurityUtil.md5("123456789")); //所有员工默认密码为123456789
            } catch (NoSuchAlgorithmException e) {
            }

            if(files!=null && files.length>=1) {
                BufferedOutputStream bw = null;
                try {
                    String fileName = files[0].getOriginalFilename();
                    if(fileName!=null && !"".equalsIgnoreCase(fileName.trim()) && NormalTools.isImageFile(fileName)) {
                        File outFile = new File(configTools.getUploadPath(PATH_PRE) + "/" + UUID.randomUUID().toString()+ NormalTools.getFileType(fileName));

                        worker.setHeadPic(outFile.getAbsolutePath().replace(configTools.getUploadPath(), "\\"));
                        FileUtils.copyInputStreamToFile(files[0].getInputStream(), outFile);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if(bw!=null) {bw.close();}
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            bind(worker);

            workerService.save(worker);
            buildWorkday(worker);
            sendWorker2Device(worker);
            sendWorker2Client("update", worker);
        }
        return "redirect:/admin/worker/list";
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

    @Token(flag= Token.READY)
    @AdminAuth(name="修改员工信息", orderNum=3, icon = "fa fa-pencil")
    @RequestMapping(value="update/{id}", method=RequestMethod.GET)
    public String update(Model model, @PathVariable Integer id, HttpServletRequest request) {
        Worker w = workerService.findOne(id);
        model.addAttribute("worker", w);
        return "admin/worker/update";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="update/{id}", method=RequestMethod.POST)
    public String update(Model model, @PathVariable Integer id, Worker worker, HttpServletRequest request, @RequestParam("file")MultipartFile[] files) {
        if(TokenTools.isNoRepeat(request)) {
            Worker w = workerService.findOne(id);

            Worker wTemp = workerService.findByPhone(worker.getPhone());
            if(wTemp!=null && !wTemp.getId().equals(id)) {
                throw new SystemException("手机号码【"+worker.getPhone()+"】已经存在");
            }

            MyBeanUtils.copyProperties(worker, w, new String[]{"id", "password"});

            if(files!=null && files.length>=1) {
                BufferedOutputStream bw = null;
                try {
                    String fileName = files[0].getOriginalFilename();
                    if(fileName!=null && !"".equalsIgnoreCase(fileName.trim()) && NormalTools.isImageFile(fileName)) {

                        File oldFile = new File(configTools.getUploadPath()+w.getHeadPic());
                        if(oldFile.exists()) {oldFile.delete();}

                        File outFile = new File(configTools.getUploadPath(PATH_PRE) + "/" + UUID.randomUUID().toString()+ NormalTools.getFileType(fileName));

                        w.setHeadPic(outFile.getAbsolutePath().replace(configTools.getUploadPath(), "\\"));
                        FileUtils.copyInputStreamToFile(files[0].getInputStream(), outFile);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if(bw!=null) {bw.close();}
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            bind(w);

            workerService.save(w);
            sendWorker2Device(w);
            sendWorker2Client("update", w);
            buildWorkday(w);
        }
        return "redirect:/admin/worker/list";
    }

    @AdminAuth(name="删除员工信息", orderNum=4, icon = "fa fa-remove")
    @RequestMapping(value="delete/{id}", method=RequestMethod.POST)
    public @ResponseBody
    String delete(@PathVariable Integer id) {
        try {
            workerService.delete(id);
            sendDelWorker2Device(id); //从设备中删除员工数据
            Worker w = workerService.findOne(id);
            sendWorker2Client("delete", w); //从客户端删除

            workdayService.deleteByWorkerId(id); //删除员工作息时间
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }

    @AdminAuth(name="初始员工密码", orderNum=4, icon = "fa fa-key")
    @RequestMapping(value="initPwd/{id}", method=RequestMethod.POST)
    public @ResponseBody
    String initPwd(@PathVariable Integer id) {
        try {
            Worker w = workerService.findOne(id);
            w.setPassword(SecurityUtil.md5("123456789"));
            workerService.save(w);
            sendWorker2Client("update", w);
            return "1";
        } catch (Exception e) {
            return "0";
        }
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

    //写员工数据到设备
    private void sendWorker2Device(Worker w) {
        String content = GetJsonTools.buildDataJson(GetJsonTools.buildWorkerJson(w));
        kaoqinFileTools.setChangeContext(content, true);
    }

    private void sendWorker2Client(String action, Worker w) {
        String content = ClientJsonTools.buildDataJson(ClientJsonTools.buildWorker(action, w));
        clientFileTools.setChangeContext(content, true);
    }

    private void sendDelWorker2Device(Integer id) {
        String content = GetJsonTools.buildDataJson(GetJsonTools.buildDeleteWorkerJson(id));
        kaoqinFileTools.setChangeContext(content, true);
    }
}
