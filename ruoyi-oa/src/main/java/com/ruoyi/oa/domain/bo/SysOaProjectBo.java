package com.ruoyi.oa.domain.bo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.math.BigDecimal;
import java.util.Date;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 项目管理业务对象 sys_oa_project
 *
 * @author huangxing
 * @date 2024-01-11
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class SysOaProjectBo extends BaseEntity {

    /**
     * ID
     */
    @NotNull(message = "ID不能为空", groups = { EditGroup.class })
    private Long projectId;

    /**
     * 项目名称
     */
    @NotBlank(message = "项目名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String projectName;

    /**
     * 项目编号
     */
    @NotBlank(message = "项目编号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String projectNum;

    /**
     * 项目类型
     */
//    @NotBlank(message = "项目类型不能为空", groups = { AddGroup.class, EditGroup.class })
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
    @NotBlank(message = "项目负责人不能为空", groups = { AddGroup.class, EditGroup.class })
    private String functionary;

    /**
     * 开始日期
     */
    @NotNull(message = "开始日期不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date beginTime;

    /**
     * 结束日期
     */
    @NotNull(message = "结束日期不能为空", groups = { AddGroup.class, EditGroup.class })
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
//    @NotNull(message = "关联合同ID不能为空", groups = { AddGroup.class, EditGroup.class })
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
