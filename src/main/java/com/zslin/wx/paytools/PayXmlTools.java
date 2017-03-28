package com.zslin.wx.paytools;

import com.zslin.web.model.WeixinConfig;
import com.zslin.wx.tools.WxConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 支付相关的XML工具类
 * @author 钟述林 20151206
 *
 */
@Component
public class PayXmlTools {

	@Autowired
	private WxConfig wxConfig;

	@Autowired
	private PaySignTools paySignTools;

	/**
	 * 生成POST数据的XML字符串
	 * @param dto 参数DTO对象
	 * @return
	 */
	public String buildParamXml(PayPostDto dto) {
		WeixinConfig config = wxConfig.getConfig();
		dto.setAppid(config.getAppid()); //APPID
		dto.setMch_id(config.getMchid()); //商户ID
		dto.setNotify_url(config.getNotifyUrl()); //设置回调地址
		dto.setSign(paySignTools.createMD5Sign(dto)); //赋值签名
		SortedMap<String, Object> params = new TreeMap<>();
		
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("<xml>");
			Field [] fields = dto.getClass().getDeclaredFields();
			for(Field f : fields) {
				String name = f.getName();
				name = name.substring(0, 1).toUpperCase() + name.substring(1); // 将属性的首字符大写，方便构造get，set方法
				
				Method m = dto.getClass().getMethod("get" + name);
				Object val = m.invoke(dto);
				
				params.put(name.toLowerCase(), val);
			}
			
			for(String key : params.keySet()) {
				Object val = params.get(key);
				if(val!=null && !"".equals(val)) {
					sb.append("\n\t<").append(key).append(">");
					/*if("attach".equalsIgnoreCase(key) || "body".equalsIgnoreCase(key) || "sign".equalsIgnoreCase(key)) {
						sb.append("<![CDATA[").append(val).append("]]>");
					} else {
						sb.append(val);
					}*/
					sb.append(val);
					sb.append("</").append(key).append(">");
				}
			}
			sb.append("\n</xml>");
			//TODO 测试先输入提交的XML数据
			System.out.println("=====================================");
			System.out.println(sb.toString());
			System.out.println("=====================================");
		} catch (Exception e) {
		}
		return sb.toString();
	}
}
