package com.ruoyi.oa.domain;

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
 * 项目管理对象 sys_oa_project
 *
 * @author huangxing
 * @date 2024-01-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_oa_project")
public class SysOaProject extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * ID
     */
    @TableId(value = "project_id")
    private Long projectId;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 项目编号
     */
    private String projectNum;
    /**
     * 项目类型
     */
    private String projectType;
    /**
     * 项目地址
     */
    private String address;

    /**
     * 项目总款
     */
    private BigDecimal funds;
    /**
     * 项目负责人
     */
    private String functionary;
    /**
     * 开始日期
     */
    private Date beginTime;
    /**
     * 结束日期
     */
    private Date finishTime;
    /**
     * 交货期
     */
    private String delivery;
    /**
     * 质保期
     */
    private String guarantee;
    /**
     * 项目介绍
     */
    private String introduction;
    /**
     * 优先级
     */
    private String projectGrade;
    /**
     * 状态
     */
    private String projectStatus;
    /**
     * 关联合同ID
     */
    private Long contractId;
    /**
     * 开票名称
     */
    private String invoiceName;
    /**
     * 纳税人识别号
     */
    private String invoiceNumber;
    /**
     * 地址电话
     */
    private String invoiceAddress;
    /**
     * 开户行及账号
     */
    private String invoiceBank;
    /**
     * 履约保证金
     */
    private String bail;
    /**
     * 附件
     */
    private String accessory;
    /**
     * 备注
     */
    private String remark;

}
