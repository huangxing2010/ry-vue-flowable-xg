package com.ruoyi.oa.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 任务管理对象 sys_oa_task
 *
 * @author huangxing
 * @date 2024-01-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_oa_task")
public class SysOaTask extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * ID
     */
    @TableId(value = "task_id")
    private Long taskId;
    /**
     * 项目ID
     */
    private Long projectId;
    /**
     * 任务主题
     */
    private String taskTitle;
    /**
     * 工作类型
     */
    private String taskType;
    /**
     * 优先级
     */
    private String taskGrade;
    /**
     * 协作人员
     */
    private String collaborator;
    /**
     * 开始时间
     */
    private Date beginTime;
    /**
     * 结束时间
     */
    private Date finishTime;
    /**
     * 详细描述
     */
    private String content;
    /**
     * 附件
     */
    private String accessory;
    /**
     * 备注
     */
    private String remark;

}
