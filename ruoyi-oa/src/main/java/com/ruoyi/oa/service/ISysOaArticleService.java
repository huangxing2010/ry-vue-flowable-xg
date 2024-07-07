package com.ruoyi.oa.service;

import com.ruoyi.oa.domain.SysOaArticle;
import com.ruoyi.oa.domain.vo.SysOaArticleVo;
import com.ruoyi.oa.domain.bo.SysOaArticleBo;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 知识管理Service接口
 *
 * @author huangxing
 * @date 2024-01-17
 */
public interface ISysOaArticleService {

    /**
     * 查询知识管理
     */
    SysOaArticle selectArticleById(Long articleId);

    /**
     * 查询知识管理列表
     */
    TableDataInfo<SysOaArticleVo> queryPageList(SysOaArticleBo bo, PageQuery pageQuery);

    /**
     * 查询知识管理列表
     */
    List<SysOaArticleVo> queryList(SysOaArticleBo bo);


    TableDataInfo<SysOaArticleVo> selectArticlePageList(SysOaArticleBo bo, PageQuery pageQuery);

    /**
     * 新增知识管理
     */
    Boolean insertByBo(SysOaArticleBo bo);

    /**
     * 修改知识管理
     */
    Boolean updateByBo(SysOaArticleBo bo);

    /**
     * 校验并批量删除知识管理信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
