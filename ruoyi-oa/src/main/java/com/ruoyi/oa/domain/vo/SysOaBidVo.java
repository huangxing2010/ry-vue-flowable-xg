package com.ruoyi.oa.domain.vo;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.common.annotation.ExcelDictFormat;
import com.ruoyi.common.convert.ExcelDictConvert;
import lombok.Data;
import java.util.Date;



/**
 * 投标管理视图对象 sys_oa_bid
 *
 * @author huangxing
 * @date 2024-02-23
 */
@Data
@ExcelIgnoreUnannotated
public class SysOaBidVo {

    private static final long serialVersionUID = 1L;

    /**
     * 文章id
     */
    @ExcelProperty(value = "文章id")
    private Long bidId;

    /**
     * 投标项目名称
     */
    @ExcelProperty(value = "投标项目名称")
    private String bidTitle;

    /**
     * 采购项目编号
     */
    @ExcelProperty(value = "采购项目编号")
    private String bidNumber;

    /**
     * 采购方式
     */
    @ExcelProperty(value = "采购方式", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "sys_bid_type")
    private String bidType;

    /**
     * 采购预算金额
     */
    @ExcelProperty(value = "采购预算金额")
    private String budget;

    /**
     * 项目分包情况
     */
    @ExcelProperty(value = "项目分包情况")
    private String packageInfo;

    /**
     * 公告发布时间
     */
    @ExcelProperty(value = "公告发布时间")
    private String noticeTime;

    /**
     * 获取文件期限
     */
    @ExcelProperty(value = "获取文件期限")
    private String deadline;

    /**
     * 报名方式
     */
    @ExcelProperty(value = "报名方式", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "sys_sign_type")
    private String signType;

    /**
     * 获取招标文件方式
     */
    @ExcelProperty(value = "获取招标文件方式")
    private String patternFile;

    /**
     * 投标截止时间
     */
    @ExcelProperty(value = "投标截止时间")
    private String expirationTime;

    /**
     * 开标地点
     */
    @ExcelProperty(value = "开标地点")
    private String bidAddress;

    /**
     * 采购人联系人
     */
    @ExcelProperty(value = "采购人联系人")
    private String clientName;

    /**
     * 采购代理机构
     */
    @ExcelProperty(value = "采购代理机构")
    private String agency;

    /**
     * 缴纳保证金情况
     */
    @ExcelProperty(value = "缴纳保证金情况")
    private String document;

    /**
     * 缴纳保证金信息
     */
    @ExcelProperty(value = "缴纳保证金情况")
    private String documentInfo;

    /**
     * 是否中标
     */
    @ExcelProperty(value = "是否中标")
    private String isWin;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    /**
     * 附件
     */
    @ExcelProperty(value = "附件")
    private String accessory;


}
