package com.zslin.wx.controller;

import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.repository.SpecificationOperator;
import com.zslin.basic.tools.ConfigTools;
import com.zslin.basic.tools.NormalTools;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.finance.dao.IFinanceCategoryDao;
import com.zslin.finance.dao.IFinanceDetailDao;
import com.zslin.finance.dao.IFinancePersonalDao;
import com.zslin.finance.dao.IFinanceVoucherDao;
import com.zslin.finance.imgTools.ImageTextTools;
import com.zslin.finance.model.FinanceDetail;
import com.zslin.finance.model.FinancePersonal;
import com.zslin.finance.model.FinanceVerifyRecord;
import com.zslin.finance.model.FinanceVoucher;
import com.zslin.finance.tools.SignImageTools;
import com.zslin.finance.tools.VerifyRecordTools;
import com.zslin.multi.dao.IStoreDao;
import com.zslin.multi.model.Store;
import com.zslin.web.model.Account;
import com.zslin.web.service.IAccountService;
import com.zslin.wx.tools.EventTools;
import com.zslin.wx.tools.SessionTools;
import net.coobird.thumbnailator.Thumbnails;
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
 * 微信财务管理
 */
@Controller
@RequestMapping(value = "wx/finance")
public class WeixinFinanceController {

    @Autowired
    private IStoreDao storeDao;

    @Autowired
    private IFinancePersonalDao financePersonalDao;

    @Autowired
    private IFinanceCategoryDao financeCategoryDao;

    @Autowired
    private IFinanceDetailDao financeDetailDao;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private SignImageTools signImageTools;

    @Autowired
    private ConfigTools configTools;

    @Autowired
    private IFinanceVoucherDao financeVoucherDao;

    @Autowired
    private EventTools eventTools;

    @Autowired
    private VerifyRecordTools verifyRecordTools;

    private static final String PATH_PRE = "finance";

    @GetMapping(value = "add")
    public String add(Model model, HttpServletRequest request) {
        List<Store> storeList = storeDao.findByStatus("1");
        String openid = SessionTools.getOpenid(request);

        FinanceDetail fd = financeDetailDao.findOne(openid);
        if(fd!=null) { //如果存在未完成的申请，则直接跳转
            return "redirect:/wx/finance/show?id="+fd.getId();
        }
        Account account = accountService.findByOpenid(openid);
        FinancePersonal personal = financePersonalDao.findByOpenid(openid);

        model.addAttribute("account", account);
        model.addAttribute("personal", personal);
        model.addAttribute("storeList", storeList);
        model.addAttribute("categoryList", financeCategoryDao.findAll());
        model.addAttribute("detail", new FinanceDetail());
        return "weixin/finance/add";
    }

    @PostMapping(value = "add")
    public String add(FinanceDetail detail, HttpServletRequest request) {
        /*FinancePersonal personal = financePersonalDao.findOne(personalId);

        detail.setUsername(personal.getName());
        detail.setUserOpenid(personal.getOpenid());
        detail.setUserPhone(personal.getPhone());
        detail.setUserSignPath(personal.getSignPath());*/
        detail.setTotalMoney(NormalTools.numberPoint(detail.getPrice()*detail.getAmount(), 2));

        detail.setCreateDay(NormalTools.curDate("yyyy-MM-dd"));
        detail.setCreateLong(System.currentTimeMillis());
        detail.setCreateTime(NormalTools.curDate("yyyy-MM-dd HH:mm:ss"));
        detail.setStepFlag("0"); //刚刚添加，需要提交到上传附件的页面
        financeDetailDao.save(detail);
        verifyRecordTools.save(FinanceVerifyRecord.TYPE_NOTHING, "报账申请", detail, financePersonalDao.findByOpenid(detail.getUserOpenid()));
        return "redirect:/wx/finance/show?id="+detail.getId();
    }

