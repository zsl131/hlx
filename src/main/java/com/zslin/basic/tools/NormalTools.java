package com.zslin.basic.tools;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Created by zsl-pc on 2016/9/14.
 */
public class NormalTools {

    public static String getFileType(String fileName) {
        if(fileName!=null && fileName.indexOf(".")>=0) {
            return fileName.substring(fileName.lastIndexOf("."), fileName.length());
        }
        return "";
    }

    /**
     * 判断文件是否为图片文件
     * @param fileName
     * @return
     */
    public static Boolean isImageFile(String fileName) {
        String [] img_type = new String[]{".jpg", ".jpeg", ".png", ".gif", ".bmp"};
        if(fileName==null) {return false;}
        fileName = fileName.toLowerCase();
        for(String type : img_type) {
            if(fileName.endsWith(type)) {return true;}
        }
        return false;
    }

    public static String getNow(String pattern) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
        String res = df.format(LocalDateTime.now());
        return res;
    }

    public static Date getDate(String dateStr, String pattern) {
        try {
            DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
            LocalDateTime ldt = LocalDateTime.parse(dateStr, df);
            return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        } catch (Exception e) {return null;}
    }

    public static String curDatetime() {
        return getNow("yyyy-MM-dd HH:mm:ss");
    }

    public static String curDate() {
        return getNow("yyyy-MM-dd");
        /*LocalDate localDate = LocalDate.now();
        return localDate.toString();*/
    }

    public static String curDate(String pattern) {
        return getNow(pattern);
    }

    /**
     * 生成两位小数的数字
     * @param d double类型的数字
     * @return
     */
    public static double buildPoint(double d) {
        /*BigDecimal bg = new BigDecimal(d);
        double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        f1 = Math.rint(f1);
        return f1;*/
        return Math.ceil(d);
    }

    /**
     * 保留几位有效数
     * @param value 数值
     * @param len 保留几位,默认2位
     * @return
     */
    public static String formatValue(Double value, Integer len) {
        if(value==null) {return "-";}
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(len==null?2:len, RoundingMode.HALF_UP);
        return bd.toString();
    }

    /**
     * 保留几位有效数
     * @param value 数值
     * @param len 保留几们数据
     * @return
     */
    public static Float numberPoint(float value, int len) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(len<0?2:len, RoundingMode.HALF_UP);
        return bd.floatValue();
    }

    public static boolean isNull(String val) {
        return val==null || "".equals(val);
    }
}
