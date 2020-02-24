package com.zslin.basic.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 系统配置
 * @author zslin.com 20160519
 *
 */
@Entity
@Table(name="a_app_config")
@Data
public class AppConfig implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	/** 系统名称 */
	@Column(name="app_name")
	private String appName;
	
	/** 当前版本 */
	@Column(name="app_version")
	private String appVersion;

	/** 创建日期 */
	@Column(name="create_date")
	private String createDate;
	
	/** 初始化标记，如果为空或为0，表示都可以初始化 */
	@Column(name="init_flag")
	private String initFlag;
	
	/** 首页路径 */
	@Column(name="index_page")
	private String indexPage;
	
	/** 页末联系人 */
	private String contant;

	/** 管理员邮箱 */
	@Column(name = "admin_email")
	private String adminEmail;

	/** 微信支付，1-开启；0-未开启 */
	@Column(name = "wx_pay")
	private String wxPay;

	/** 联系地址 */
	private String address;

	/** 联系电话 */
	private String phone;

	/** 纬度 */
	private String latitude;

	/** 经度 */
	private String longitude;
}
