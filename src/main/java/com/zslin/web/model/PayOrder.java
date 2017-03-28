package com.zslin.web.model;

import org.hibernate.annotations.Index;

import javax.persistence.*;

/**
 * 微信交易订单
 * @author 钟述林 20151210
 *
 */
@Entity
@Table(name="w_pay_order")
public class PayOrder {

	private Integer id;
	
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

	/** 只有是就餐订单才需要此字段 */
	@Column(name = "order_no")
	private String orderNo;
	
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
	
	/** 交易类型，如：JSAPI、NATIVE、APP */
	private String trade_type="JSAPI";
	
	/** 用户标记，用户Openid，trade_type为JSAPI时，此参数必传 */
	private String openid;
	
	/** 商品ID，只在trade_type为NATIVE时需要填写。此id为二维码中包含的商品ID，商户自行维护。 */
	private String product_id;
	
	/** 预付款Id，由微信端生成 */
	private String prepay_id;
	
	/** 状态，0：新创建的订单，1：已支付成功；2：用户已取消或作废的订单 */
	private String status;
	
	@Column(name = "create_time")
	private String createTime;

	@Column(name = "create_day")
	private String createDay;

	@Column(name = "create_long")
	private Long createLong;

	private String nickname;

	/** 电话 */
	private String phone;

	/** 头像 */
	private String headimg;

	@Column(name = "account_id")
	private Integer accountId;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getHeadimg() {
		return headimg;
	}

	public void setHeadimg(String headimg) {
		this.headimg = headimg;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCreateDay() {
		return createDay;
	}

	public void setCreateDay(String createDay) {
		this.createDay = createDay;
	}

	public Long getCreateLong() {
		return createLong;
	}

	public void setCreateLong(Long createLong) {
		this.createLong = createLong;
	}

	/** 预付款Id，由微信端生成 */
	public String getPrepay_id() {
		return prepay_id;
	}

	/** 预付款Id，由微信端生成 */
	public void setPrepay_id(String prepay_id) {
		this.prepay_id = prepay_id;
	}

	/** 状态，0：新创建的订单，1：已支付成功；2：用户已取消或作废的订单 */
	public String getStatus() {
		return status;
	}

	/** 状态，0：新创建的订单，1：已支付成功；2：用户已取消或作废的订单 */
	public void setStatus(String status) {
		this.status = status;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	/** 用户昵称 */
	public String getNickname() {
		return nickname;
	}

	/** 用户昵称 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
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
	@Index(name="out_trade_no")
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

	/** 交易类型，如：JSAPI、NATIVE、APP */
	public String getTrade_type() {
		return trade_type;
	}

	/** 交易类型，如：JSAPI、NATIVE、APP */
	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}

	/** 用户标记，用户Openid，trade_type为JSAPI时，此参数必传 */
	@Index(name="openid")
	public String getOpenid() {
		return openid;
	}

	/** 用户标记，用户Openid，trade_type为JSAPI时，此参数必传 */
	public void setOpenid(String openid) {
		this.openid = openid;
	}

	/** 商品ID，只在trade_type为NATIVE时需要填写。此id为二维码中包含的商品ID，商户自行维护。 */
	@Index(name="product_id")
	public String getProduct_id() {
		return product_id;
	}

	/** 商品ID，只在trade_type为NATIVE时需要填写。此id为二维码中包含的商品ID，商户自行维护。 */
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}
}
