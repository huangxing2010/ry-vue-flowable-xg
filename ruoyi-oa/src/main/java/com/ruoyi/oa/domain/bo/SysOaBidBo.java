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
 * 投标管理业务对象 sys_oa_bid
 *
 * @author huangxing
 * @date 2024-02-23
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class SysOaBidBo extends BaseEntity {

    /**
     * 文章id
     */
    @NotNull(message = "文章id不能为空", groups = { EditGroup.class })
    private Long bidId;

    /**
     * 投标项目名称
     */
    @NotBlank(message = "投标项目名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String bidTitle;

    /**
     * 采购项目编号
     */
    @NotBlank(message = "采购项目编号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String bidNumber;

    /**
     * 采购方式
     */
    @NotBlank(message = "采购方式不能为空", groups = { AddGroup.class, EditGroup.class })
    private String bidType;

    /**
     * 采购预算金额
     */
    @NotNull(message = "采购预算金额不能为空", groups = { AddGroup.class, EditGroup.class })
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
