package com.ruoyi.oa.domain.bo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.util.Date;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 事件提醒业务对象 sys_oa_remind
 *
 * @author huangxing
 * @date 2024-04-02
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class SysOaRemindBo extends BaseEntity {

    /**
     * 提醒ID
     */
    private Long remindId;

    /**
     * 提醒名称
     */
    @NotBlank(message = "提醒名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String taskTitle;

    /**
     * 事件内容
     */
    @NotBlank(message = "事件内容不能为空", groups = { AddGroup.class, EditGroup.class })
    private String content;

    /**
     * 办理时间
     */
    @NotNull(message = "办理时间不能为空", groups = { AddGroup.class, EditGroup.class })
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
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