    @GetMapping(value = "show")
    public String show(Model model, Integer id, HttpServletRequest request) {
        FinanceDetail detail = financeDetailDao.findOne(id);
        String openid = SessionTools.getOpenid(request);
        model.addAttribute("personal", financePersonalDao.findByOpenid(openid));
        model.addAttribute("openid", openid);
        model.addAttribute("detail", detail);
        model.addAttribute("voucherList", financeVoucherDao.findByDetailId(id));
        return "weixin/finance/show";
    }

    @PostMapping(value = "upload")
    public @ResponseBody String upload(HttpServletRequest request, String objId, @RequestParam("file") MultipartFile[] files) {
        if(files!=null && files.length>=1) {
            BufferedOutputStream bw = null;
            try {
                String fileName = files[0].getOriginalFilename();
                if(fileName!=null && !"".equalsIgnoreCase(fileName.trim()) && NormalTools.isImageFile(fileName)) {

                    File outFile = new File(configTools.getFilePath(PATH_PRE) + UUID.randomUUID().toString()+ NormalTools.getFileType(fileName));

                    FinanceVoucher fv = new FinanceVoucher();
                    fv.setCreateDay(NormalTools.curDate());
                    fv.setCreateTime(NormalTools.curDatetime());
                    fv.setCreateLong(System.currentTimeMillis());
                    fv.setDetailId(Integer.parseInt(objId));
                    fv.setPicPath(outFile.getAbsolutePath().replace(configTools.getFilePath(), "/").replaceAll("\\\\", "/"));
                    FileUtils.copyInputStreamToFile(files[0].getInputStream(), outFile);
                    Thumbnails.of(outFile).size(1200, 1200).toFile(outFile); //修改尺寸
                    try { ImageTextTools.writeText(outFile.getAbsolutePath(), "ID:"+objId); } catch (Exception e) { }

                    financeVoucherDao.save(fv);
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

        return "1";
    }

    /** 签名 */
    @GetMapping(value = "sign")
    public String sign(Model model, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        Account account = accountService.findByOpenid(openid);
        FinancePersonal personal = financePersonalDao.findByOpenid(openid);
        model.addAttribute("account", account);
        model.addAttribute("personal", personal);
        return "weixin/finance/sign";
    }

    @PostMapping(value = "sign")
    public @ResponseBody String sign(Integer personalId, String base) {
        signImageTools.saveSign(personalId, base);
        return "1";
    }

    /** 取消申请 */
    @PostMapping(value = "cancel")
    public @ResponseBody String cancel(Integer id) {
        financeDetailDao.cancel(id);
        return "1";
    }

    @GetMapping(value = "listOwn")
    public String listOwn(Model model, Integer page, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        Page<FinanceDetail> datas = financeDetailDao.findAll(ParamFilterUtil.getInstance().buildSearch(model, request,
                new SpecificationOperator("userOpenid", "eq", openid)),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id_d")));
        model.addAttribute("datas", datas);
        return "weixin/finance/listOwn";
    }

    @GetMapping(value = "listConfirm")
    public String listConfirm(Model model, Integer page, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        Page<FinanceDetail> datas = financeDetailDao.findAll(ParamFilterUtil.getInstance().buildSearch(model, request,
                new SpecificationOperator("confirmStatus", "eq", "1", "and"),
                new SpecificationOperator("confirmOpenid", "eq", openid, "and")
                ),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id_d")));
        model.addAttribute("datas", datas);
        return "weixin/finance/listConfirm";
    }

    @GetMapping(value = "listVerify")
    public String listVerify(Model model, Integer page, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        FinancePersonal personal = financePersonalDao.findByOpenid(openid);
        SpecificationOperator so = null;
        if(FinancePersonal.TYPE_BOSS.equals(personal.getType())) {
            so = new SpecificationOperator("status", "eq", "1");
        } else if(FinancePersonal.TYPE_VOUCHER.equals(personal.getType())) {
            so = new SpecificationOperator("voucherStatus", "eq", "1");
        }

        Page<FinanceDetail> datas = financeDetailDao.findAll(ParamFilterUtil.getInstance().buildSearch(model, request,
                so),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id_d")));
        model.addAttribute("datas", datas);
        return "weixin/finance/listVerify";
    }

    /** 修改状态status */
    @PostMapping(value = "updateStatus")
    public @ResponseBody String updateStatus(Integer id, String status, String reason, HttpServletRequest request) {
        reason = (reason==null)?"":reason;
        //financeDetailDao.updateStatus(status, id);
        String openid = SessionTools.getOpenid(request); //获取当前用户的Openid
        FinancePersonal personal = financePersonalDao.findByOpenid(openid);
        FinanceDetail fd = financeDetailDao.findOne(id); //申请对象
        String sep = "\\n";
        if("1".equals(status)) { //如果是设置审核中，则需要通知审核人员
            //System.out.println("WeixinFinanceController.updateStatus"+verifyOpenids);
            if(!"2".equals(fd.getStatus())) { //如果老板没有审核通过，则表示需要提交到老板审核
                StringBuilder remark = new StringBuilder();
                remark.append("报账人：").append(fd.getUsername()).append(sep)
                        .append("对应店铺：").append(fd.getStoreName()).append(sep)
                        .append("报账项目：").append(fd.getTitle()).append(sep)
                        .append("单价：").append(fd.getPrice()).append(sep)
                        .append("数量：").append(fd.getAmount()).append(sep)
                        .append("金额：").append(fd.getTotalMoney()).append(" 元").append(sep);
                List<String> verifyOpenids = financePersonalDao.findByType(FinancePersonal.TYPE_BOSS);
                eventTools.eventRemind(verifyOpenids, "新报账申请", "财务报账", NormalTools.curDatetime(), remark.toString(), "/wx/finance/show?id=" + id);
//                financeDetailDao.updateStatus(status, id);
                fd.setStatus(status);
            } else if("2".equals(fd.getStatus())) {
                if(!"2".equals(fd.getConfirmStatus())) {
                    fd.setConfirmStatus("1");
                    //TODO 通知收货人员
                    StringBuilder remark = new StringBuilder();
                    remark.append("报账人：").append(fd.getUsername()).append(sep)
                            .append("对应店铺：").append(fd.getStoreName()).append(sep)
                            .append("报账项目：").append(fd.getTitle()).append(sep)
                            .append("单价：").append(fd.getPrice()).append(sep)
                            .append("数量：").append(fd.getAmount()).append(sep)
                            .append("金额：").append(fd.getTotalMoney()).append(" 元").append(sep);
                    eventTools.eventRemind(fd.getConfirmOpenid(), "物品需要重新收货确认", "收货确认提醒", NormalTools.curDatetime(), remark.toString(), "/wx/finance/show?id=" + id);
                } else if(!"2".equals(fd.getVoucherStatus())) { //如果财务人员没有审核通过，则提交到财务审核
                    List<String> voucherOpenids = financePersonalDao.findByType(FinancePersonal.TYPE_VOUCHER);
                    fd.setVoucherStatus("1");
                    StringBuilder remark = new StringBuilder();
                    remark.append("报账人：").append(fd.getUsername()).append(sep)
                            .append("对应店铺：").append(fd.getStoreName()).append(sep)
                            .append("报账项目：").append(fd.getTitle()).append(sep)
                            .append("单价：").append(fd.getPrice()).append(sep)
                            .append("数量：").append(fd.getAmount()).append(sep)
                            .append("金额：").append(fd.getTotalMoney()).append(" 元").append(sep);
                    eventTools.eventRemind(voucherOpenids, "报账需要重新财务审核", "财务审核提醒", NormalTools.curDatetime(), remark.toString(), "/wx/finance/show?id=" + id);
                }
            }

        } else if("2".equals(status)) { //通过
            fd.setStatus(status);
            StringBuilder remark = new StringBuilder();
            remark.append("报账人：").append(fd.getUsername()).append(sep)
                    .append("对应店铺：").append(fd.getStoreName()).append(sep)
                    .append("报账项目：").append(fd.getTitle()).append(sep)
                    .append("单价：").append(fd.getPrice()).append(sep)
                    .append("数量：").append(fd.getAmount()).append(sep)
                    .append("金额：").append(fd.getTotalMoney()).append(" 元").append(sep)
                    .append("结果：").append("审核通过，请指定收货人收货");
            eventTools.eventRemind(fd.getUserOpenid(), "报账审核通知", "财务报账被驳回", NormalTools.curDatetime(), remark.toString(), "/wx/finance/show?id="+id);
        } else if("3".equals(status)) { //如果是驳回

            fd.setStatus(status);

            StringBuilder remark = new StringBuilder();
            remark.append("报账人：").append(fd.getUsername()).append(sep)
                    .append("对应店铺：").append(fd.getStoreName()).append(sep)
                    .append("报账项目：").append(fd.getTitle()).append(sep)
                    .append("单价：").append(fd.getPrice()).append(sep)
                    .append("数量：").append(fd.getAmount()).append(sep)
                    .append("金额：").append(fd.getTotalMoney()).append(" 元").append(sep)
                    .append("驳回原因：").append(reason);
            eventTools.eventRemind(fd.getUserOpenid(), "报账审核通知", "财务报账被驳回", NormalTools.curDatetime(), remark.toString(), "/wx/finance/show?id="+id);
        }
        fd.setVerifyDay(NormalTools.curDate());
        fd.setVerifyLong(System.currentTimeMillis());
        fd.setVerifyTime(NormalTools.curDatetime());
        fd.setVerifyReason(reason);
        fd.setVerifyName(personal.getName());
        fd.setVerifyOpenid(personal.getOpenid());
        fd.setVerifyPhone(personal.getPhone());
        fd.setVerifySignPath(personal.getSignPath());

        verifyRecordTools.save(FinanceVerifyRecord.TYPE_BOSS, reason, fd, personal);
        financeDetailDao.save(fd);
        return "1";
    }

    /** 收货确认状态修改 */
    @PostMapping(value = "updateConfirm")
    public @ResponseBody String updateConfirm(Integer id, String status, String reason, HttpServletRequest request) {
        reason = (reason==null)?"":reason;
        String openid = SessionTools.getOpenid(request); //获取当前用户的Openid
        FinancePersonal personal = financePersonalDao.findByOpenid(openid); //操作员
        FinanceDetail fd = financeDetailDao.findOne(id); //申请对象
        String sep = "\\n";
        if("2".equals(status)) { //如果是确认收货，通知财务审核
            reason = "确认收货";
            List<String> verifyOpenids = financePersonalDao.findByType(FinancePersonal.TYPE_VOUCHER); //财务人员
            StringBuilder remark = new StringBuilder();
            remark.append("报账人：").append(fd.getUsername()).append(sep)
                    .append("对应店铺：").append(fd.getStoreName()).append(sep)
                    .append("报账项目：").append(fd.getTitle()).append(sep)
                    .append("单价：").append(fd.getPrice()).append(sep)
                    .append("数量：").append(fd.getAmount()).append(sep)
                    .append("金额：").append(fd.getTotalMoney()).append(" 元").append(sep)
                    .append("审核人：").append(fd.getVerifyName()).append(sep)
                    .append("收货人：").append(personal.getName()).append(sep);
            eventTools.eventRemind(verifyOpenids, "报账物品收货通知", "财务报账", NormalTools.curDatetime(), remark.toString(), "/wx/finance/show?id="+id);
            fd.setVoucherStatus("1"); //设置财务人待审核
        } else if("3".equals(status)) { //如果是确认未收货，通知报账人
            StringBuilder remark = new StringBuilder();
            remark.append("报账人：").append(fd.getUsername()).append(sep)
                    .append("对应店铺：").append(fd.getStoreName()).append(sep)
                    .append("报账项目：").append(fd.getTitle()).append(sep)
                    .append("单价：").append(fd.getPrice()).append(sep)
                    .append("数量：").append(fd.getAmount()).append(sep)
                    .append("金额：").append(fd.getTotalMoney()).append(" 元").append(sep)
                    .append("收货状态：").append("确认未收货").append(sep)
                    .append("驳回原因：").append(reason);
            eventTools.eventRemind(fd.getUserOpenid(), "报账物品未收货通知", "财务报账被驳回", NormalTools.curDatetime(), remark.toString(), "/wx/finance/show?id="+id);
        }
        fd.setConfirmDay(NormalTools.curDate());
        fd.setConfirmLong(System.currentTimeMillis());
        fd.setConfirmName(personal.getName());
        fd.setConfirmOpenid(personal.getOpenid());
        fd.setConfirmReason(reason);
        fd.setConfirmSign(personal.getSignPath());
        fd.setConfirmStatus(status);
        fd.setConfirmTime(NormalTools.curDatetime());
        financeDetailDao.save(fd);

        verifyRecordTools.save(FinanceVerifyRecord.TYPE_CONFIRM, reason, fd, personal);
        return "1";
    }

    /** 财务凭证状态修改 */
    @PostMapping(value = "updateVoucher")
    public @ResponseBody String updateVoucher(Integer id, String status, String reason, HttpServletRequest request) {
        reason = (reason==null)?"":reason;
        String openid = SessionTools.getOpenid(request); //获取当前用户的Openid
        FinancePersonal personal = financePersonalDao.findByOpenid(openid); //操作员
        FinanceDetail fd = financeDetailDao.findOne(id); //申请对象
        String sep = "\\n";
        if("2".equals(status)) { //如果是凭证齐全，通知报账人员
            reason = "凭证齐全";
            //List<String> verifyOpenids = financePersonalDao.findByType(FinancePersonal.TYPE_VOUCHER); //财务人员
            StringBuilder remark = new StringBuilder();
            remark.append("报账人：").append(fd.getUsername()).append(sep)
                    .append("对应店铺：").append(fd.getStoreName()).append(sep)
                    .append("报账项目：").append(fd.getTitle()).append(sep)
                    .append("单价：").append(fd.getPrice()).append(sep)
                    .append("数量：").append(fd.getAmount()).append(sep)
                    .append("金额：").append(fd.getTotalMoney()).append(" 元").append(sep)
                    .append("审核人：").append(fd.getVerifyName()).append(sep)
                    .append("收货人：").append(fd.getConfirmName()).append(sep)
                    .append("财务人：").append(personal.getName()).append(sep);
            eventTools.eventRemind(fd.getUserOpenid(), "报账凭证齐全通知", "财务报账", NormalTools.curDatetime(), remark.toString(), "/wx/finance/show?id="+id);
        } else if("3".equals(status)) { //如果是确认未收货，通过报账人
            StringBuilder remark = new StringBuilder();
            remark.append("报账人：").append(fd.getUsername()).append(sep)
                    .append("对应店铺：").append(fd.getStoreName()).append(sep)
                    .append("报账项目：").append(fd.getTitle()).append(sep)
                    .append("单价：").append(fd.getPrice()).append(sep)
                    .append("数量：").append(fd.getAmount()).append(sep)
                    .append("金额：").append(fd.getTotalMoney()).append(" 元").append(sep)
                    .append("驳回原因：").append(reason);
            eventTools.eventRemind(fd.getUserOpenid(), "报账凭证不全通知", "财务报账被驳回", NormalTools.curDatetime(), remark.toString(), "/wx/finance/show?id="+id);
        }
        fd.setVoucherDay(NormalTools.curDate());
        fd.setVoucherLong(System.currentTimeMillis());
        fd.setVoucherName(personal.getName());
        fd.setVoucherOpenid(personal.getOpenid());
        fd.setVoucherReason(reason);
        fd.setVoucherSign(personal.getSignPath());
        fd.setVoucherStatus(status);
        fd.setVoucherTime(NormalTools.curDatetime());
        financeDetailDao.save(fd);

        verifyRecordTools.save(FinanceVerifyRecord.TYPE_VOUCHER, reason, fd, personal);
        return "1";
    }

    /** 删除凭证 */
    @PostMapping(value = "deleteVoucher")
    public @ResponseBody String deleteVoucher(Integer id) {
        FinanceVoucher fv = financeVoucherDao.findOne(id);
//        System.out.println(fv.getPicPath());
//        System.out.println(configTools.getFilePath());
        File f = new File(configTools.getFilePath() + fv.getPicPath());
//        System.out.println(f.exists());
        f.delete();
        financeVoucherDao.delete(fv);
        return "1";
    }

    /** 获取收货人员 */
    @PostMapping(value = "listPersonal")
    public @ResponseBody List<FinancePersonal> listPersonal(HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        FinancePersonal personal = financePersonalDao.findByOpenid(openid);
        String storeSn = personal.getStoreSn();
        List<FinancePersonal> res ;
        if(storeSn==null || "".equals(storeSn)) {
            res = financePersonalDao.findByMarkFlag("1");
        } else {res = financePersonalDao.findByMarkFlagAndStoreSn("1", storeSn);}
        return res;
    }

    /** 通过昵称或电话获取用户信息 */
    @PostMapping(value = "queryAccount")
    public @ResponseBody
    List<Account> queryAccount(String query) {
        List<Account> list = accountService.query(query);
        return list;
    }

    @PostMapping(value = "savePersonal")
    public @ResponseBody String savePersonal(String name, String phone, String openid, HttpServletRequest request) {
        Account a = accountService.findByOpenid(openid);
        String curOpenid = SessionTools.getOpenid(request);
        FinancePersonal personal = financePersonalDao.findByOpenid(openid);
        if(personal!=null) {
            return "0"; //表示已经添加过了
        }
        personal = new FinancePersonal();
        FinancePersonal curPersonal = financePersonalDao.findByOpenid(curOpenid); //当前用户
        personal.setMarkFlag("1");
        personal.setStoreSn(curPersonal.getStoreSn());
        personal.setStoreName(curPersonal.getStoreName());
        personal.setType("0");
        personal.setPhone(phone);
        personal.setName(name);
        personal.setOpenid(openid);
        personal.setAccountId(a.getId());
        personal.setNickname(a.getNickname());
        financePersonalDao.save(personal);
        return "1";
    }

    /** 设置确认收货人员 */
    @PostMapping(value = "setPersonal")
    public @ResponseBody String setPersonal(Integer personalId, Integer detailId, HttpServletRequest request) {
        FinancePersonal personal = financePersonalDao.findOne(personalId);
        FinanceDetail detail = financeDetailDao.findOne(detailId);
        //TODO 正式使用时，下面代码块取消注释
        if(personal.getOpenid().equals(detail.getUserOpenid())) {
            return "0"; //指定的收货人不能是自己
        }

        detail.setConfirmStatus("1");
        detail.setConfirmOpenid(personal.getOpenid());
        detail.setConfirmName(personal.getName());
        financeDetailDao.save(detail);

        String sep = "\\n";
        StringBuffer remark = new StringBuffer();
        remark.append("报账人：").append(detail.getUsername()).append(sep)
                .append("对应店铺：").append(detail.getStoreName()).append(sep)
                .append("报账项目：").append(detail.getTitle()).append(sep)
                .append("单价：").append(detail.getPrice()).append(sep)
                .append("数量：").append(detail.getAmount()).append(sep)
                .append("金额：").append(detail.getTotalMoney()).append(" 元").append(sep)
                .append("状态：").append("请注意查收货物");
        eventTools.eventRemind(personal.getOpenid(), "有货物需要查收", "注意查收货物", NormalTools.curDatetime(), remark.toString(), "/wx/finance/show?id="+detailId);
        return "1";
    }
}
