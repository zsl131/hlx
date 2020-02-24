package com.zslin.kaoqin.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.annotations.Token;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.ConfigTools;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.basic.tools.NormalTools;
import com.zslin.basic.tools.TokenTools;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.kaoqin.model.DeviceAdvert;
import com.zslin.kaoqin.service.IDeviceAdvertService;
import com.zslin.kaoqin.tools.GetJsonTools;
import com.zslin.kaoqin.tools.KaoqinFileTools;
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
import java.util.List;
import java.util.UUID;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/25 23:11.
 */
@Controller
@RequestMapping(value="admin/deviceAdvert")
@AdminAuth(name="设备广告", orderNum=10, psn="考勤管理", pentity=0, porderNum=20)
public class AdminDeviceAdvertController {

    @Autowired
    private IDeviceAdvertService deviceAdvertService;

    @Autowired
    private ConfigTools configTools;

    @Autowired
    private KaoqinFileTools kaoqinFileTools;

    private static final String PATH_PRE = "deviceAdvert";

    @GetMapping(value = "list")
    @AdminAuth(name = "设备广告管理", orderNum = 1, type = "1", icon = "fa fa-bullhorn")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<DeviceAdvert> datas = deviceAdvertService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("orderNo")));
        model.addAttribute("datas", datas);
        return "admin/deviceAdvert/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name = "添加设备广告", orderNum = 2, icon="fa fa-plus")
    @RequestMapping(value="add", method= RequestMethod.GET)
    public String add(Model model, HttpServletRequest request) {
        model.addAttribute("deviceAdvert", new DeviceAdvert());
        return "admin/deviceAdvert/add";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="add", method=RequestMethod.POST)
    public String add(Model model, DeviceAdvert deviceAdvert, HttpServletRequest request, @RequestParam("file")MultipartFile[] files) {
        if(TokenTools.isNoRepeat(request)) { //不是重复提交
            if(files!=null && files.length>=1) {
                BufferedOutputStream bw = null;
                try {
                    String fileName = files[0].getOriginalFilename();
                    if(fileName!=null && !"".equalsIgnoreCase(fileName.trim()) && NormalTools.isImageFile(fileName)) {
                        File outFile = new File(configTools.getFilePath(PATH_PRE) + File.separator + UUID.randomUUID().toString()+ NormalTools.getFileType(fileName));
                        deviceAdvert.setPicPath(outFile.getAbsolutePath().replace(configTools.getFilePath(), File.separator));
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
            deviceAdvertService.save(deviceAdvert);
        }
        return "redirect:/admin/deviceAdvert/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name="修改设备广告", orderNum=3, icon = "fa fa-pencil")
    @RequestMapping(value="update/{id}", method=RequestMethod.GET)
    public String update(Model model, @PathVariable Integer id, HttpServletRequest request) {
        DeviceAdvert d = deviceAdvertService.findOne(id);
        model.addAttribute("deviceAdvert", d);
        return "admin/deviceAdvert/update";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="update/{id}", method=RequestMethod.POST)
    public String update(Model model, @PathVariable Integer id, DeviceAdvert deviceAdvert, HttpServletRequest request, @RequestParam("file")MultipartFile[] files) {
        if(TokenTools.isNoRepeat(request)) {
            DeviceAdvert d = deviceAdvertService.findOne(id);
            MyBeanUtils.copyProperties(deviceAdvert, d, new String[]{"id"});

            if(files!=null && files.length>=1) {
                BufferedOutputStream bw = null;
                try {
                    String fileName = files[0].getOriginalFilename();
                    if(fileName!=null && !"".equalsIgnoreCase(fileName.trim()) && NormalTools.isImageFile(fileName)) {

                        File oldFile = new File(configTools.getFilePath()+d.getPicPath());
                        if(oldFile.exists()) {oldFile.delete();}

                        File outFile = new File(configTools.getFilePath(PATH_PRE) + File.separator + UUID.randomUUID().toString()+ NormalTools.getFileType(fileName));
                        d.setPicPath(outFile.getAbsolutePath().replace(configTools.getFilePath(), File.separator));
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

            deviceAdvertService.save(d);
        }
        return "redirect:/admin/deviceAdvert/list";
    }

    @AdminAuth(name="删除设备广告", orderNum=4, icon = "fa fa-remove")
    @RequestMapping(value="delete/{id}", method=RequestMethod.POST)
    public @ResponseBody
    String delete(@PathVariable Integer id) {
        try {

            DeviceAdvert d = deviceAdvertService.findOne(id);
            File oldFile = new File(configTools.getFilePath()+d.getPicPath());
            if(oldFile.exists()) {oldFile.delete();}

            deviceAdvertService.delete(id);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }

    @AdminAuth(name="推送广告到设备", orderNum=4, icon = "fa fa-list")
    @RequestMapping(value="send", method=RequestMethod.POST)
    public @ResponseBody
    String send() {
        try {
            List<DeviceAdvert> list = deviceAdvertService.findUse();
            String content = GetJsonTools.buildDataJson(GetJsonTools.buildAdvertJson(list, configTools.getFilePath()));
            kaoqinFileTools.setChangeContext(content, true);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }
}
