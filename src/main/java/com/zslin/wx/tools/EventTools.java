package com.zslin.wx.tools;

import com.qq.weixin.mp.aes.WXBizMsgCrypt;
import com.zslin.wx.dto.TempParamDto;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 接收消息的事件处理工具类
 * @author 钟述林 20150729
 *
 */
@Component
public class EventTools {

	@Autowired
	private WxConfig wxConfig;

	@Autowired
	private AccessTokenTools accessTokenTools;

	/**
	 * 获取事件消息的元素对象
	 * @param request
	 * @return
	 */
	public Element getMessageEle(HttpServletRequest request) {
		Element root = null;
		try {
			String signature = request.getParameter("signature"); //微信加密签名
			String timestamp = request.getParameter("timestamp"); //时间戳
			String nonce = request.getParameter("nonce"); //随机数
			
			WXBizMsgCrypt pc = new WXBizMsgCrypt(wxConfig.getToken(), wxConfig.getAeskey(), wxConfig.getAppid());
			
			InputStream in =  request.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(
					in, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);
			StringBuffer buffer = new StringBuffer();
			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = null;
			Document document = null;
			String resultStr = buffer.toString();
			if(resultStr.indexOf("Encrypt")>=0) { 
				StringReader sr = new StringReader(resultStr);
				is = new InputSource(sr);
				document = db.parse(is);
				
				root = document.getDocumentElement();
				NodeList nodelist1 = root.getElementsByTagName("Encrypt");

				String encrypt = nodelist1.item(0).getTextContent();
				
				String format = "<xml><ToUserName><![CDATA[toUser]]></ToUserName><Encrypt><![CDATA[%1$s]]></Encrypt></xml>";
				String fromXML = String.format(format, encrypt);
				
				try {
					resultStr = pc.decryptMsg(signature, timestamp, nonce, fromXML);
				} catch (Exception e) {
//				e.printStackTrace();
				}
			}
			
			is = new InputSource(new StringReader(resultStr));
			document = db.parse(is);
			root = document.getDocumentElement();
		} catch (Exception e) {
//			e.printStackTrace();
		}
		return root;
	}

	/**
     * 事件提醒
	 * @param toUser 接收者
	 * @param title 事件标题
	 * @param titleColor 标题颜色
	 * @param eventType 事件类型
	 * @param typeColor 类型颜色
	 * @param date 日期
	 * @param dateColor 日期颜色
	 * @param remark 提醒具体内容
	 * @param remarkColor 内容颜色
	 * @param url 链接地址
	 * @return
     */
	public boolean eventRemind(String toUser,
							   String title, String titleColor,
							   String eventType, String typeColor,
							   String date, String dateColor,
							   String remark, String remarkColor,
							   String url) {
		List<TempParamDto> paramList = new ArrayList<>();
		paramList.add(new TempParamDto("first", title, titleColor));
		paramList.add(new TempParamDto("keyword1", eventType, typeColor));
		paramList.add(new TempParamDto("keyword2", date, dateColor));
		paramList.add(new TempParamDto("remark", remark, remarkColor));
		return sendMsg(toUser, wxConfig.getEventTemp(), url, "#FF0000", paramList);
	}

	/**
	 * 事件提醒
	 * @param toUser 接收者
	 * @param title 事件标题
	 * @param eventType 事件类型
	 * @param date 日期
	 * @param remark 提醒具体内容
	 * @param url 链接地址
	 * @return
	 */
	public boolean eventRemind(String toUser,
							   String title,
							   String eventType,
							   String date,
							   String remark,
							   String url) {
		return eventRemind(toUser, title, "#0000FF", eventType, "#888888"
		, date, "#888888", remark, "#666666", url);
	}

	/**
	 * 发送消息
	 * @param toUser 接收方的openid
	 * @param tempId 消息模板id
	 * @param url 链接地址
	 * @param topColor 顶部颜色
	 * @param paramList 参数列表
	 * @return 发送成功返回true，否则返回false
	 */
	public boolean sendMsg(String toUser, String tempId, String url, String topColor, List<TempParamDto> paramList) {
		String res = sendMessage(toUser, tempId, url, topColor, paramList);
		String errcode = JsonTools.getJsonParam(res, "errcode");
		if("0".equals(errcode)) {return true;}
		else if("40003".equals(errcode)) {
			//System.out.println("无效openid："+toUser);
		} else if("40037".equals(errcode)) {
			System.out.println("无效模板Id："+tempId);
		}
		System.out.println("WeixinXmlUtil=="+res);
		return false;
	}

	/**
	 * 发送消息
	 * @param toUser 接收方的openid
	 * @param tempId 消息模板id
	 * @param url 链接地址
	 * @param topColor 顶部颜色
	 * @param paramList 参数列表
	 * @return 返回发送结果
	 */
	public String sendMessage(String toUser, String tempId, String url, String topColor, List<TempParamDto> paramList) {

		String tempUrl = url;
		if(tempUrl!=null && tempUrl.indexOf("{openid}")>=0) {tempUrl = tempUrl.replaceAll("\\{openid\\}", toUser);}

		String postUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+accessTokenTools.getAccessToken();
		String params = createTemplateMessageXml(toUser, tempId, tempUrl, topColor, paramList);
		JSONObject jsonObj = WeixinUtil.httpRequest(postUrl, "POST", params);
		String res = jsonObj==null?"":jsonObj.toString();
		return res;
	}

	/**
	 * 发送消息的XML字符串
	 * @param toUser 接收方的openid
	 * @param tempId 消息模板id
	 * @param url 链接地址
	 * @param topColor 顶部颜色
	 * @param paramList 参数列表
	 * @return
	 */
	public String createTemplateMessageXml(String toUser, String tempId, String url, String topColor, List<TempParamDto> paramList) {
		StringBuffer sb = new StringBuffer();
		sb.append("{");

		sb.append("\"touser\":\"").append(toUser).append("\",").
				append("\"template_id\":\"").append(tempId).append("\",");

		if(url!=null && !"".equals(url.trim())) {
			sb.append("\"url\":\"").append(url).append("\",");
		}

		sb.append("\"topcolor\":\"").append(topColor).append("\",").
				append("\"data\":{");
		int i = 0;
		for(TempParamDto dto : paramList) {
			i++;
			sb.append("\"").append(dto.getParamName()).append("\": {\"value\":\"").append(dto.getValue()).append("\",\"color\":\"").append(dto.getColor()).append("\"}");
			if(i<paramList.size()) {sb.append(",");}
		}

		sb.append("}}");
		return sb.toString();
	}
}
