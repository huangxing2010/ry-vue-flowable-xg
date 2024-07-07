package com.ruoyi.oa.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;
import java.util.List;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 知识管理对象 sys_oa_article
 *
 * @author huangxing
 * @date 2024-01-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_oa_article")
public class SysOaArticle extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 文章id
     */
    @TableId(value = "article_id")
    private Long articleId;
    /**
     * 文章分类
     */
    private Long categoryId;
    /**
     * 文章标题
     */
    private String articleTitle;
    /**
     * 副标题
     */
    private String subhead;
    /**
     * 来源
     */
    private String source;
    /**
     * 内容
     */
    private String content;
    /**
     * 查看数
     */
    private Integer checkNum;
    /**
     * 附件
     */
    private String accessory;
    /**
     * 备注
     */
    private String remark;

    private SysOaCategory category;

}
