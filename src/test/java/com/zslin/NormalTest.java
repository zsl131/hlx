package com.zslin;

import com.zslin.admin.dto.MyTicketDto;
import com.zslin.basic.db.dto.DBConfig;
import com.zslin.basic.db.tools.ExportDBTools;
import com.zslin.basic.model.AppConfig;
import com.zslin.basic.service.IAppConfigService;
import com.zslin.basic.tools.NormalTools;
import com.zslin.basic.tools.SecurityUtil;
import com.zslin.card.dto.CardCheckDto;
import com.zslin.card.service.ICardCheckService;
import com.zslin.card.tools.CardNoTools;
import com.zslin.client.tools.ClientFileTools;
import com.zslin.client.tools.RestdayTools;
import com.zslin.kaoqin.dto.DayDto;
import com.zslin.kaoqin.dto.MonthDto;
import com.zslin.kaoqin.model.Clockin;
import com.zslin.kaoqin.service.IClockinService;
import com.zslin.kaoqin.tools.*;
import com.zslin.multi.tools.MoneyBagTools;
import com.zslin.multi.tools.WaitTableNoTools;
import com.zslin.sms.tools.RandomTools;
import com.zslin.sms.tools.SmsConfig;
import com.zslin.sms.tools.SmsTools;
import com.zslin.web.model.Income;
import com.zslin.web.model.WeixinConfig;
import com.zslin.web.service.IBuffetOrderDetailService;
import com.zslin.web.service.IBuffetOrderService;
import com.zslin.web.service.IIncomeService;
import com.zslin.web.tools.CommonTools;
import com.zslin.weixin.model.HlxTicket;
import com.zslin.weixin.service.IHlxTicketService;
import com.zslin.wx.tools.ExchangeTools;
import com.zslin.wx.tools.HlxTools;
import com.zslin.wx.tools.WxConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ClassUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/24 10:35.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("zsl")
public class NormalTest {

    @Autowired
    private WxConfig wxConfig;

    @Autowired
    private KaoqinFileTools kaoqinFileTools;

    @Autowired
    private SmsTools smsTools;

    @Autowired
    private SmsConfig smsConfig;

    @Autowired
    private IBuffetOrderDetailService buffetOrderDetailService;

    @Autowired
    private IBuffetOrderService buffetOrderService;

    @Autowired
    private IIncomeService incomeService;

    @Autowired
    private RestdayTools restdayTools;

    @Autowired
    private ExchangeTools exchangeTools;

    @Autowired
    private CardNoTools cardNoTools;

    @Autowired
    private ICardCheckService cardCheckService;

    @Autowired
    private IAppConfigService appConfigService;

    @Autowired
    private HlxTools hlxTools;

//    private IdLeafService

    @Autowired
    private IHlxTicketService hlxTicketService;

    @Autowired
    private MoneyBagTools moneyBagTools;

    @Autowired
    private DBConfig config;

    @Autowired
    private ExportDBTools exportDBTools;

    @Autowired
    private WaitTableNoTools waitTableNoTools;

    @Test
    public void test50() throws Exception {
        String str = "data:image/png;base64,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";

        BASE64Decoder decoder = new BASE64Decoder();
        byte[] bytes = decoder.decodeBuffer(str.split(",")[1]);
        for(int i=0;i<bytes.length;i++) {
            if(bytes[i]<0) {bytes[i] += 256;}
        }

        OutputStream out = new FileOutputStream("D:/temp/sign.png");
        out.write(bytes);
        out.flush();
        out.close();

    }

    @Test
    public void test49() {
//        String no = waitTableNoTools.getWaitTableNo("A");
        String storeSn = ClientFileTools.QWZW_SN;
        System.out.println(waitTableNoTools.getWaitTableNo(storeSn, "A"));
        System.out.println(waitTableNoTools.getWaitTableNo(storeSn, "A"));
        System.out.println(waitTableNoTools.getWaitTableNo(storeSn, "A"));
        System.out.println(waitTableNoTools.getWaitTableNo(storeSn, "B"));
        System.out.println(waitTableNoTools.getWaitTableNo(storeSn, "B"));
        System.out.println(waitTableNoTools.getWaitTableNo(storeSn, "C"));
    }

