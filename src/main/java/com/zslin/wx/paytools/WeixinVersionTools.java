package com.zslin.wx.paytools;

/**
 * 微信版本检测工具类
 * @author 钟述林 20151205
 *
 */
public class WeixinVersionTools {

	/**
	 * 判断用户的微信版本是否支持微信支付功能
	 * @param agent 用户的头文件
	 * @return
	 */
	public static boolean supportPay(String agent) {
		String version = getWeixinVersion(agent);
		if(version==null) {return false;}
		Integer ver = 0;
		if(version.indexOf(".")>=0) {
			try {
				ver = Integer.parseInt(version.substring(0, version.indexOf(".")));
			} catch (Exception e) {
				return false;
			}
		} else {
			try {
				ver = Integer.parseInt(version);
			} catch (Exception e) {
				return false;
			}
		}
		if(ver>=5) {return true;}
		return false;
	}
	
	/**
	 * 获取微信版本号
	 * @param agent 用户浏览器头文件
	 * @return
	 */
	public static String getWeixinVersion(String agent) {
		if(agent==null) {return null;}
		if(agent.toLowerCase().indexOf("MicroMessenger/".toLowerCase())>=0){
			String temp = agent.toLowerCase().split("MicroMessenger/".toLowerCase())[1];
			if(temp.indexOf(" ")>0) {
				return temp.substring(0, temp.indexOf(" "));
			} else {return temp;}
		} else {return null;}
	}
}
