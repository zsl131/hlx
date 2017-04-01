package com.zslin.wx.tools;

import com.qq.weixin.mp.aes.WXBizMsgCrypt;
import com.zslin.basic.tools.NormalTools;
import com.zslin.basic.tools.PinyinToolkit;
import com.zslin.web.model.EventRecord;
import com.zslin.web.model.TemplateMessage;
import com.zslin.web.model.WeixinConfig;
import com.zslin.web.service.IEventRecordService;
import com.zslin.web.service.ITemplateMessageService;
import com.zslin.wx.dto.EventRemarkDto;
import com.zslin.wx.dto.TempParamDto;
import com.zslin.wx.dto.TemplateParamDto;
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
import java.util.Date;
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

	@Autowired
	private IEventRecordService eventRecordService;

	@Autowired
	private ITemplateMessageService templateMessageService;

	@Autowired
	private SignTools signTools;

	/**
	 * 获取事件消息的元素对象
	 * @param request
	 * @return
	 */
	public Element getMessageEle(HttpServletRequest request) {
		WeixinConfig config = wxConfig.getConfig();
		Element root = null;
		try {
			String signature = request.getParameter("signature"); //微信加密签名
			String timestamp = request.getParameter("timestamp"); //时间戳
			String nonce = request.getParameter("nonce"); //随机数

			boolean check = signTools.checkSignature(signature, timestamp, nonce);
			if(!check) {return null;} //如果验证不通过
			WXBizMsgCrypt pc = new WXBizMsgCrypt(config.getToken(), config.getAeskey(), config.getAppid());
			
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
				e.printStackTrace();
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
	 * @param users 接收者
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
	public boolean eventRemind(List<String> users,
							   String title, String titleColor,
							   String eventType, String typeColor,
							   String date, String dateColor,
							   String remark, String remarkColor,
							   String url) {
		for(String toUser : users) {
			eventRemind(toUser, title, titleColor, eventType, typeColor, date, dateColor, remark, remarkColor, url);
		}
		return true;
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

		//添加事件推送记录
		addEventRecord(toUser, title, eventType, date, remark, url);

		return sendMsg(toUser, wxConfig.getConfig().getEventTemp(), url, "#FF0000", paramList);
	}

	public boolean sendTemplateMessage(String tempTitle, String toUser, String title, String titleColor, String url, TemplateParamDto ... dtos) {
		String titleCode = PinyinToolkit.cn2Spell(tempTitle,"");
		TemplateMessage tm = templateMessageService.findByCode(titleCode);

		String templateId = tm==null?wxConfig.getConfig().getEventTemp():tm.getTempId();
		List<TempParamDto> paramList = buildParamDtos(tm==null?3:tm.getParLen(), title, titleColor, dtos);
		//添加事件推送记录
		addEventRecord(toUser, title, title, NormalTools.curDate(), title, url);

		return sendMsg(toUser, templateId, url, "#FF0000", paramList);
	}

	/**
	 * 通用事件通知方法
	 * 参数的个数由dtos决定
	 * @param parLen 参数列表长度，除first外的，包含remark
	 * @param title 标题，即第一个参数
	 * @param titleColor 标题颜色，不输入有默认值
	 * @param dtos 参数列表，最后一个是remark可以传多个值
	 * @return
	 */
	private List<TempParamDto> buildParamDtos(Integer parLen, String title, String titleColor, TemplateParamDto ... dtos) {
		List<TempParamDto> paramList = new ArrayList<>();
		paramList.add(new TempParamDto("first", title, titleColor==null||"".equalsIgnoreCase(titleColor)?"#888888":titleColor));
		if(parLen<dtos.length) {
			for(int i=0;i<parLen-1;i++) {
				TemplateParamDto dto = dtos[i];
				String color = dto.getColor();
				color = color==null ||"".equalsIgnoreCase(color.trim())?"#888888":color;
				paramList.add(new TempParamDto("keyword"+(i+1), dto.getValue(), color));
			}
			StringBuffer sb = new StringBuffer();
			for(int i=parLen; i<dtos.length-1;i++) {
				TemplateParamDto dto = dtos[i];
				sb.append(dto.getParamName()).append((dto.getParamName()==null || "".equals(dto.getParamName()))?"":"：").append(dto.getValue()).append("\\n");
			}
			TemplateParamDto remarkDto = dtos[dtos.length-1];
			String color = remarkDto.getColor();
			String pName = remarkDto.getParamName();
			sb.append(pName==null||"".equalsIgnoreCase(pName)?"":pName).append((pName==null || "".equals(pName))?"":"：").append(remarkDto.getValue()).append("\\n");
			color = color==null ||"".equalsIgnoreCase(color.trim())?"#888888":color;
			paramList.add(new TempParamDto("remark", sb.toString(), color));
		} else {
			for(int i=0;i<dtos.length-1;i++) {
				TemplateParamDto dto = dtos[i];
				String color = dto.getColor();
				color = color==null ||"".equalsIgnoreCase(color.trim())?"#888888":color;
				paramList.add(new TempParamDto("keyword"+(i+1), dto.getValue(), color));
			}
			TemplateParamDto remarkDto = dtos[dtos.length-1];
			String color = remarkDto.getColor();
			color = color==null ||"".equalsIgnoreCase(color.trim())?"#888888":color;
			paramList.add(new TempParamDto("remark", remarkDto.getValue(), color));
		}
		return paramList;
	}

	/**
	 * 添加事件通知记录
	 * @param openid
	 * @param title
	 * @param titleRemark
	 * @param eventDate
	 * @param remark
	 * @param url
	 */
	private void addEventRecord(String openid, String title, String titleRemark,
								String eventDate, String remark, String url) {
		EventRecord er = new EventRecord();
		er.setOpenid(openid);
		er.setEventDate(eventDate);
		er.setRemark(remark);
		er.setTitle(title);
		er.setTitleRemark(titleRemark);
		er.setUrl(url);
		er.setCreateTime(NormalTools.curDate("yyyy-MM-dd HH:mm:ss"));
		er.setCreateDay(NormalTools.curDate("yyyy-MM-dd"));
		er.setCreateDate(new Date());
		er.setCreateLong(System.currentTimeMillis());

		eventRecordService.save(er);
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

	public boolean eventRemind(String toUser, String title, String eventType, String date, String url, EventRemarkDto... dtos) {
		StringBuffer sb = new StringBuffer();
		for(EventRemarkDto dto : dtos) {
			sb.append(dto.getName()).append((dto.getName()==null || "".equals(dto.getName()))?"":"：").append(dto.getValue()).append("\\n");
		}
		return eventRemind(toUser, title, eventType, date, sb.toString(), url);
	}

	public boolean eventRemind(List<String> users, String title, String eventType, String date, String url, EventRemarkDto... dtos) {
		StringBuffer sb = new StringBuffer();
		for(EventRemarkDto dto : dtos) {
			sb.append(dto.getName()).append((dto.getName()==null || "".equals(dto.getName()))?"":"：").append(dto.getValue()).append("\\n");
		}
		return eventRemind(users, title, eventType, date, sb.toString(), url);
	}

	/**
	 * 事件提醒
	 * @param users 接收者
	 * @param title 事件标题
	 * @param eventType 事件类型
	 * @param date 日期
	 * @param remark 提醒具体内容
	 * @param url 链接地址
	 * @return
	 */
	public boolean eventRemind(List<String> users,
							   String title,
							   String eventType,
							   String date,
							   String remark,
							   String url) {
		for(String toUser : users) {
			eventRemind(toUser, title, eventType, date, remark, url);
		}
		return true;
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
			if(!url.toLowerCase().startsWith("http://") && !url.toLowerCase().startsWith("https://")) {
				url = wxConfig.getConfig().getUrl()+url;
			}
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
