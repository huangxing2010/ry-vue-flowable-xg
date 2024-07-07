package com.ruoyi.oa.domain.bo;

import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import com.ruoyi.oa.domain.SysOaDetail;
import com.ruoyi.oa.domain.SysOaProject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.time.Instant;
import java.util.Date;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 进出账管理业务对象 sys_oa_finance
 *
 * @author huangxing
 * @date 2024-01-17
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class SysOaFinanceBo extends BaseEntity {

    /**
     * ID
     */
//    @NotNull(message = "ID不能为空", groups = { EditGroup.class })
    private Long financeId;

    /**
     * 项目ID
     */
//    @NotNull(message = "项目ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long projectId;

    /**
     * 账务名称
     */
    @NotBlank(message = "账务名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String financeTitle;

    /**
     * 经手人/付款方
     */
    @NotBlank(message = "经手人/付款方不能为空", groups = { AddGroup.class, EditGroup.class })
    private String financeParties;

    /**
     * 支付类型
     */
    @NotBlank(message = "支付类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String payType;

    /**
     * 进出账类型
     */
    @NotBlank(message = "进出账类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String financeType;

    /**
     * 交易时间
     */
    @NotNull(message = "交易时间不能为空", groups = { AddGroup.class, EditGroup.class })
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
     * 进出账明细
     */
    private List<SysOaDetail> detailList;


}
