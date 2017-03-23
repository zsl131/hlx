package com.zslin.wx.paytools;

/**
 * 微信支付结果DTO对象
 * @author 钟述林 20151206
 *
 */
public class PayResultDto {

	/** 返回代码，如SUCCESS、FAIL */
	private String return_code;
	
	/** 返回信息，如果不为空，则表示提交失败 */
	private String return_msg;
	
	/** 结果代码 */
	private String result_code;
	
	/** APPId */
	private String appid;
	
	/** 商户Id */
	private String mch_id;
	
	/** 设备信息 */
	private String device_info;
	
	/** 随机字符串 */
	private String nonce_str;
	
	/** 签名 */
	private String sign;

	/** 错误代码 */
	private String err_code;
	
	/** 错误描述 */
	private String err_code_des;
	
	/** 订单号 */
	private String prepay_id;
	
	/** 类型 */
	private String trade_type;

	/** trade_type为NATIVE时有返回，用于生成二维码，展示给用户进行扫码支付 */
	private String code_url;

	public String getCode_url() {
		return code_url;
	}

	public void setCode_url(String code_url) {
		this.code_url = code_url;
	}

	public String getPrepay_id() {
		return prepay_id;
	}

	public void setPrepay_id(String prepay_id) {
		this.prepay_id = prepay_id;
	}

	public String getTrade_type() {
		return trade_type;
	}

	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getMch_id() {
		return mch_id;
	}

	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public String getDevice_info() {
		return device_info;
	}

	public void setDevice_info(String device_info) {
		this.device_info = device_info;
	}

	public String getNonce_str() {
		return nonce_str;
	}

	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getErr_code() {
		return err_code;
	}

	public void setErr_code(String err_code) {
		this.err_code = err_code;
	}

	public String getErr_code_des() {
		return err_code_des;
	}

	public void setErr_code_des(String err_code_des) {
		this.err_code_des = err_code_des;
	}

	/** 返回代码，如SUCCESS、FAIL */
	public String getReturn_code() {
		return return_code;
	}

	/** 返回代码，如SUCCESS、FAIL */
	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}

	/** 返回信息，如果不为空，则表示提交失败 */
	public String getReturn_msg() {
		return return_msg;
	}

	/** 返回信息，如果不为空，则表示提交失败 */
	public void setReturn_msg(String return_msg) {
		this.return_msg = return_msg;
	}

	/** 结果代码 */
	public String getResult_code() {
		return result_code;
	}

	/** 结果代码 */
	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}

	@Override
	public String toString() {
		return "PayResultDto [return_code=" + return_code + ", return_msg="
				+ return_msg + ", result_code=" + result_code + ", appid="
				+ appid + ", mch_id=" + mch_id + ", device_info=" + device_info
				+ ", nonce_str=" + nonce_str + ", sign=" + sign + ", err_code="
				+ err_code + ", err_code_des=" + err_code_des + ", prepay_id="
				+ prepay_id + ", trade_type=" + trade_type + "]";
	}
	
}
