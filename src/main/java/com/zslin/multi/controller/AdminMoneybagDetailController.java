package com.zslin.multi.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.multi.dao.IMoneybagDao;
import com.zslin.multi.dao.IMoneybagDetailDao;
import com.zslin.multi.dao.IStoreDao;
import com.zslin.multi.model.Moneybag;
import com.zslin.multi.model.MoneybagDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value="admin/moneybagDetail")
@AdminAuth(name="会员详情管理", orderNum=10, psn="多店管理", pentity=0, porderNum=20)
public class AdminMoneybagDetailController {

    @Autowired
    private IMoneybagDao moneybagDao;

    @Autowired
    private IStoreDao storeDao;

    @Autowired
    private IMoneybagDetailDao moneybagDetailDao;

    @GetMapping(value = "list")
    @AdminAuth(name = "会员详情列表", orderNum = 1, type = "1", icon = "fa fa-money")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<MoneybagDetail> datas = moneybagDetailDao.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id_d")));
        model.addAttribute("datas", datas);
        model.addAttribute("storeList", storeDao.findAll());
        return "admin/multi/moneybagDetail/list";
    }

    /** 手动解冻 */
    @PostMapping(value = "unfreeze")
    public @ResponseBody String unfreeze(Integer id) {
        MoneybagDetail detail = moneybagDetailDao.findOne(id);
        if("1".equals(detail.getFreezeFlag())) { //如果是冻结状态
            detail.setFreezeFlag("0");
            Moneybag bag = moneybagDao.findByPhone(detail.getPhone());

            if(bag.getFreezeMoney()>=detail.getMoney()) {
                //3. 钱包可用金额增加
                bag.setMoney(bag.getMoney()+detail.getMoney());
                //4. 钱包冻结金额减少
                bag.setFreezeMoney(bag.getFreezeMoney()-detail.getMoney());
                moneybagDao.save(bag);
            }
        }
        return "1";
    }
}
