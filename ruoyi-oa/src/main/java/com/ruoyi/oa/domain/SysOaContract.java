package com.ruoyi.oa.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 合同管理对象 sys_oa_contract
 *
 * @author huangxing
 * @date 2024-01-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_oa_contract")
public class SysOaContract extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 合同ID
     */
    @TableId(value = "contract_id")
    private Long contractId;
    /**
     * 项目id
     */
    private Long projectId;
    /**
     * 合同编号
     */
    private String contractNum;
    /**
     * 项目名称
     */
    private String contractName;
    /**
     * 甲方名称
     */
    private String firstName;
    /**
     * 甲方负责人
     */
    private String firstPerson;
    /**
     * 甲方电话
     */
    private String firstPhone;
    /**
     * 乙方名称
     */
    private String secondName;
    /**
     * 乙方负责人
     */
    private String secondPerson;
    /**
     * 乙方电话
     */
    private String secondPhone;
    /**
     * 合同金额
     */
    private String contractPrice;
    /**
     * 签订日期
     */
    private Date signTime;
    /**
     * 有效期
     */
    private String validity;

    /**
     * 类型
     */
    private String contractType;

    /**
     * 状态
     */
    private String contractStatus;
    /**
     * 附件
     */
    private String accessory;
    /**
     * 备注
     */
    private String remark;

}
