package com.ruoyi.oa.domain.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.common.annotation.ExcelDictFormat;
import com.ruoyi.common.convert.ExcelDictConvert;
import com.ruoyi.oa.domain.SysOaDetail;
import com.ruoyi.oa.domain.SysOaProject;
import lombok.Data;
import java.util.Date;
import java.util.List;


/**
 * 进出账管理视图对象 sys_oa_finance
 *
 * @author huangxing
 * @date 2024-01-17
 */
@Data
@ExcelIgnoreUnannotated
public class SysOaFinanceVo {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ExcelProperty(value = "ID")
    private Long financeId;

    /**
     * 项目ID
     */
    @ExcelProperty(value = "项目ID")
    private Long projectId;


    /**
     * 账务名称
     */
    @ExcelProperty(value = "账务名称")
    private String financeTitle;

    /**
     * 经手人/付款方
     */
    @ExcelProperty(value = "经手人/付款方")
    private String financeParties;

    /**
     * 支付类型
     */
    @ExcelProperty(value = "支付类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "sys_pay_type")
    private String payType;

    /**
     * 进出账类型
     */
    @ExcelProperty(value = "进出账类型")
    private String financeType;

    /**
     * 交易时间
     */
    @ExcelProperty(value = "交易时间")
    private Date financeTime;



    /**
     * 开票比例
     */
    @ExcelProperty(value = "开票比例")
    private String makeRatio;
    /**
     * 开票时间
     */
    @ExcelProperty(value = "开票时间")
    private String makeTime;

    /**
     * 开票金额
     */
    @ExcelProperty(value = "开票金额")
    private String makePrice;

    /**
     * 开票情况说明
     */
    @ExcelProperty(value = "开票情况说明")
    private String makeExplain;

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

    /**
     * 一对一关联项目表
     */
    private SysOaProject project;

    /**
     * 一对多关联进出账明细
     */
    private List<SysOaDetail> detailList;


}
