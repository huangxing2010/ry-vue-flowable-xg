package com.ruoyi.oa.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.oa.domain.SysOaCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.ruoyi.oa.domain.bo.SysOaArticleBo;
import com.ruoyi.oa.domain.vo.SysOaArticleVo;
import com.ruoyi.oa.domain.SysOaArticle;
import com.ruoyi.oa.mapper.SysOaArticleMapper;
import com.ruoyi.oa.service.ISysOaArticleService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 知识管理Service业务层处理
 *
 * @author huangxing
 * @date 2024-01-17
 */
@RequiredArgsConstructor
@Service
public class SysOaArticleServiceImpl implements ISysOaArticleService {

    private final SysOaArticleMapper baseMapper;

    /**
     * 查询知识管理
     */
    @Override
    public SysOaArticle selectArticleById(Long articleId){
        SysOaArticle sysOaArticleInfo = baseMapper.selectArticleById(articleId);
        //更新阅读数量
        SysOaArticleBo sysOaArticleBo = new SysOaArticleBo();
        sysOaArticleBo.setArticleId(articleId);
        sysOaArticleBo.setCheckNum(sysOaArticleInfo.getCheckNum() + 1);
        this.updateByBo(sysOaArticleBo);

        return sysOaArticleInfo;
    }

    /**
     * 查询知识管理列表
     */
    @Override
    public TableDataInfo<SysOaArticleVo> queryPageList(SysOaArticleBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<SysOaArticle> lqw = buildQueryWrapper(bo);
        Page<SysOaArticleVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }


    /**
     * 自定义-查询知识管理列表
     * @param bo
     * @param pageQuery
     * @return
     */
    @Override
    public TableDataInfo<SysOaArticleVo> selectArticlePageList(SysOaArticleBo bo, PageQuery pageQuery){
        LambdaQueryWrapper<SysOaArticle> lqw = buildQueryWrapper(bo);
        Page<SysOaArticleVo> articlePageList = baseMapper.selectArticlePageList(pageQuery.build(), lqw);
        System.out.println(articlePageList);
        return TableDataInfo.build(articlePageList);

    }


    /**
     * 查询知识管理列表
     */
    @Override
    public List<SysOaArticleVo> queryList(SysOaArticleBo bo) {
        LambdaQueryWrapper<SysOaArticle> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<SysOaArticle> buildQueryWrapper(SysOaArticleBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<SysOaArticle> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getCategoryId() != null, SysOaArticle::getCategoryId, bo.getCategoryId());
        lqw.eq(StringUtils.isNotBlank(bo.getArticleTitle()), SysOaArticle::getArticleTitle, bo.getArticleTitle());
        lqw.orderByDesc(SysOaArticle::getCreateTime);
        return lqw;
    }

    /**
     * 新增知识管理
     */
    @Override
    public Boolean insertByBo(SysOaArticleBo bo) {
        SysOaArticle add = BeanUtil.toBean(bo, SysOaArticle.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setArticleId(add.getArticleId());
        }
        return flag;
    }

    /**
     * 修改知识管理
     */
    @Override
    public Boolean updateByBo(SysOaArticleBo bo) {
        SysOaArticle update = BeanUtil.toBean(bo, SysOaArticle.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(SysOaArticle entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除知识管理
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
