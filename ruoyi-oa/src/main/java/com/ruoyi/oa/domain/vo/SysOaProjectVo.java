package com.ruoyi.oa.domain.vo;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.common.annotation.ExcelDictFormat;
import com.ruoyi.common.convert.ExcelDictConvert;
import lombok.Data;
import java.util.Date;



/**
 * 项目管理视图对象 sys_oa_project
 *
 * @author huangxing
 * @date 2024-01-11
 */
@Data
@ExcelIgnoreUnannotated
public class SysOaProjectVo {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ExcelProperty(value = "ID")
    private Long projectId;

    /**
     * 项目名称
     */
    @ExcelProperty(value = "项目名称")
    private String projectName;

    /**
     * 项目编号
     */
    @ExcelProperty(value = "项目编号")
    private String projectNum;

    /**
     * 项目类型
     */
    @ExcelProperty(value = "项目类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "sys_project_type")
    private String projectType;

    /**
     * 项目地址
     */
    @ExcelProperty(value = "项目地址")
    private String address;

    /**
     * 项目总款
     */
    @ExcelProperty(value = "项目总款")
    private BigDecimal funds;

    /**
     * 项目负责人
     */
    @ExcelProperty(value = "项目负责人")
    private String functionary;

    /**
     * 开始日期
     */
    @ExcelProperty(value = "开始日期")
    private Date beginTime;

    /**
     * 结束日期
     */
    @ExcelProperty(value = "结束日期")
    private Date finishTime;

    /**
     * 交货期
     */
    @ExcelProperty(value = "交货期")
    private String delivery;
    /**
     * 质保期
     */
    @ExcelProperty(value = "质保期")
    private String guarantee;

    /**
     * 项目介绍
     */
    @ExcelProperty(value = "项目介绍")
    private String introduction;

    /**
     * 优先级
     */
    @ExcelProperty(value = "优先级", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "sys_sort_grade")
    private String projectGrade;

    /**
     * 状态
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "sys_project_status")
    private String projectStatus;

    /**
     * 合同ID
     */
    @ExcelProperty(value = "关联合同", converter = ExcelDictConvert.class)
    private Long contractId;
    /**
     * 开票名称
     */
    @ExcelProperty(value = "开票名称")
    private String invoiceName;
    /**
     * 纳税人识别号
     */
    @ExcelProperty(value = "纳税人识别号")
    private String invoiceNumber;
    /**
     * 地址电话
     */
    @ExcelProperty(value = "地址电话")
    private String invoiceAddress;
    /**
     * 开户行及账号
     */
    @ExcelProperty(value = "开户行及账号")
    private String invoiceBank;
    /**
     * 履约保证金
     */
    @ExcelProperty(value = "履约保证金")
    private String bail;
    /**
     * 附件
     */
    @ExcelProperty(value = "附件")
    private String accessory;

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
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;


}
