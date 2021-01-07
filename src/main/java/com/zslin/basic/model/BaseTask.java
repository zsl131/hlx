package com.zslin.basic.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity
@Table(name = "base_task")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String taskUuid;

    /** 任务描述 */
    private String taskDesc;

    @NotBlank(message = "beanName不能为空")
    private String beanName;

    @NotBlank(message = "methodName不能为空")
    private String methodName;

    /** 执行规则 */
    private String cron;

    /** 状态，1-启用；0-停用 */
    private String status = "0";

    /** 参数 */
    @Lob
    private String params;

    /**
     * 任务类型
     *  1-单次任务
     *  2-循环任务
     *  3-cron规则任务
     */
    @NotBlank(message = "任务类型不能为空")
    private String type;

    /**
     * 开始时间，格式：yyyy-MM-dd HH:mm:ss
     */
    private String startTime;

    /**
     * 下一次执行间隔时间，单位秒
     */
    private Long period;

    /** 是否等待上一次任务执行完成，1-是；0-否 */
    private String isWait="0";

    private String createDay;

    private String createTime;

    private Long createLong;

    private Integer sucCount = 0;
    private Integer errCount = 0;

    public static final String TYPE_SINGLE = "1";
    public static final String TYPE_LOOP = "2";
    public static final String TYPE_CRON = "3";
}
