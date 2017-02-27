package com.zslin.wx.tools;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * 与微信平台进行交互的网络处理工具类
 * @author 钟述林
 *
 */
public class InternetTools {

	/**
	 * 处理get请求
	 * @param serverName url
	 * @param params 参数
	 * @return 返回结果
	 */
	public static String doGet(String serverName, Map<String, Object> params) {
		String result = null;
		int flag = 0;
		while(result==null && (flag++)<3) {
			try {
				URL url = new URL(rebuildUrl(serverName, params));
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setReadTimeout(5000);
				conn.connect();
				BufferedReader reader =new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
				result = reader.readLine();
				
			} catch (Exception e) {
				System.out.println("InternetTools.toGet 出现异常："+e.getMessage());
			}
		}
		return result;
	}
	
	/**
	 * 重新生成url
	 * @param serverName
	 * @param params
	 * @return
	 */
	private static String rebuildUrl(String serverName, Map<String, Object> params) {
		StringBuffer sb = new StringBuffer(serverName);
		if(serverName.indexOf("?")<0) {
			sb.append("?1=1");
		}
		if(params!=null) {
			for(String key : params.keySet()) {
				sb.append("&").append(key).append("=").append(params.get(key));
			}
		}
		return sb.toString();
	}
}
