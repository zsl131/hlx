package com.zslin.basic.qiniu.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 七牛配置
 * @author 钟述林
 * @data generate on: 2020-09-04
 */
@Data
@Entity
@Table(name = "qiniu_qiniu_config")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QiniuConfig implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	
	/**
	* 在七牛控制台在查看
	*/
	private String accessKey;

	/**
	* 在七牛控制台在查看
	*/
	private String secretKey;

	/**
	* domain文件域名
	*/
	private String url;

	private String bucketName;
}
