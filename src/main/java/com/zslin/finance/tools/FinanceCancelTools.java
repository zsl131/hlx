package com.zslin.finance.tools;

import com.zslin.basic.qiniu.tools.MyFileTools;
import com.zslin.basic.qiniu.tools.QiniuTools;
import com.zslin.basic.tools.ConfigTools;
import com.zslin.finance.dao.IFinanceVoucherDao;
import com.zslin.finance.model.FinanceVoucher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

/**
 * 取消申请处理工具类
 */
@Component
public class FinanceCancelTools {

    @Autowired
    private QiniuTools qiniuTools;

    @Autowired
    private ConfigTools configTools;

    @Autowired
    private IFinanceVoucherDao financeVoucherDao;

    /**
     * 取消申请也需要删除所有的凭证，否则无法再次上传
     * @param id detailId
     */
    public void cancel(Integer id) {
        List<FinanceVoucher> voucherList = financeVoucherDao.findByTargetTypeAndDetailId(FinanceVoucher.TARGET_TYPE_FIN, id);
        for(FinanceVoucher voucher : voucherList) {
            String path = voucher.getPicPath();
            if(MyFileTools.isRemoteFile(path)) {
                qiniuTools.deleteFile(MyFileTools.getFileName(path));
            } else {
                File f = new File(configTools.getFilePath() + path);
                f.delete();
            }
            financeVoucherDao.delete(voucher);
        }
    }
}
