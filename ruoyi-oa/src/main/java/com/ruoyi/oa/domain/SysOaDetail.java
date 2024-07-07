package com.ruoyi.oa.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import java.math.BigDecimal;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 进出账明细对象 sys_oa_detail
 *
 * @author huangxing
 * @date 2024-01-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_oa_detail")
public class SysOaDetail extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * ID
     */
    @TableId(value = "detail_id")
    private Long detailId;
    /**
     * 进出账ID
     */
    private Long financeId;
    /**
     * 名称
     */
    private String detailTitle;
    /**
     * 金额
     */
    private BigDecimal price;

    /**
     * 大写金额
     */
    private String bigPrice;

    /**
     * 备注
     */
    private String remark;

}
