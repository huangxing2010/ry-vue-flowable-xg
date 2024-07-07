package com.ruoyi.oa.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.annotation.DataColumn;
import com.ruoyi.common.annotation.DataPermission;
import com.ruoyi.oa.domain.SysOaArticle;
import com.ruoyi.oa.domain.SysOaFinance;
import com.ruoyi.oa.domain.vo.SysOaArticleVo;
import com.ruoyi.common.core.mapper.BaseMapperPlus;
import com.ruoyi.oa.domain.vo.SysOaFinanceVo;
import org.apache.ibatis.annotations.Param;

/**
 * 知识管理Mapper接口
 *
 * @author huangxing
 * @date 2024-01-17
 */
public interface SysOaArticleMapper extends BaseMapperPlus<SysOaArticleMapper, SysOaArticle, SysOaArticleVo> {


    @DataPermission({
            @DataColumn(key = "categoryName", value = "c.category_id"),
            @DataColumn(key = "articleTitle", value = "a.article_id")
    })
    Page<SysOaArticleVo> selectArticlePageList(@Param("page") Page<SysOaArticle> page, @Param(Constants.WRAPPER)Wrapper<SysOaArticle> queryWrapper);

    SysOaArticle selectArticleById(Long articleId);



}
