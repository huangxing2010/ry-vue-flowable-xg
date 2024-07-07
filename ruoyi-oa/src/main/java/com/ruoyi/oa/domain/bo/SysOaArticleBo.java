package com.ruoyi.oa.domain.bo;

import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import com.ruoyi.oa.domain.SysOaCategory;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.util.Date;
import java.util.List;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 知识管理业务对象 sys_oa_article
 *
 * @author huangxing
 * @date 2024-01-17
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class SysOaArticleBo extends BaseEntity {

    /**
     * 文章id
     */
    @NotNull(message = "文章id不能为空", groups = { EditGroup.class })
    private Long articleId;

    /**
     * 文章分类
     */
    @NotNull(message = "文章分类不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long categoryId;

    /**
     * 文章标题
     */
    @NotBlank(message = "文章标题不能为空", groups = { AddGroup.class, EditGroup.class })
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
