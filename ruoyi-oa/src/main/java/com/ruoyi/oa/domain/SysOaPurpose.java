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
 * 采购意向对象 sys_oa_purpose
 *
 * @author huangxing
 * @date 2024-03-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_oa_purpose")
public class SysOaPurpose extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 项目id
     */
    @TableId(value = "purpose_id")
    private Long purposeId;
    /**
     * 项目名称
     */
    private String purposeTitle;
    /**
     * 采购单位
     */
    private String subhead;
    /**
     * 预算金额
     */
    private String budget;
    /**
     * 采购需求情况
     */
    private String content;
    /**
     * 预计采购时间
     */
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
