package com.zslin.wx.paytools;

/**
 * 支付POST方式的DTO对象
 * @author 钟述林  20151206
 *
 */
public class PayPostDto {

	/** 公众号Id */
	private String appid;
	
	/** 商户号 */
	private String mch_id;
	
	/** 设备号 */
	private String device_info;
	
	/** 随机字符串，不超过32位 */
	private String nonce_str;
	
	/** 签名 */
	private String sign;
	
	/** 商品描述 */
	private String body;
	
	/** 附加数据，原样返回 */
	private String attach;
	
	/** 商户订单号，不超过32位，必须唯一 */
	private String out_trade_no;
	
	/** 总金额，单位为分，不能有小数点 */
	private Integer total_fee;
	
	/** 终端IP，H5则使用IP地址 */
	private String spbill_create_ip;
	
	/** 交易起始时间，格式：yyyyMMddHHmmss */
	private String time_start;
	
	/** 交易结束时间，格式：yyyyMMddHHmmss */
	private String time_expire;
	
	/** 商品标记 */
	private String goods_tag;
	
	/** 通知地址，即回调地址 */
	private String notify_url;
	
	/** 交易类型，如：JSAPI、NATIVE、APP */
	private String trade_type;
	
	/** 用户标记，用户Openid，trade_type为JSAPI时，此参数必传 */
	private String openid;
	
	/** 商品ID，只在trade_type为NATIVE时需要填写。此id为二维码中包含的商品ID，商户自行维护。 */
	private String product_id;

	/** 公众号Id */
	public String getAppid() {
		return appid;
	}

	/** 公众号Id */
	public void setAppid(String appid) {
		this.appid = appid;
	}

	/** 商户号 */
	public String getMch_id() {
		return mch_id;
	}

	/** 商户号 */
	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	/** 设备号 */
	public String getDevice_info() {
		return device_info;
	}

	/** 设备号 */
	public void setDevice_info(String device_info) {
		this.device_info = device_info;
	}

	/** 随机字符串，不超过32位 */
	public String getNonce_str() {
		return nonce_str;
	}

	/** 随机字符串，不超过32位 */
	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}

	/** 签名 */
	public String getSign() {
		return sign;
	}

	/** 签名 */
	public void setSign(String sign) {
		this.sign = sign;
	}

	/** 商品描述 */
	public String getBody() {
		return body;
	}

	/** 商品描述 */
	public void setBody(String body) {
		this.body = body;
	}

	/** 附加数据，原样返回 */
	public String getAttach() {
		return attach;
	}

	/** 附加数据，原样返回 */
	public void setAttach(String attach) {
		this.attach = attach;
	}

	/** 商户订单号，不超过32位，必须唯一 */
	public String getOut_trade_no() {
		return out_trade_no;
	}

	/** 商户订单号，不超过32位，必须唯一 */
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	/** 总金额，单位为分，不能有小数点 */
	public Integer getTotal_fee() {
		return total_fee;
	}

	/** 总金额，单位为分，不能有小数点 */
	public void setTotal_fee(Integer total_fee) {
		this.total_fee = total_fee;
	}

	/** 终端IP，H5则使用IP地址 */
	public String getSpbill_create_ip() {
		return spbill_create_ip;
	}

	/** 终端IP，H5则使用IP地址 */
	public void setSpbill_create_ip(String spbill_create_ip) {
		this.spbill_create_ip = spbill_create_ip;
	}

	/** 交易起始时间，格式：yyyyMMddHHmmss */
	public String getTime_start() {
		return time_start;
	}

	/** 交易起始时间，格式：yyyyMMddHHmmss */
	public void setTime_start(String time_start) {
		this.time_start = time_start;
	}

	/** 交易结束时间，格式：yyyyMMddHHmmss */
	public String getTime_expire() {
		return time_expire;
	}

	/** 交易结束时间，格式：yyyyMMddHHmmss */
	public void setTime_expire(String time_expire) {
		this.time_expire = time_expire;
	}

	/** 商品标记 */
	public String getGoods_tag() {
		return goods_tag;
	}

	/** 商品标记 */
	public void setGoods_tag(String goods_tag) {
		this.goods_tag = goods_tag;
	}

	/** 通知地址，即回调地址 */
	public String getNotify_url() {
		return notify_url;
	}

	/** 通知地址，即回调地址 */
	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	/** 交易类型，如：JSAPI、NATIVE、APP */
	public String getTrade_type() {
		return trade_type;
	}

	/** 交易类型，如：JSAPI、NATIVE、APP */
	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}

	/** 用户标记，用户Openid，trade_type为JSAPI时，此参数必传 */
	public String getOpenid() {
		return openid;
	}

	/** 用户标记，用户Openid，trade_type为JSAPI时，此参数必传 */
	public void setOpenid(String openid) {
		this.openid = openid;
	}

	/** 商品ID，只在trade_type为NATIVE时需要填写。此id为二维码中包含的商品ID，商户自行维护。 */
	public String getProduct_id() {
		return product_id;
	}

	/** 商品ID，只在trade_type为NATIVE时需要填写。此id为二维码中包含的商品ID，商户自行维护。 */
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}
	
	public PayPostDto(String device_info, String nonce_str,
			String body, String attach, String out_trade_no, Integer total_fee,
			String spbill_create_ip, String time_start, String time_expire,
			String goods_tag, String trade_type, String openid,
			String product_id) {
		super();
		this.device_info = device_info;
		this.nonce_str = nonce_str;
		this.body = body;
		this.attach = attach;
		this.out_trade_no = out_trade_no;
		this.total_fee = total_fee;
		this.spbill_create_ip = spbill_create_ip;
		this.time_start = time_start;
		this.time_expire = time_expire;
		this.goods_tag = goods_tag;
		this.trade_type = trade_type;
		this.openid = openid;
		this.product_id = product_id;
//		this.appid = WeixinConstant.APP_ID;
//		this.mch_id = WeixinConstant.MCH_ID;
//		this.notify_url = WeixinConstant.NOTICE_URL;
//		this.sign = PaySignTools.createMD5Sign(this);
	}
	
}
