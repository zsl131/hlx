package com.zslin.wx.dto;

/**
 * 消息模板参数DTO对象
 * @author 钟述林 20150705
 *
 */
public class TempParamDto {

	/** 参数名称 */
	private String paramName;
	
	/** 对应值 */
	private String value;
	
	/** 对应颜色 */
	private String color;

	public TempParamDto() {}
	
	public TempParamDto(String paramName, String value, String color) {
		super();
		this.paramName = paramName;
		this.value = value;
		this.color = color;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
}
