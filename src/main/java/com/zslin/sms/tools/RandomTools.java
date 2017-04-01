package com.zslin.sms.tools;

import java.util.Random;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/8 10:51.
 * 随机数工具类
 */
public class RandomTools {

    private static Random randGen = null;
    private static char[] numbersAndLetters = null;

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

    /**
     * 生成长度为length的随机字符串
     * @param length 长度
     * @return
     */
    public static final String randomString(int length) {
        if (length < 1) {
            return null;
        }
        if (randGen == null) {
            randGen = new Random();
            numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
        }
        char [] randBuffer = new char[length];
        for (int i=0; i<randBuffer.length; i++) {
            randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
        }
        return new String(randBuffer);
    }
}
