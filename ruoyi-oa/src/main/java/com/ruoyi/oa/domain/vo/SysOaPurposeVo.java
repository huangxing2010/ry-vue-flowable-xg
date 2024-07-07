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
 * 采购意向视图对象 sys_oa_purpose
 *
 * @author huangxing
 * @date 2024-03-31
 */
@Data
@ExcelIgnoreUnannotated
public class SysOaPurposeVo {

    private static final long serialVersionUID = 1L;

    /**
     * 项目id
     */
    @ExcelProperty(value = "项目id")
    private Long purposeId;

    /**
     * 项目名称
     */
    @ExcelProperty(value = "项目名称")
    private String purposeTitle;

    /**
     * 采购单位
     */
    @ExcelProperty(value = "采购单位")
    private String subhead;

    /**
     * 预算金额
     */
    @ExcelProperty(value = "预算金额")
    private String budget;

    /**
     * 采购需求情况
     */
    @ExcelProperty(value = "采购需求情况")
    private String content;

    /**
     * 预计采购时间
     */
    @ExcelProperty(value = "预计采购时间")
    private String purposeTime;

    /**
     * 是否投标
     */
    @ExcelProperty(value = "是否投标", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "sys_yes_no")
    private String isBid;

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
