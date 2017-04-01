package com.zslin.wx.paytools;

import com.zslin.sms.tools.RandomTools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 微信支付普通工具类
 * @author 钟述林 20151206
 *
 */
public class PayNormalTools {

	/** 获取系统当前时间，格式为：yyyyMMddHHmmss */
	public static String getCurtime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(new Date());
	}
	
	/** 获取几分钟之后的时间，格式为：yyyyMMddHHmmss */
	public static String getAfterTime(Integer min) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, min);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(cal.getTime());
	}
	
	/** 获取系统时间戳 */
	public static String getTimeStamp() {
		return String.valueOf(System.currentTimeMillis() / 1000);
	}
	
	/** 生成随机字符串 */
	public static String buildRandString() {
		StringBuffer sb = new StringBuffer(getCurtime()+ RandomTools.randomString(10));
		return sb.toString();
	}
}
