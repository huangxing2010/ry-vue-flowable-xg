package com.ruoyi.oa.domain.bo;

import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.util.Date;

import java.math.BigDecimal;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 进出账明细业务对象 sys_oa_detail
 *
 * @author huangxing
 * @date 2024-01-17
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class SysOaDetailBo extends BaseEntity {

    /**
     * ID
     */
    private Long detailId;

    /**
     * 进出账ID
     */
    @NotNull(message = "进出账ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long financeId;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String detailTitle;

    /**
     * 金额
     */
    @NotNull(message = "金额不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal price;

    /**
     * 大写金额
     */
    @NotNull(message = "大写金额不能为空", groups = { AddGroup.class, EditGroup.class })
    private String bigPrice;


    /**
     * 备注
     */
    private String remark;


}
