package com.zslin.sms.tools;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/8 10:51.
 * 随机数工具类
 */
public class RandomTools {

    public static String randomNum4() {
        int num = 9999;
        return randomNum(num);
    }

    public static String randomNum6() {
        int num = 999999;
        return randomNum(num);
    }

    private static String randomNum(int num) {
        int res = (int)(Math.random()*num);
        return res+"";
    }
}
