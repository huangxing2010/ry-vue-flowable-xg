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
 * 事件提醒视图对象 sys_oa_remind
 *
 * @author huangxing
 * @date 2024-04-02
 */
@Data
@ExcelIgnoreUnannotated
public class SysOaRemindVo {

    private static final long serialVersionUID = 1L;

    /**
     * 提醒ID
     */
    @ExcelProperty(value = "提醒ID")
    private Long remindId;

    /**
     * 提醒名称
     */
    @ExcelProperty(value = "提醒名称")
    private String taskTitle;

    /**
     * 事件内容
     */
    @ExcelProperty(value = "事件内容")
    private String content;

    /**
     * 办理时间
     */
    @ExcelProperty(value = "办理时间")
    private Date taskTime;

    /**
     * 办理状态
     */
    @ExcelProperty(value = "办理状态")
    private String taskStatus;

    /**
     * 办理情况
     */
    @ExcelProperty(value = "办理情况")
    private String manage;

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

    /**
     * 修改者
     */
    @ExcelProperty(value = "修改者")
    private String updateBy;

    /**
     * 修改时间
     */
    @ExcelProperty(value = "修改时间")
    private Date updateTime;




}
