package com.zslin.admin.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.annotations.Token;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.repository.SpecificationOperator;
import com.zslin.basic.tools.TokenTools;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.web.model.GamePrize;
import com.zslin.web.service.IGamePrizeService;
import com.zslin.web.tools.GamePrizeTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/30 14:54.
 */
@Controller
@RequestMapping(value = "admin/gamePrize")
@AdminAuth(name = "游戏奖励管理", psn = "微信管理", orderNum = 7, porderNum = 1, pentity = 0, icon = "fa fa-gamepad")
public class AdminGamePrizeController {

    @Autowired
    private IGamePrizeService gamePrizeService;

    @Autowired
    private GamePrizeTools gamePrizeTools;

    @GetMapping(value = "list")
    @AdminAuth(name = "游戏奖励管理", type = "1", orderNum = 1, icon = "fa fa-gamepad")
    public String list(Model model, Integer page, String batchNo, HttpServletRequest request) {

        if(batchNo!=null && !"".equals(batchNo)) {
            /*SimpleSpecificationBuilder b = new SimpleSpecificationBuilder();
            b.add("batchNo", "eq", batchNo);*/
            Page<GamePrize> datas = gamePrizeService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request, new SpecificationOperator("batchNo", "eq", batchNo)),
                    SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("winnerLong_d")));
            model.addAttribute("datas", datas);
        } else {
            ParamFilterUtil.getInstance().buildSearch(model, request);
            Page<GamePrize> datas = gamePrizeService.findByBatch(SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id_d")));
            model.addAttribute("datas", datas);
        }
        return "admin/gamePrize/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name = "添加游戏中奖名单", orderNum = 2, icon="fa fa-plus")
    @RequestMapping(value="add", method= RequestMethod.GET)
    public String add(Model model, HttpServletRequest request) {
        return "admin/gamePrize/add";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="add", method=RequestMethod.POST)
    public String add(Model model, String title, String batchNo, HttpServletRequest request, @RequestParam("file")MultipartFile[] files) {
        if(TokenTools.isNoRepeat(request)) { //不是重复提交
            if(files!=null && files.length>=1) {
                try {
                    gamePrizeTools.processExcel(title, batchNo, files[0].getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "redirect:/admin/gamePrize/list";
    }
}
