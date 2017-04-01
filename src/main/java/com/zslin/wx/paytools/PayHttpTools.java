package com.zslin.wx.paytools;

import com.zslin.wx.tools.WeixinUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 微信支付Http请求工具类
 * @author 钟述林 20151206
 *
 */
@Component
public class PayHttpTools {

	@Autowired
	private PayXmlTools payXmlTools;

	private String httpRequest(String requestUrl,
			String requestMethod, String outputStr) {
		JSONObject res = WeixinUtil.httpRequest(requestUrl, requestMethod, outputStr);
		return res==null?null:res.toString();
	}
	
	/**
	 * 提交支付数据，并返回支付结果
	 * @param paramDto 支付参数DTO对象
	 * @return 返回支付结果
	 */
	public PayResultDto postPay(PayPostDto paramDto) {
		String res = httpRequest("https://api.mch.weixin.qq.com/pay/unifiedorder", "POST", payXmlTools.buildParamXml(paramDto));
		return parsePayResult(res);
	}
	
	public String getPrepayId(String paramXml) {
		String res = httpRequest("https://api.mch.weixin.qq.com/pay/unifiedorder", "POST", paramXml);
		PayResultDto dto = parsePayResult(res);
		return dto.getPrepay_id();
	}
	
	/**
	 * 解析支付结果
	 * @param resStr 结果字符串
	 * @return
	 */
	private PayResultDto parsePayResult(String resStr) {
		PayResultDto dto = new PayResultDto();
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			StringReader sr = new StringReader(resStr);
			InputSource is = new InputSource(sr);
			Document document = db.parse(is);
			
			Element root = document.getDocumentElement();
			Field [] fields = dto.getClass().getDeclaredFields();
			for(Field f : fields) {
				String name = f.getName();
				String name2 = name.substring(0, 1).toUpperCase() + name.substring(1); // 将属性的首字符大写，方便构造get，set方法
//				Node msgId = root.getElementsByTagName("MsgId").item(0);
				NodeList nodeList = root.getElementsByTagName(name);
				if(nodeList!=null && nodeList.getLength()>=1) {
					Node node = nodeList.item(0);
					Method m = dto.getClass().getMethod("set" + name2, String.class);
					m.invoke(dto, node.getTextContent());
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}
}
