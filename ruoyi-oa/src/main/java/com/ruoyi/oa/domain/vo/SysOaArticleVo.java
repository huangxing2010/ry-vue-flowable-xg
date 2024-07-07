package com.ruoyi.oa.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.common.annotation.ExcelDictFormat;
import com.ruoyi.common.convert.ExcelDictConvert;
import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import com.ruoyi.oa.domain.SysOaArticle;
import com.ruoyi.oa.domain.SysOaCategory;
import com.ruoyi.oa.domain.SysOaProject;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;


/**
 * 知识管理视图对象 sys_oa_article
 *
 * @author huangxing
 * @date 2024-01-17
 */
@Data
@ExcelIgnoreUnannotated
public class SysOaArticleVo {

    private static final long serialVersionUID = 1L;

    /**
     * 文章id
     */
    @ExcelProperty(value = "文章id")
    private Long articleId;

    /**
     * 文章分类
     */
    @ExcelProperty(value = "文章分类")
    private Long categoryId;

    /**
     * 文章标题
     */
    @ExcelProperty(value = "文章标题")
    private String articleTitle;

    /**
     * 文章标题
     */
    @ExcelProperty(value = "文章副标题")
    private String subhead;

    /**
     * 来源
     */
    @ExcelProperty(value = "来源")
    private String source;

    /**
     * 备注
     */
    @ExcelProperty(value = "描述")
    private String remark;

    /**
     * 查看数
     */
    @ExcelProperty(value = "查看数")
    private Integer checkNum;

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

    @ExcelProperty(value = "分类实体")
    private SysOaCategory category;


}
