package com.zslin.wx.controller;

import com.zslin.basic.qiniu.model.QiniuConfig;
import com.zslin.basic.qiniu.tools.MyFileTools;
import com.zslin.basic.qiniu.tools.QiniuConfigTools;
import com.zslin.basic.qiniu.tools.QiniuTools;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.repository.SpecificationOperator;
import com.zslin.basic.tools.ConfigTools;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.basic.tools.NormalTools;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.client.tools.ClientFileTools;
import com.zslin.finance.dao.IFinancePersonalDao;
import com.zslin.finance.dao.IFinanceVoucherDao;
import com.zslin.finance.imgTools.ImageTextTools;
import com.zslin.finance.model.FinancePersonal;
import com.zslin.finance.model.FinanceVoucher;
import com.zslin.multi.dao.IStoreDao;
import com.zslin.multi.model.Store;
import com.zslin.sms.tools.RandomTools;
import com.zslin.web.model.Account;
import com.zslin.web.model.Income;
import com.zslin.web.service.IAccountService;
import com.zslin.web.service.IIncomeService;
import com.zslin.wx.dto.UploadResult;
import com.zslin.wx.tools.AccountTools;
import com.zslin.wx.tools.IncomeNoticeTools;
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
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by 钟述林 393156105@qq.com on 2017/8/30 10:58.
 */
@Controller
@RequestMapping(value = "wx/income")
public class WeixinIncomeController {

    @Autowired
    private IIncomeService incomeService;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private IncomeNoticeTools incomeNoticeTools;

    @Autowired
    private IFinancePersonalDao financePersonalDao;

    @Autowired
    private IFinanceVoucherDao financeVoucherDao;

    @Autowired
    private ConfigTools configTools;

    @Autowired
    private QiniuTools qiniuTools;

    @Autowired
    private QiniuConfigTools qiniuConfigTools;

    @Autowired
    private IStoreDao storeDao;

    @GetMapping(value = "list")
    public String list(Model model, Integer page, String storeSn, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        Account a = accountService.findByOpenid(openid);
        FinancePersonal personal = financePersonalDao.findByOpenid(openid);
        if((a!=null && AccountTools.isPartner(a.getType())) || personal!=null) { //只有股东或财务人员才可以看
            String month = request.getParameter("filter_comeMonth");
            String sn = request.getParameter("filter_storeSn");
            String storeSns = personal.getStoreSns();

//            System.out.println("========sn="+sn);
//            System.out.println("++++++++storeSN="+storeSn);

            if(sn!=null && sn.indexOf("-")>=0 && (storeSn==null ||"".equals(storeSn))) {storeSn = sn.substring(sn.indexOf("-")+1);}
//            System.out.println("++++2++++storeSN="+storeSn);
            storeSn = (storeSn ==null || "".equals(storeSn.trim()))? ClientFileTools.HLX_SN:storeSn;
//            System.out.println("++++3++++storeSN="+storeSn);
            Page<Income> datas = incomeService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request,
                    new SpecificationOperator("storeSn", "eq", storeSn)),
                    SimplePageBuilder.generate(page, 33, SimpleSortBuilder.generateSort("comeDay_d")));
            month = (month==null||"".equalsIgnoreCase(month) || month.indexOf("-")<0)?NormalTools.curDate("yyyyMM"):month.substring(month.indexOf("-")+1, month.length());

            Double avg = incomeService.average(storeSn, month);
            Integer moreThan = incomeService.moreThan(storeSn, month, 25000d);
            Double totalMoney = incomeService.totalMoney(storeSn, month);

