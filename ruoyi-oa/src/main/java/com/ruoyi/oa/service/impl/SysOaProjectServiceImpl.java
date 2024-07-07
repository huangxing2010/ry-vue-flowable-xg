package com.ruoyi.oa.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.oa.domain.SysOaContract;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.ruoyi.oa.domain.bo.SysOaProjectBo;
import com.ruoyi.oa.domain.vo.SysOaProjectVo;
import com.ruoyi.oa.domain.SysOaProject;
import com.ruoyi.oa.mapper.SysOaProjectMapper;
import com.ruoyi.oa.service.ISysOaProjectService;

import java.util.*;

/**
 * 项目管理Service业务层处理
 *
 * @author huangxing
 * @date 2024-01-11
 */
@RequiredArgsConstructor
@Service
public class SysOaProjectServiceImpl implements ISysOaProjectService {

    private final SysOaProjectMapper baseMapper;

    /**
     * 查询项目管理
     */
    @Override
    public SysOaProjectVo queryById(Long projectId){
        return baseMapper.selectVoById(projectId);
    }

    /**
     * 查询项目管理列表
     */
    @Override
    public TableDataInfo<SysOaProjectVo> queryPageList(SysOaProjectBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<SysOaProject> lqw = buildQueryWrapper(bo);
        Page<SysOaProjectVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询项目管理列表
     */
    @Override
    public List<SysOaProjectVo> queryList(SysOaProjectBo bo) {
        LambdaQueryWrapper<SysOaProject> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<SysOaProject> buildQueryWrapper(SysOaProjectBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<SysOaProject> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getProjectName()), SysOaProject::getProjectName, bo.getProjectName());
        lqw.eq(StringUtils.isNotBlank(bo.getProjectNum()), SysOaProject::getProjectNum, bo.getProjectNum());
        lqw.eq(StringUtils.isNotBlank(bo.getProjectType()), SysOaProject::getProjectType, bo.getProjectType());
        lqw.eq(StringUtils.isNotBlank(bo.getProjectStatus()), SysOaProject::getProjectStatus, bo.getProjectStatus());
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
                SysOaProject::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));
        lqw.orderByDesc(SysOaProject::getCreateTime);
        return lqw;
    }

    /**
     * 新增项目管理
     */
    @Override
    public Boolean insertByBo(SysOaProjectBo bo) {
        SysOaProject add = BeanUtil.toBean(bo, SysOaProject.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setProjectId(add.getProjectId());
        }
        return flag;
    }

    /**
     * 修改项目管理
     */
    @Override
    public Boolean updateByBo(SysOaProjectBo bo) {
        SysOaProject update = BeanUtil.toBean(bo, SysOaProject.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(SysOaProject entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除项目管理
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
