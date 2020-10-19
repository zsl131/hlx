package com.zslin.multi.tools;

import com.zslin.basic.tools.NormalTools;
import com.zslin.multi.dao.IMoneybagDetailDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MoneyBagTools {

    private static final String SEP = "\\n";

    @Autowired
    private IMoneybagDetailDao moneybagDetailDao;

    /**
     * 生成钱包统计字符串
     * @param content 日期，格式：WyyyyMMdd
     * @return
     */
    public String buildBagStr(String content) {
        String createDate = buildDate(content);
        String sepLine = "----------------";
        StringBuffer sb = new StringBuffer();
        Float hlxIn = moneybagDetailDao.queryMoney("1", "hlx", createDate); //入账
        Float hlxOut = moneybagDetailDao.queryMoney("2", "hlx", createDate); //出账
        hlxIn = hlxIn==null?0f:hlxIn; hlxOut = hlxOut==null?0f:hlxOut;

        Float lmsIn = moneybagDetailDao.queryMoney("1", "lms", createDate); //入账
        Float lmsOut = moneybagDetailDao.queryMoney("2", "lms", createDate); //出账
        lmsIn = lmsIn==null?0f:lmsIn; lmsOut = lmsOut==null?0f:lmsOut;

        Float qwzwIn = moneybagDetailDao.queryMoney("1", "qwzw", createDate); //入账
        Float qwzwOut = moneybagDetailDao.queryMoney("2", "qwzw", createDate); //出账
        qwzwIn = qwzwIn==null?0f:qwzwIn; qwzwOut = qwzwOut==null?0f:qwzwOut;
        //sb.append("查询日期：").append(createDate).append(SEP);
       // sb.append("当月天数：").append(days).append(" 天").append(spe);
        //sb.append("消费人次：").append(formatValue(sum, 0)).append(" 人").append(spe);
        //sb.append("平均每天：").append(formatValue(sum/days, 2)).append(" 人").append(spe);
        sb.append("会员充值查询").append(SEP)
            .append("查询日期：").append(createDate).append(sepLine).append(SEP)
            .append("汉丽轩充值：").append(NormalTools.formatValue(hlxIn*1.0, 2)).append(" 元").append(SEP)
            .append("汉丽轩消费：").append(NormalTools.formatValue(hlxOut*1.0, 2)).append(" 天").append(SEP)
            .append(sepLine)
            .append("兰木肆充值：").append(NormalTools.formatValue(lmsIn*1.0, 2)).append(" 元").append(SEP)
            .append("兰木肆消费：").append(NormalTools.formatValue(lmsOut*1.0, 2)).append(" 天").append(SEP)
            .append(sepLine)
            .append("签王充值：").append(NormalTools.formatValue(qwzwIn*1.0, 2)).append(" 元").append(SEP)
            .append("签王消费：").append(NormalTools.formatValue(qwzwOut*1.0, 2)).append(" 天").append(SEP)
            .append("请注意核对数据有效性");
        return sb.toString();
    }

    /** 是否是是查询钱包 */
    public boolean isBagStr(String content) {
        try {
            content = content.toLowerCase();
            if(content.length()==9 && content.startsWith("w") && Integer.parseInt(content.substring(1))>1) {return true;}
            else {return false;}
        } catch (Exception e) {
            return false;
        }
    }

    public String buildDate(String content) {
        return content.substring(1);
    }
}
