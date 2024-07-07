package com.ruoyi.oa.domain.bo;

import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.util.Date;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 采购意向业务对象 sys_oa_purpose
 *
 * @author huangxing
 * @date 2024-03-31
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class SysOaPurposeBo extends BaseEntity {

    /**
     * 项目id
     */
    @NotNull(message = "项目id不能为空", groups = { EditGroup.class })
    private Long purposeId;

    /**
     * 项目名称
     */
    @NotBlank(message = "项目名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String purposeTitle;

    /**
     * 采购单位
     */
    @NotBlank(message = "采购单位不能为空", groups = { AddGroup.class, EditGroup.class })
    private String subhead;

    /**
     * 预算金额
     */
    @NotBlank(message = "预算金额不能为空", groups = { AddGroup.class, EditGroup.class })
    private String budget;

    /**
     * 采购需求情况
     */
    @NotBlank(message = "采购需求情况不能为空", groups = { AddGroup.class, EditGroup.class })
    private String content;

    /**
     * 预计采购时间
     */
    @NotNull(message = "预计采购时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private String purposeTime;

    /**
     * 是否投标
     */
    private String isBid;

    /**
     * 附件
     */
    private String accessory;

    /**
     * 备注
     */
    private String remark;


}
