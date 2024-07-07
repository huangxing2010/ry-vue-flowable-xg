package com.ruoyi.oa.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.common.annotation.ExcelDictFormat;
import com.ruoyi.common.convert.ExcelDictConvert;
import lombok.Data;
import java.util.Date;



/**
 * 知识分类视图对象 sys_oa_category
 *
 * @author huangxing
 * @date 2024-01-17
 */
@Data
@ExcelIgnoreUnannotated
public class SysOaCategoryVo {

    private static final long serialVersionUID = 1L;

    /**
     * 产品id
     */
    @ExcelProperty(value = "产品id")
    private Long categoryId;

    /**
     * 父级分类id
     */
    @ExcelProperty(value = "父级分类id")
    private Long parentId;

    /**
     * 分类名称
     */
    @ExcelProperty(value = "分类名称")
    private String categoryName;

    /**
     * 状态
     */
    @ExcelProperty(value = "状态")
    private String status;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    /**
     * 创建者
     */
    @ExcelProperty(value = "创建者")
    private String createBy;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;


}