    @Test
    public void test48() {
        String str0 = hlxTools.calDay("hlx");
        System.out.println(str0);

        String str = hlxTools.calDay("qwzw-auto", 0);
        System.out.println(str);
    }

    @Test
    public void test47() {
        exportDBTools.exportDbByJDBC();
    }

    @Test
    public void test46() {
        System.out.println(config);
    }

    @Test
    public void test45() {
        System.out.println("10".split("_")[0]);
        System.out.println("10_".split("_")[0]);
        System.out.println("10_hlx".split("_")[0]);
    }

    @Test
    public void test44() {
        String str = moneyBagTools.buildBagStr("w20201018");
        System.out.println(str);
    }

    @Test
    public void test43() {
        String str = "20201019";
        System.out.println(str);
        System.out.println(Integer.parseInt(str));
        System.out.println(moneyBagTools.isBagStr("W20201019"));
        System.out.println(moneyBagTools.isBagStr("w20201019"));
        System.out.println(moneyBagTools.isBagStr("W2020101 9"));
        System.out.println(moneyBagTools.isBagStr("W20201s19"));
    }

    @Test
    public void test42() {
        String phone = "15925061256";
        System.out.println(phone.substring(phone.length()-4));
    }

    @Test
    public void test41() {
        String storeSn = ClientFileTools.HLX_SN;
        Integer count1 = hlxTicketService.queryAll(storeSn);
        Integer count2 = hlxTicketService.queryAll(storeSn, "1");

        Integer count3 = hlxTicketService.queryByDay(storeSn, "2020-06-04");
        Integer count4 = hlxTicketService.queryWriteOffCount(storeSn, "2020-06-04");

        System.out.println("count1::"+count1);
        System.out.println("count2::"+count2);
        System.out.println("count3::"+count3);
        System.out.println("count4::"+count4);
    }

    @Test
    public void test40() {
        HlxTicket ticket;
        String code = "";
        int i=0;
        do {
            code = RandomTools.genCode7();
            if(i++<50) {
                HlxTicket h = new HlxTicket();
                h.setTicketNo(code);
                hlxTicketService.save(h);
            }
            ticket = hlxTicketService.findByTicketNo(code);
            if(ticket==null) {break;}
        } while(ticket!=null);

        System.out.println(code);
    }

    @Test
    public void test39() {
        for(int i=0;i<100;i++) {
            System.out.print(RandomTools.genCode7() + "      ");
            if(i%50==0) {
                System.out.println();
            }
        }
    }

    @Test
    public void test38() {

        /*@Bean(initMethod = "init")
        public IdLeafService idLeafService() {
            MysqlIdLeafServiceImpl mysqlIdLeafService = new MysqlIdLeafServiceImpl();
            mysqlIdLeafService.setJdbcTemplate(jdbcTemplate);
            mysqlIdLeafService.setAsynLoadingSegment(true);
            mysqlIdLeafService.setBizTag("Order");
            return mysqlIdLeafService;
        }*/
    }

    @Test
    public void test37() {
        String str = hlxTools.calDay("hlx");
        System.out.println(str);
    }

    @Test
    public void test36() {
        System.out.println(buildSn(1));
        System.out.println(buildSn(8));
        System.out.println(buildSn(15));
        System.out.println(buildSn(555));
    }

    private String buildSn(Integer snNo) {
        String str = snNo + "";
        StringBuffer sb = new StringBuffer();
        for(int i = 6-str.length();i>0; i--) {
            sb.append("0");
        }
        sb.append(str);
        return sb.toString();
    }

    @Test
    public void test35() {
        AppConfig ac = appConfigService.loadOne();
        System.out.println(ac.getAppName());
        System.out.println(ac.getAdminEmail());
    }

    @Test
    public void test34() {
        List<CardCheckDto> list = cardCheckService.findCheckDtoByDay("20181016");
        for(CardCheckDto dto : list) {
            System.out.println("======"+dto);
        }

        List<CardCheckDto> list2 = cardCheckService.findCheckDtoByMonth("201810");
        for(CardCheckDto dto : list2) {
            System.out.println("======"+dto);
        }
    }

