package com.ruoyi.oa.domain.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.common.annotation.ExcelDictFormat;
import com.ruoyi.common.convert.ExcelDictConvert;
import lombok.Data;
import java.util.Date;



/**
 * 任务管理视图对象 sys_oa_task
 *
 * @author huangxing
 * @date 2024-01-11
 */
@Data
@ExcelIgnoreUnannotated
public class SysOaTaskVo {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ExcelProperty(value = "ID")
    private Long taskId;

    /**
     * 项目ID
     */
    @ExcelProperty(value = "项目ID")
    private Long projectId;

    /**
     * 任务主题
     */
    @ExcelProperty(value = "任务主题")
    private String taskTitle;

    /**
     * 工作类型
     */
    @ExcelProperty(value = "工作类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "sys_work_type")
    private String taskType;

    /**
     * 优先级
     */
    @ExcelProperty(value = "优先级")
    private String taskGrade;

    /**
     * 协作人员
     */
    @ExcelProperty(value = "协作人员")
    private String collaborator;

    /**
     * 开始时间
     */
    @ExcelProperty(value = "开始时间")
    private Date beginTime;

    /**
     * 结束时间
     */
    @ExcelProperty(value = "结束时间")
    private Date finishTime;

    /**
     * 详细描述
     */
    @ExcelProperty(value = "详细描述")
    private String content;

    /**
     * 附件
     */
    @ExcelProperty(value = "附件")
    private String accessory;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    /**
     * 创建者
     */
    @ExcelProperty(value = "创建者")
    private String createBy;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;


}
