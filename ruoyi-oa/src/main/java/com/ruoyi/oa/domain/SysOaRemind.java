package com.ruoyi.oa.domain;

import com.alibaba.excel.annotation.ExcelProperty;
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
 * 事件提醒对象 sys_oa_remind
 *
 * @author huangxing
 * @date 2024-04-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_oa_remind")
public class SysOaRemind extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 提醒ID
     */
    @TableId(value = "remind_id")
    private Long remindId;
    /**
     * 提醒名称
     */
    private String taskTitle;
    /**
     * 事件内容
     */
    private String content;
    /**
     * 办理时间
     */
    private Date taskTime;
    /**
     * 办理状态
     */
    private String taskStatus;

    /**
     * 办理情况
     */
    private String manage;
    /**
     * 附件
     */
    private String accessory;
    /**
     * 备注
     */
    private String remark;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改者
     */
    private String updateBy;

    /**
     * 修改时间
     */
    private Date updateTime;

}
