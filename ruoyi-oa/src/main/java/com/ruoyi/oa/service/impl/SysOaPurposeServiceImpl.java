package com.ruoyi.oa.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.ruoyi.oa.domain.bo.SysOaPurposeBo;
import com.ruoyi.oa.domain.vo.SysOaPurposeVo;
import com.ruoyi.oa.domain.SysOaPurpose;
import com.ruoyi.oa.mapper.SysOaPurposeMapper;
import com.ruoyi.oa.service.ISysOaPurposeService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 采购意向Service业务层处理
 *
 * @author huangxing
 * @date 2024-03-31
 */
@RequiredArgsConstructor
@Service
public class SysOaPurposeServiceImpl implements ISysOaPurposeService {

    private final SysOaPurposeMapper baseMapper;

    /**
     * 查询采购意向
     */
    @Override
    public SysOaPurposeVo queryById(Long purposeId){
        return baseMapper.selectVoById(purposeId);
    }

    /**
     * 查询采购意向列表
     */
    @Override
    public TableDataInfo<SysOaPurposeVo> queryPageList(SysOaPurposeBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<SysOaPurpose> lqw = buildQueryWrapper(bo);
        Page<SysOaPurposeVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询采购意向列表
     */
    @Override
    public List<SysOaPurposeVo> queryList(SysOaPurposeBo bo) {
        LambdaQueryWrapper<SysOaPurpose> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<SysOaPurpose> buildQueryWrapper(SysOaPurposeBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<SysOaPurpose> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getPurposeTitle()), SysOaPurpose::getPurposeTitle, bo.getPurposeTitle());
        lqw.like(StringUtils.isNotBlank(bo.getSubhead()), SysOaPurpose::getSubhead, bo.getSubhead());
        lqw.eq(StringUtils.isNotBlank(bo.getIsBid()), SysOaPurpose::getIsBid, bo.getIsBid());
        return lqw;
    }

    /**
     * 新增采购意向
     */
    @Override
    public Boolean insertByBo(SysOaPurposeBo bo) {
        SysOaPurpose add = BeanUtil.toBean(bo, SysOaPurpose.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setPurposeId(add.getPurposeId());
        }
        return flag;
    }

    /**
     * 修改采购意向
     */
    @Override
    public Boolean updateByBo(SysOaPurposeBo bo) {
        SysOaPurpose update = BeanUtil.toBean(bo, SysOaPurpose.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(SysOaPurpose entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除采购意向
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