    @Test
    public void test33() {
        String str = "";
        System.out.printf("========"+isCardNo(str));
        System.out.printf("========"+isCardNo("12345678"));
        System.out.printf("========"+isCardNo("5000002"));
        System.out.printf("========"+isCardNo("100000d"));
        System.out.printf("========"+isCardNo("2000235"));
    }

    private boolean isCardNo(String str) {
        return (str.length()==7 && (str.startsWith("1") || str.startsWith("2") || str.startsWith("3")) && str.matches("[0-9]+")) ;
    }

    @Test
    public void test32() {
        List<Integer> list = cardNoTools.buildCardNos("1", 10);
        for(int i=0;i<list.size();i++) {
            System.out.println(list.get(i));
        }
    }

    @Test
    public void test31() {
        exchangeTools.listWxUsers(null);
    }

    @Test
    public void test30() {
        for(int i=0;i<100000;i++) {
            String code = RandomTools.randomNum4();
            if(code.length()!=4) {
                System.out.println("==============="+code);
            }
        }
    }

    @Test
    public void test29() {
        smsTools.sendMsg(Integer.parseInt(smsConfig.getSendCodeIid()), "18214274897", "code", "00000");
    }

    @Test
    public void test28() throws Exception {
        String pwd = "";
        String p = SecurityUtil.md5("root", "111111");
        System.out.println(p);
    }

    @Test
    public void test27() {
        restdayTools.setRestday("20180115", "1");
        String res = restdayTools.setRestday("20180214_2");
        System.out.println(res+"===="+("20180113_1".indexOf("_")));
    }

