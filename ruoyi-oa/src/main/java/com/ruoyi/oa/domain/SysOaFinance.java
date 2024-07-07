package com.ruoyi.oa.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 进出账管理对象 sys_oa_finance
 *
 * @author huangxing
 * @date 2024-01-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_oa_finance")
public class SysOaFinance extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * ID
     */
    @TableId(value = "finance_id")
    private Long financeId;
    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 账务名称
     */
    private String financeTitle;
    /**
     * 经手人/付款方
     */
    private String financeParties;
    /**
     * 支付类型
     */
    private String payType;
    /**
     * 进出账类型
     */
    private String financeType;
    /**
     * 交易时间
     */
    private Date financeTime;

    /**
     * 开票比例
     */
    private String makeRatio;
    /**
     * 开票时间
     */
    private String makeTime;

    /**
     * 开票金额
     */
    private String makePrice;

    /**
     * 开票情况说明
     */
    private String makeExplain;

    /**
     * 附件
     */
    private String accessory;
    /**
     * 备注
     */
    private String remark;

    /**
     * 一对一关联项目表
     */
    private SysOaProject project;

    /**
     * 一对多关联进出账明细
     */
    @TableField(exist = false)
    private List<SysOaDetail> detailList;

}
