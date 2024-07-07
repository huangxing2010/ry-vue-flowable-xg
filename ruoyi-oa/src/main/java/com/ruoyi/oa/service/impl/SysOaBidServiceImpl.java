package com.ruoyi.oa.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.oa.domain.SysOaProject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.ruoyi.oa.domain.bo.SysOaBidBo;
import com.ruoyi.oa.domain.vo.SysOaBidVo;
import com.ruoyi.oa.domain.SysOaBid;
import com.ruoyi.oa.mapper.SysOaBidMapper;
import com.ruoyi.oa.service.ISysOaBidService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 投标管理Service业务层处理
 *
 * @author huangxing
 * @date 2024-02-23
 */
@RequiredArgsConstructor
@Service
public class SysOaBidServiceImpl implements ISysOaBidService {

    private final SysOaBidMapper baseMapper;

    /**
     * 查询投标管理
     */
    @Override
    public SysOaBidVo queryById(Long bidId){
        return baseMapper.selectVoById(bidId);
    }

    /**
     * 查询投标管理列表
     */
    @Override
    public TableDataInfo<SysOaBidVo> queryPageList(SysOaBidBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<SysOaBid> lqw = buildQueryWrapper(bo);
        Page<SysOaBidVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询投标管理列表
     */
    @Override
    public List<SysOaBidVo> queryList(SysOaBidBo bo) {
        LambdaQueryWrapper<SysOaBid> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<SysOaBid> buildQueryWrapper(SysOaBidBo bo) {
        Map<String, Object> params = bo.getParams();
//        System.out.println("params===123==" + params);
        LambdaQueryWrapper<SysOaBid> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getBidTitle()), SysOaBid::getBidTitle, bo.getBidTitle());
        lqw.eq(StringUtils.isNotBlank(bo.getBidNumber()), SysOaBid::getBidNumber, bo.getBidNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getIsWin()), SysOaBid::getIsWin, bo.getIsWin());
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
                SysOaBid::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));
        lqw.orderByDesc(SysOaBid::getCreateTime);
        return lqw;
    }

    /**
     * 新增投标管理
     */
    @Override
    public Boolean insertByBo(SysOaBidBo bo) {
        SysOaBid add = BeanUtil.toBean(bo, SysOaBid.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setBidId(add.getBidId());
        }
        return flag;
    }

    /**
     * 修改投标管理
     */
    @Override
    public Boolean updateByBo(SysOaBidBo bo) {
        SysOaBid update = BeanUtil.toBean(bo, SysOaBid.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(SysOaBid entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除投标管理
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
