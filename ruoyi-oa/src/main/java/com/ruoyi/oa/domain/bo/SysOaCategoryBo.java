package com.ruoyi.oa.domain.bo;

import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.util.Date;

import com.ruoyi.common.core.domain.TreeEntity;

/**
 * 知识分类业务对象 sys_oa_category
 *
 * @author huangxing
 * @date 2024-01-17
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class SysOaCategoryBo extends TreeEntity<SysOaCategoryBo> {

    /**
     * 产品id
     */
    @NotNull(message = "产品id不能为空", groups = { EditGroup.class })
    private Long categoryId;

    /**
     * 分类名称
     */
    @NotBlank(message = "分类名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String categoryName;

    /**
     * 状态
     */
    @NotBlank(message = "状态不能为空", groups = { AddGroup.class, EditGroup.class })
    private String status;

    /**
     * 备注
     */
    private String remark;


}
