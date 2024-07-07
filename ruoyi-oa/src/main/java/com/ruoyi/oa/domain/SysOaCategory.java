package com.ruoyi.oa.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import com.ruoyi.common.core.domain.TreeEntity;

/**
 * 知识分类对象 sys_oa_category
 *
 * @author huangxing
 * @date 2024-01-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_oa_category")
public class SysOaCategory extends TreeEntity<SysOaCategory> {

    private static final long serialVersionUID=1L;

    /**
     * 产品id
     */
    @TableId(value = "category_id")
    private Long categoryId;
    /**
     * 分类名称
     */
    private String categoryName;
    /**
     * 状态
     */
    private String status;
    /**
     * 备注
     */
    private String remark;

}
