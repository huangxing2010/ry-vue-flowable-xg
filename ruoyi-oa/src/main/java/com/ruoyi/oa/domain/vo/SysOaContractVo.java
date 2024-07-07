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
 * 合同管理视图对象 sys_oa_contract
 *
 * @author huangxing
 * @date 2024-01-10
 */
@Data
@ExcelIgnoreUnannotated
public class SysOaContractVo {

    private static final long serialVersionUID = 1L;

    /**
     * 合同ID
     */
    @ExcelProperty(value = "合同ID")
    private Long contractId;

    /**
     * 项目ID
     */
    @ExcelProperty(value = "项目ID")
    private Long projectId;


    /**
     * 合同编号
     */
    @ExcelProperty(value = "合同编号")
    private String contractNum;

    /**
     * 项目名称
     */
    @ExcelProperty(value = "项目名称")
    private String contractName;

    /**
     * 甲方名称
     */
    @ExcelProperty(value = "甲方名称")
    private String firstName;

    /**
     * 甲方负责人
     */
    @ExcelProperty(value = "甲方负责人")
    private String firstPerson;

    /**
     * 甲方电话
     */
    @ExcelProperty(value = "甲方电话")
    private String firstPhone;

    /**
     * 乙方名称
     */
    @ExcelProperty(value = "乙方电话")
    private String secondName;
    /**
     * 乙方负责人
     */
    @ExcelProperty(value = "乙方负责人")
    private String secondPerson;
    /**
     * 乙方电话
     */
    @ExcelProperty(value = "乙方电话")
    private String secondPhone;

    /**
     * 合同金额
     */
    @ExcelProperty(value = "合同金额")
    private String contractPrice;

    /**
     * 签订日期
     */
    @ExcelProperty(value = "签订日期")
    private Date signTime;
    /**
     * 有效期
     */
    @ExcelProperty(value = "有效期")
    private String validity;

    /**
     * 类型
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    private String contractType;

    /**
     * 状态
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "sys_show_hide")
    private String contractStatus;

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
     * 附件
     */
    @ExcelProperty(value = "附件")
    private String accessory;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;


}
