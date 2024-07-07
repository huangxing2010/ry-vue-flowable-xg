package com.ruoyi.oa.domain;

import com.alibaba.excel.annotation.ExcelProperty;
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
 * 投标管理对象 sys_oa_bid
 *
 * @author huangxing
 * @date 2024-02-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_oa_bid")
public class SysOaBid extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 文章id
     */
    @TableId(value = "bid_id")
    private Long bidId;
    /**
     * 投标项目名称
     */
    private String bidTitle;
    /**
     * 采购项目编号
     */
    private String bidNumber;
    /**
     * 采购方式
     */
    private String bidType;
    /**
     * 采购预算金额
     */
    private String budget;
    /**
     * 项目分包情况
     */
    private String packageInfo;
    /**
     * 公告发布时间
     */
    private String noticeTime;
    /**
     * 获取文件期限
     */
    private String deadline;
    /**
     * 报名方式
     */
    private String signType;
    /**
     * 获取招标文件方式
     */
    private String patternFile;
    /**
     * 投标截止时间
     */
    private String expirationTime;
    /**
     * 开标地点
     */
    private String bidAddress;
    /**
     * 采购人联系人
     */
    private String clientName;
    /**
     * 采购代理机构
     */
    private String agency;
    /**
     * 缴纳保证金情况
     */
    private String document;

    /**
     * 缴纳保证金信息
     */
    private String documentInfo;

    /**
     * 是否中标
     */
    private String isWin;
    /**
     * 备注
     */
    private String remark;
    /**
     * 附件
     */
    private String accessory;

}
