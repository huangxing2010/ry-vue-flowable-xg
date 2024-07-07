package com.ruoyi.oa.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.oa.domain.SysOaTask;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.ruoyi.oa.domain.bo.SysOaCategoryBo;
import com.ruoyi.oa.domain.vo.SysOaCategoryVo;
import com.ruoyi.oa.domain.SysOaCategory;
import com.ruoyi.oa.mapper.SysOaCategoryMapper;
import com.ruoyi.oa.service.ISysOaCategoryService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 知识分类Service业务层处理
 *
 * @author huangxing
 * @date 2024-01-17
 */
@RequiredArgsConstructor
@Service
public class SysOaCategoryServiceImpl implements ISysOaCategoryService {

    private final SysOaCategoryMapper baseMapper;

    /**
     * 查询知识分类
     */
    @Override
    public SysOaCategoryVo queryById(Long categoryId){
        return baseMapper.selectVoById(categoryId);
    }


    /**
     * 查询知识分类列表
     */
    @Override
    public List<SysOaCategoryVo> queryList(SysOaCategoryBo bo) {
        LambdaQueryWrapper<SysOaCategory> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<SysOaCategory> buildQueryWrapper(SysOaCategoryBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<SysOaCategory> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getCategoryName()), SysOaCategory::getCategoryName, bo.getCategoryName());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), SysOaCategory::getStatus, bo.getStatus());
        lqw.orderByDesc(SysOaCategory::getCreateTime);
        return lqw;
    }

    /**
     * 新增知识分类
     */
    @Override
    public Boolean insertByBo(SysOaCategoryBo bo) {
        SysOaCategory add = BeanUtil.toBean(bo, SysOaCategory.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setCategoryId(add.getCategoryId());
        }
        return flag;
    }

    /**
     * 修改知识分类
     */
    @Override
    public Boolean updateByBo(SysOaCategoryBo bo) {
        SysOaCategory update = BeanUtil.toBean(bo, SysOaCategory.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(SysOaCategory entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除知识分类
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