    @Test
    public void test26() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, 14);
        System.out.println(cal.get(Calendar.DAY_OF_MONTH)+"====="+cal.get(Calendar.DAY_OF_WEEK));
        cal.set(Calendar.DAY_OF_MONTH, 13);
        System.out.println(cal.get(Calendar.DAY_OF_MONTH)+"====="+cal.get(Calendar.DAY_OF_WEEK));
        cal.set(Calendar.DAY_OF_MONTH, 12);
        System.out.println(cal.get(Calendar.DAY_OF_MONTH)+"====="+cal.get(Calendar.DAY_OF_WEEK));
        cal.set(Calendar.DAY_OF_MONTH, 11);
        System.out.println(cal.get(Calendar.DAY_OF_MONTH)+"====="+cal.get(Calendar.DAY_OF_WEEK));
        cal.set(Calendar.DAY_OF_MONTH, 10);
        System.out.println(cal.get(Calendar.DAY_OF_MONTH)+"====="+cal.get(Calendar.DAY_OF_WEEK));
        cal.set(Calendar.DAY_OF_MONTH, 9);
        System.out.println(cal.get(Calendar.DAY_OF_MONTH)+"====="+cal.get(Calendar.DAY_OF_WEEK));
        cal.set(Calendar.DAY_OF_MONTH, 8);
        System.out.println(cal.get(Calendar.DAY_OF_MONTH)+"====="+cal.get(Calendar.DAY_OF_WEEK));
        cal.set(Calendar.DAY_OF_MONTH, 7);
        System.out.println(cal.get(Calendar.DAY_OF_MONTH)+"====="+cal.get(Calendar.DAY_OF_WEEK));
        cal.set(Calendar.DAY_OF_MONTH, 6);
        System.out.println(cal.get(Calendar.DAY_OF_MONTH)+"====="+cal.get(Calendar.DAY_OF_WEEK));
        cal.set(Calendar.DAY_OF_MONTH, 5);
        System.out.println(cal.get(Calendar.DAY_OF_MONTH)+"====="+cal.get(Calendar.DAY_OF_WEEK));
        System.out.println(cal.get(Calendar.MONTH)+"====="+cal.getActualMaximum(Calendar.DATE));
    }

    @Test
    public void test25() {
        Income income = incomeService.findOne(2);
        Float totalMoney = income.getCash()+income.getAlipay()+income.getFfan()+income.getMarket()+income.getMeituan()+income.getMember()+income.getOther()+income.getWeixin();
        BigDecimal bg = new BigDecimal(totalMoney);
        totalMoney = bg.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        System.out.println("============"+totalMoney);

        Double avg = incomeService.average("hlx", "201708");
        System.out.println("===avg==="+avg);
    }

    @Test
    public void test24() {
        Float f = buffetOrderService.queryBondByStatus("2017-06-20 16:00", "2017-06-20 23:30", "2"); //未退
        System.out.println("未退==============="+f);

        Float f2 = buffetOrderService.queryBondByStatus("2017-06-20 16:00", "2017-06-20 23:30", "5"); //有扣
        System.out.println("有扣==============="+f2);

        Float f3 = buffetOrderService.queryBondMoney("2017-06-20 16:00", "2017-06-20 23:30"); //所有
        System.out.println("==============="+f3);
    }

    @Test
    public void test23() {
        float f1 = (float)NormalTools.buildPoint(45/2f);
        System.out.println(f1);
        float f2 = (float)NormalTools.buildPoint(55/2f);
        System.out.println(f2);
    }

    @Test
    public void test22() {
        MyTicketDto mtd = new MyTicketDto(1, "d");
        Map<MyTicketDto, Integer> map = new HashMap<>();
        map.put(mtd, 3);
        System.out.println(map.containsKey(new MyTicketDto(1, "23")));
    }

    @Test
    public void test21() {
        Float f = buffetOrderService.queryMoneyByTicket("2017-06-11 09:00", "2017-06-11 15:30");
        System.out.println("==============="+f);

        Float f2 = buffetOrderService.queryMoneyByMeiTuan("2017-06-11 09:00", "2017-06-11 15:30");
        System.out.println("==============="+f2);
    }

    @Test
    public void test20() {
        Integer n1 = buffetOrderDetailService.queryCount("2017-06-08", "66666");
        Integer n2 = buffetOrderDetailService.queryCount("2017-06-08", "88888");
        System.out.println(n1+"============"+n2);
    }

    @Test
    public void test19() {
        smsTools.sendMsg(Integer.parseInt(smsConfig.getSendCodeIid()), "15925061256", "code", "123456");
    }

    @Test
    public void test16() {
        System.out.println(File.pathSeparator);
        System.out.println(File.separator);
    }

    @Test
    public void test() {
        WeixinConfig config = wxConfig.getConfig();
        System.out.println(config.getAppid()+"======"+config.getSecret()+"========="+config.getToken());
    }

    @Test
    public void test1() {
        String url = "http://www.zslin.com?id=1#zsl";
        System.out.println(url.substring(0, url.indexOf("#")));
    }

    @Test
    public void test2() {
        String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        System.out.println(path);
    }

    @Test
    public void test03() {
        String str = PicTools.getPicBase64("D:\\temp\\upload\\hlx\\deviceAdvert\\01bc98e3-c0c1-4ec3-bb1a-369cfc1061bb.jpg");
        System.out.println(str);
    }

    @Test
    public void test04() {
        String str = "{status:1,info:\"ok\",data:[{id:\"0\",do:\"update\",data:\"config\",name:\"玉盘珍\",company:\"昭通市玉盘珍餐饮有限公司\",companyid:1,max:3000,function:65535,delay:28,errdelay:10,timezone:+8,encrypt:0,expired:\"2055-12-10 12:10:10\"}}]}\n";
        System.out.println(str);
        System.out.println(str.substring(0, str.length()-2));
        System.out.println("ab\"c\n"+"===="+"ab\"c\n".length());
        String s = "\n";
        System.out.println("".equals(s.trim()));
    }

    @Test
    public void test05() {
        String con = GetJsonTools.buildCleanJson();
        kaoqinFileTools.setChangeContext(GetJsonTools.buildDataJson(con), false);
    }

    @Test
    public void test06() {
        String con = GetJsonTools.buildDeleteWorkerJson(3);
        kaoqinFileTools.setChangeContext(GetJsonTools.buildDataJson(con), false);
    }

    @Test
    public void test07() {
        String con = GetJsonTools.buildRebootDeviceJson();
        kaoqinFileTools.setChangeContext(GetJsonTools.buildDataJson(con), false);
    }

    @Test
    public void test08() {
        for(int i=0;i<20;i++) {
            System.out.println(RandomTools.randomNum4());
        }
    }

    @Test
    public void test09() {
        System.out.println(CommonTools.keep2Point(33d/10));
        System.out.println(CommonTools.daysOfTwo("2017-03-09", "2017-03-18"));
    }

    @Test
    public void test11() {
        String str = "91658502";
        Integer i = Integer.parseInt(str);
        System.out.println(str+"========"+i);
    }

    @Test
    public void test12() throws Exception {
        String day = NormalTools.curDate("yyyy-MM-dd");
        String str = "8:30";
        String s = day + " " + str;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date d = sdf.parse(s);
        System.out.println(s);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        System.out.println(cal.get(Calendar.YEAR)+"=="+cal.get(Calendar.MONTH)+"=="+cal.get(Calendar.DAY_OF_MONTH)+"=="+cal.get(Calendar.HOUR)+"=="+cal.get(Calendar.MINUTE)+"=="+cal.get(Calendar.SECOND));
//        System.out.println(d.getYear()+"=="+d.getMonth()+"=="+d.getDay()+"=="+d.getHours()+"=="+d.getMinutes()+"=="+d.getSeconds());
    }

    @Autowired
    private ClockinTools clockinTools;

    @Test
    public void test13() {
        String clockTime = "2017-04-20 08:22:59";
        clockinTools.clockin(1, clockTime, 1);
    }

    @Autowired
    private IClockinService clockinService;

    @Test
    public void test14() throws Exception {
        String month = "2017-03-";
        for(int i=12; i<=31;i++) {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for(int j=1;j<=4;j++) {
                cal.setTime(sdf.parse(month + i));
                Clockin c = new Clockin();
                c.setDepId(1);
                c.setWorkerId(2);
                c.setWorkerName("张三");
                c.setVerify(1);
                c.setCurDay(sdf.format(cal.getTime()));
                c.setWeekday(getWeekday(cal));
                c.setDay(cal.get(Calendar.DAY_OF_MONTH));
                c.setMonth(cal.get(Calendar.MONTH)+1);
                c.setYear(cal.get(Calendar.YEAR));
                c.setTime("2017-04-20 08:22:59");
                c.setStep(j+"");
                c.setFlag(Math.random() < 0.4 ? 0 : 1);
                clockinService.save(c);
            }
        }
//        System.out.println(getWeekday("2017-03-15"));
    }

    private String getWeekday(Calendar cal) {
        try {
            String[] weekDays = {"日", "一", "二", "三", "四", "五", "六"};

            int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
            if (w < 0)
                w = 0;
            return weekDays[w];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Test
    public void test15() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String str = "2017-3-5";
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(str));
        System.out.println(sdf.format(cal.getTime()));
        System.out.println(cal.get(Calendar.DAY_OF_WEEK));
    }

    /*@Test
    public void test16() {
        List<MonthDto> list = ClockinShowTools.initBefore(2017, 3, 1);
        System.out.println("3====="+list.size());
        list = ClockinShowTools.initBefore(2017, 3, 15);
        System.out.println("17====="+list.size());
        list = ClockinShowTools.initBefore(2017, 3, 18);
        System.out.println("20====="+list.size());
    }*/

    @Autowired
    private ClockinShowTools clockinShowTools;

    @Test
    public void test17() {
        MonthDto dto = clockinShowTools.buildWorkerClockin(2017, 3, 2);
        System.out.println(dto);
        for(DayDto d : dto.getList()) {
            System.out.println(d==null?"-":d);
        }
    }

    @Test
    public void test18() {
        String str = NormalTools.curDate("yyyy-MM");
        String [] array = str.split("-");
        System.out.println(array[0]+"======"+array[1]);
        System.out.println(Integer.parseInt(array[0])+"===="+Integer.parseInt(array[1]));
    }
}
