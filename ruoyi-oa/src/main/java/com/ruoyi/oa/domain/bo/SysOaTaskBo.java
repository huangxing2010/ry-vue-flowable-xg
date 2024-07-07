package com.ruoyi.oa.domain.bo;

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
 * 任务管理业务对象 sys_oa_task
 *
 * @author huangxing
 * @date 2024-01-11
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class SysOaTaskBo extends BaseEntity {

    /**
     * ID
     */
    @NotNull(message = "ID不能为空", groups = { EditGroup.class })
    private Long taskId;

    /**
     * 项目ID
     */
    @NotNull(message = "项目ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long projectId;

    /**
     * 任务主题
     */
    @NotBlank(message = "任务主题不能为空", groups = { AddGroup.class, EditGroup.class })
    private String taskTitle;

    /**
     * 工作类型
     */
    @NotBlank(message = "工作类型不能为空", groups = { AddGroup.class, EditGroup.class })
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
    @NotNull(message = "开始时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date beginTime;

    /**
     * 结束时间
     */
    @NotNull(message = "结束时间不能为空", groups = { AddGroup.class, EditGroup.class })
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