            model.addAttribute("avg", avg==null?0:avg);
            model.addAttribute("moreThan", moreThan==null?0:moreThan);
            model.addAttribute("totalMoney", totalMoney==null?0:totalMoney);
            model.addAttribute("datas", datas);
            model.addAttribute("month", month);
            model.addAttribute("storeName", ClientFileTools.BUILD_STORE_NAME(storeSn)); //店铺名称
            model.addAttribute("storeSn", storeSn);
            model.addAttribute("storeList", storeDao.findByStatus("1", storeSns));
        }
        return "weixin/income/list";
    }

    @GetMapping(value = "show")
    public String show(Model model, Integer id, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        Account a = accountService.findByOpenid(openid);
        FinancePersonal personal = financePersonalDao.findByOpenid(openid);
        if((a!=null && AccountTools.isPartner(a.getType())) || personal!=null) { //只有股东才可以看
            Income income = incomeService.findOne(id);
            model.addAttribute("income", income);
            String month = income.getComeMonth();
            Double avg = incomeService.average(income.getStoreSn(), month);
            Integer moreThan = incomeService.moreThan(income.getStoreSn(), month, 20000d);
            Double totalMoney = incomeService.totalMoney(income.getStoreSn(), month);
            model.addAttribute("avg", avg==null?0:avg);
            model.addAttribute("moreThan", moreThan==null?0:moreThan);
            model.addAttribute("totalMoney", totalMoney==null?0:totalMoney);
        }
        return "weixin/income/show";
    }

    @GetMapping(value = "add")
    public String add(Model model, String day, String storeSn, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        FinancePersonal personal = financePersonalDao.findByOpenid(openid);
        if(personal==null || !"1".equals(personal.getIsCasher())) {
            return "redirect:/wx/business/error?errCode=-1";
        }
        day = (day==null || "".equals(day))?NormalTools.curDate("yyyyMMdd"):day;

        List<Store> storeList ;
        Income income = null;
        try {
            String storeSns = personal.getCashStores();
            storeList = storeDao.findBySns(storeSns.split(";"));
            if(storeList!=null && storeList.size()>0) {
                storeSn = (storeSn==null || "".equals(storeSn.trim()))?storeList.get(0).getSn():storeSn;
                model.addAttribute("curStoreSn", storeSn);
            }
        } catch (Exception e) {
            storeList = new ArrayList<>();
        }
        if(storeSn!=null) {
            income = incomeService.findByComeDay(storeSn, day, "1");
        }
        if(income==null) {
            income = new Income();
            income.setToken(RandomTools.randomString(8).toUpperCase());
        } else {
            if(income.getToken()==null || "".equals(income.getToken().trim())) {
                income.setToken(RandomTools.randomString(8).toUpperCase());
            }
        }
        model.addAttribute("income", income);
        model.addAttribute("storeList", storeList);

        /*List<Store> storeList = null;
        if(personal==null || personal.getStoreSn()==null || "".equals(personal.getStoreSn())) {
            storeList = storeDao.findByStatus("1");
        } else {
            storeList = new ArrayList<>();
            Store s = new Store();
            s.setName(personal.getStoreName());
            s.setSn(personal.getStoreSn());
            storeList.add(s);
        }
        model.addAttribute("storeList", storeList);*/
        model.addAttribute("day", day);
        model.addAttribute("personal", personal);

        return "weixin/income/add";
    }

    @PostMapping(value="add")
    public String add(Model model, String storeSn, Income income, HttpServletRequest request) {
        /*System.out.println("====================");
        System.out.println(income);
        System.out.println(storeSn);
        System.out.println("====================");*/
        Float totalMoney = income.getCash()+income.getAlipay()+income.getFfan()+income.getMarket()+income.getMeituan()+income.getMember()+income.getOther()+income.getWeixin();
        BigDecimal bg = new BigDecimal(totalMoney);
        income.setTotalMoney(bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        String comeDay = income.getComeDay();
        comeDay = comeDay==null||"".equalsIgnoreCase(comeDay)? NormalTools.curDate("yyyyMMdd"):comeDay;
        String comeYear = comeDay.substring(0, 4);
        String comeMonth = comeDay.substring(0, 6);
        income.setComeDay(comeDay);
        income.setComeMonth(comeMonth);
        income.setComeYear(comeYear);
        income.setCreateDay(NormalTools.curDate("yyyy-MM-dd"));
        income.setCreateTime(NormalTools.curDatetime());

        FinanceVoucher fv = financeVoucherDao.findByTargetToken(income.getToken());
        if(fv!=null) {
            income.setTicketPath(fv.getPicPath());
        }

        try {
            String openid = SessionTools.getOpenid(request);
            Account account = accountService.findByOpenid(openid);
            income.setOpenid(openid);
            income.setNickname(account.getNickname());
        } catch (Exception e) {
//            e.printStackTrace();
        }

        if("1".equals(income.getType())) { //如果是营业收入，需要判断是否已经添加
            Income oldIn = incomeService.findByComeDay(income.getStoreSn(), comeDay, income.getType());
            if(oldIn!=null) {
                oldIn.setDeskCount(income.getDeskCount());
                oldIn.setPeopleCount(income.getPeopleCount());
                oldIn.setTotalMoney(income.getTotalMoney());
                oldIn.setCash(income.getCash());
                oldIn.setOther(income.getOther());
                incomeService.save(oldIn);
            } else {
                incomeService.save(income);
            }
        } else {
            incomeService.save(income);
        }

//        incomeNoticeTools.notice(income); //通知
        return "redirect:/wx/income/list?storeSn="+income.getStoreSn();
    }

    @GetMapping(value="update/{id}")
    public String update(Model model, @PathVariable Integer id, HttpServletRequest request) {
        Income in = incomeService.findOne(id);
        model.addAttribute("income", in);
        return "weixin/income/update";
    }

    @PostMapping(value="update/{id}")
    public String update(Model model, @PathVariable Integer id, Income income, HttpServletRequest request) {
        Income in = incomeService.findOne(id);
        MyBeanUtils.copyProperties(income, in, new String[]{"id", "totalMoney"});
//        in.setTotalMoney(income.getCash()+income.getAlipay()+income.getFfan()+income.getMarket()+income.getMeituan()+income.getMember()+income.getOther()+income.getWeixin());
        Float totalMoney = income.getCash()+income.getAlipay()+income.getFfan()+income.getMarket()+income.getMeituan()+income.getMember()+income.getOther()+income.getWeixin();
        BigDecimal bg = new BigDecimal(totalMoney);
        in.setTotalMoney(bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        String comeDay = income.getComeDay();
        comeDay = comeDay==null||"".equalsIgnoreCase(comeDay)? NormalTools.curDate("yyyyMMdd"):comeDay;
        String comeYear = comeDay.substring(0, 4);
        String comeMonth = comeDay.substring(0, 6);
        in.setComeDay(comeDay);
        in.setComeMonth(comeMonth);
        in.setComeYear(comeYear);

        incomeService.save(in);
        return "redirect:/wx/income/list";
    }

    @PostMapping(value = "upload")
    public @ResponseBody
    UploadResult upload(HttpServletRequest request, String objId, String token, String title, @RequestParam("file") MultipartFile[] files) {
        if(files!=null && files.length>=1) {
            BufferedOutputStream bw = null;
            try {
                String fileName = files[0].getOriginalFilename();
                if(fileName!=null && !"".equalsIgnoreCase(fileName.trim()) && NormalTools.isImageFile(fileName)) {

                    File outFile = new File(configTools.getFilePath("income") + UUID.randomUUID().toString()+ NormalTools.getFileType(fileName));

                    FinanceVoucher fv = new FinanceVoucher();
                    fv.setTargetType(FinanceVoucher.TARGET_TYPE_INCOME);
                    fv.setCreateDay(NormalTools.curDate());
                    fv.setCreateTime(NormalTools.curDatetime());
                    fv.setCreateLong(System.currentTimeMillis());
                    fv.setTargetToken(token);
                    boolean hasId = false;
                    int id = 0;
                    try {
                        id = Integer.parseInt(objId);
                        fv.setDetailId(Integer.parseInt(objId));
                        if(id>0) {hasId = true;}
                    } catch (Exception e) {

                    }
                    //fv.setPicPath(outFile.getAbsolutePath().replace(configTools.getFilePath(), "/").replaceAll("\\\\", "/"));
                    FileUtils.copyInputStreamToFile(files[0].getInputStream(), outFile);
                    Thumbnails.of(outFile).size(1300, 1300).toFile(outFile); //修改尺寸

                    String md5 = MyFileTools.getFileMd5(outFile); //文件MD5值
                    /*FinanceVoucher oldVoucher = financeVoucherDao.findByMd5(md5); //通过MD5获取对象
                    if(oldVoucher!=null) {
                        outFile.deleteOnExit();
                        outFile.delete();
                        String detailTitle = financeDetailDao.findTitle(oldVoucher.getDetailId()); //获取报账名称
                        return new UploadResult("-5", "在【"+detailTitle+"】报账中于["+oldVoucher.getCreateTime()+"]已经上传过此凭证");
//                        return "-5"; //表示md5已经存在
                    }*/
                    fv.setFileMd5(md5);

                    try { ImageTextTools.writeText(outFile.getAbsolutePath(), "ID:"+objId+"（"+title+"）"); } catch (Exception e) { }

                    QiniuConfig qiniuConfig = qiniuConfigTools.getQiniuConfig();
                    String key = "income_"+objId+"_"+System.currentTimeMillis()+ MyFileTools.getFileType(outFile.getName());
                    String url = qiniuConfig.getUrl() + "/" + key;
                    FileInputStream fis = new FileInputStream(outFile);
                    qiniuTools.upload(fis, key);
                    fv.setPicPath(url);

                    outFile.deleteOnExit();
                    outFile.delete();

                    financeVoucherDao.save(fv);
                    if(hasId) {
                        incomeService.updateTicketPath(url, id);
                    }
                    return new UploadResult("1", url);
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

        return new UploadResult("1", "上传成功");
    }
}
