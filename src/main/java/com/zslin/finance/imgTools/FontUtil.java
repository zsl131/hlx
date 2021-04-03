package com.zslin.finance.imgTools;


import org.springframework.util.StringUtils;
import sun.font.FontDesignMetrics;

import java.awt.*;

public class FontUtil {

    //得到默认字体
    public static Font getDefaultFont() {
        return new Font(null);
    }

    //得到字符串长度
    public static int getStringLength(Font font, String str) {
        if (StringUtils.isEmpty(str)) {
            return 0;
        }
        if (null == font) {
            font = getDefaultFont();
        }
        FontDesignMetrics metrics = FontDesignMetrics.getMetrics(font);

        char[] strcha = str.toCharArray();
        int strWidth = metrics.charsWidth(strcha, 0, str.length());
        return strWidth;
    }

    //得到应该换行前的最大字符串
    public static String strSplitMaxLenth(Font font, String str, int maxLength) {
        if (StringUtils.isEmpty(str) || maxLength < 1) {
            return str;
        }
        if (null == font) {
            font = getDefaultFont();
        }
        FontDesignMetrics metrics = FontDesignMetrics.getMetrics(font);
        String max_Str = str;

        int width = 0;
        for (int i = 0; i < str.length(); i++) {
            width += metrics.charWidth(str.charAt(i));
            if (width > maxLength) {
                //上一个长度
                max_Str = str.substring(0, i);
                break;
            }
        }
        return max_Str;
    }

    //是否是最长的字符串，如果是true，就不需要再次进行截断
    public static boolean isMaxStr(Font font, String str, int maxLength) {
        if (StringUtils.isEmpty(str) || maxLength < 1) {
            return true;
        }
        if (null == font) {
            font = getDefaultFont();
        }
        FontDesignMetrics metrics = FontDesignMetrics.getMetrics(font);
        int width = 0;
        for (int i = 0; i < str.length(); i++) {
            width += metrics.charWidth(str.charAt(i));
            if (width > maxLength) {
                return false;
            }
        }
        return width <= maxLength;

    }
}
