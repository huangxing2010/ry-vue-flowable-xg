package com.ruoyi.oa.domain.vo;

import java.math.BigDecimal;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.common.annotation.ExcelDictFormat;
import com.ruoyi.common.convert.ExcelDictConvert;
import lombok.Data;
import java.util.Date;



/**
 * 进出账明细视图对象 sys_oa_detail
 *
 * @author huangxing
 * @date 2024-01-17
 */
@Data
@ExcelIgnoreUnannotated
public class SysOaDetailVo {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ExcelProperty(value = "ID")
    private Long detailId;

    /**
     * 进出账ID
     */
    @ExcelProperty(value = "进出账ID")
    private Long financeId;

    /**
     * 名称
     */
    @ExcelProperty(value = "名称")
    private String detailTitle;

    /**
     * 金额
     */
    @ExcelProperty(value = "金额")
    private BigDecimal price;

    /**
     * 大写金额
     */
    @ExcelProperty(value = "大写金额")
    private String bigPrice;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;


}
