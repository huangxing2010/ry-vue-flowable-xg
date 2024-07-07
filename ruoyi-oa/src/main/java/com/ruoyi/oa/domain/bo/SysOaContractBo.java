package com.ruoyi.oa.domain.bo;

import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.util.Date;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 合同管理业务对象 sys_oa_contract
 *
 * @author huangxing
 * @date 2024-01-10
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class SysOaContractBo extends BaseEntity {

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
    @NotBlank(message = "项目名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String contractName;

    /**
     * 甲方名称
     */
    @NotBlank(message = "甲方名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String firstName;

    /**
     * 甲方负责人
     */
    @NotBlank(message = "甲方负责人不能为空", groups = { AddGroup.class, EditGroup.class })
    private String firstPerson;

    /**
     * 甲方电话
     */
    @NotBlank(message = "甲方电话不能为空", groups = { AddGroup.class, EditGroup.class })
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
