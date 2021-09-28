package com.zslin.weixin.tools;

import com.zslin.finance.dao.IFinanceDetailDao;
import com.zslin.finance.model.FinanceDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component
public class ModifyFinanceDetailTools {

    @Autowired
    private IFinanceDetailDao financeDetailDao;

    public String process(Integer detailId) {
        FinanceDetail fd = financeDetailDao.findOne(detailId);
        if(fd!=null && fd.getConfirmDay()==null) {
            Date newDate = newDate(rebuildDate(fd.getVerifyTime()), 120);
            fd.setConfirmDay(getDateString(newDate, "yyyy-MM-dd"));
            fd.setConfirmTime(getDateString(newDate, "yyyy-MM-dd HH:mm:ss"));
            fd.setConfirmLong(newDate.getTime());
            fd.setConfirmName("李云鹏");
            fd.setConfirmOpenid("o_TZkwf48eG6KilaMI4gcjd0jhoI");
            fd.setConfirmSign("/sign/sign_5.png");
            fd.setConfirmReason("确认收货");
            fd.setConfirmStatus("2");
            fd.setVoucherStatus("1");
            financeDetailDao.save(fd);
            return "Process Success!!";
        }
        return "No Found Need Processed Detail...";
    }

    private Date newDate(Date oldDate, Integer plus) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(oldDate);
        cal.add(Calendar.MINUTE, plus);
        return cal.getTime();
    }

    private Date rebuildDate(String str) {
        try {
            String pattern = "yyyy-MM-dd HH:mm:ss";
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.parse(str);
        } catch (ParseException e) {
            return new Date();
        }
    }

    private String getDateString(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }
}
