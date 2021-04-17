package com.zslin.finance.tools;

import com.zslin.basic.tools.NormalTools;
import com.zslin.finance.dao.IFinanceVerifyRecordDao;
import com.zslin.finance.model.FinanceDetail;
import com.zslin.finance.model.FinancePersonal;
import com.zslin.finance.model.FinanceVerifyRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 记录工具类
 */
@Component
public class VerifyRecordTools {

    @Autowired
    private IFinanceVerifyRecordDao financeVerifyRecordDao;

    public void save(String type, String reason, FinanceDetail detail, FinancePersonal personal) {
        FinanceVerifyRecord vfr = new FinanceVerifyRecord();
        vfr.setCreateDay(NormalTools.curDate());
        vfr.setCreateLong(System.currentTimeMillis());
        vfr.setCreateTime(NormalTools.curDatetime());
        vfr.setDetailId(detail.getId());
        vfr.setOptName(personal.getName());
        vfr.setOptOpenid(personal.getOpenid());
        vfr.setOptPhone(personal.getPhone());
        vfr.setReason(reason);
        vfr.setStoreName(detail.getStoreName());
        vfr.setStoreSn(detail.getStoreSn());
        vfr.setType(type);
        financeVerifyRecordDao.save(vfr);
    }
}
