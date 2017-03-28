package com.zslin.wx.paytools;

import com.zslin.basic.tools.SecurityUtil;
import com.zslin.web.model.WeixinConfig;
import com.zslin.wx.tools.WxConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 微信支付的签名工具类
 * @author 钟述林 20151204
 */
@Component
public class PaySignTools {

	@Autowired
	private WxConfig wxConfig;

	/**
	 * 生成MD5的签名
	 * @param dto 参数列表
	 * @return
	 */
	public String createMD5Sign(PayPostDto dto) {
		WeixinConfig config = wxConfig.getConfig();
		dto.setAppid(config.getAppid());
		dto.setMch_id(config.getMchid());
		dto.setNotify_url(config.getNotifyUrl());
		try {
			SortedMap<String, Object> params = new TreeMap<String, Object>();
			Field [] fields = dto.getClass().getDeclaredFields();
			for(Field f : fields) {
				String name = f.getName();
				name = name.substring(0, 1).toUpperCase() + name.substring(1); // 将属性的首字符大写，方便构造get，set方法
				
				Method m = dto.getClass().getMethod("get" + name);
				Object val = m.invoke(dto);
				
				params.put(name.toLowerCase(), val);
			}
			String sign = createSign(params);
			return sign;
		} catch (Exception e) {
			return null;
		}
	}
	
	public String createSign(SortedMap<String, Object> params) {
		try {
			StringBuffer sb = new StringBuffer();
			for(String key : params.keySet()) {
				Object val = params.get(key);
				if(val!=null && !"".equals(val) && !"sign".equals(key) && !"key".equals(key)) {
					sb.append(key).append("=").append(val).append("&");
				}
			}
			sb.append("key=").append(wxConfig.getConfig().getApiKey());
			String sign = SecurityUtil.md5(sb.toString()).toUpperCase();
			return sign;
		} catch (Exception e) {
			return"";
		}
	}
	
	public String createResultSign(PayResultDto dto, String timestamp) {
		try {
			SortedMap<String, Object> params = new TreeMap<>();
//			params.put("appid", dto.getAppid());
			params.put("appId", dto.getAppid()); 
			params.put("timeStamp", timestamp);
			params.put("nonceStr", dto.getNonce_str());
			params.put("package", "prepay_id="+dto.getPrepay_id());
			params.put("signType", "MD5");
			
			String sign = createSign(params);
			return sign;
		} catch (Exception e) {
			return "";
		}
	}
	
	public String genPackage(SortedMap<String, Object> packageParams) {
		String sign = createSign(packageParams);

		StringBuffer sb = new StringBuffer();
		
		for(String key : packageParams.keySet()) {
			Object val = packageParams.get(key);
			sb.append(key).append("=").append(UrlEncode(val.toString())).append("&");
		}
		
		// 去掉最后一个&
		String packageValue = sb.append("sign=" + sign).toString();
//		System.out.println("UrlEncode后 packageValue=" + packageValue);
		return packageValue;
	}
	
	public static String UrlEncode(String src) {
		try {
			return URLEncoder.encode(src, "UTF-8").replace("+", "%20");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}
}
