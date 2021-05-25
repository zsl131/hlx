package com.zslin.weixin.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;


/**
 * 微信模板消息
 * @author 钟述林
 * @data generate on: 2020-09-04
 */
@Data
@Entity
@Table(name = "wx_template_message_relation")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TemplateMessageRelation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	
	/**
	* 模板名称
	* @remark 与本系统中名称一一对应
	*/
	private String templateName;

	/**
	* 名称拼音
	*/
	private String templatePinyin;

	/**
	* 模板ID
	* @remark 与微信平台模板消息一一对应
	*/
	private String templateId;

	/**
	* 键值对
	* @remark 如：反馈日期=keyword1-回复内容=keyword2-备注=remark
	*/
	@Lob
	private String keyValues;

}
