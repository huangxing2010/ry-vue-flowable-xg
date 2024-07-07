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
import com.ruoyi.oa.domain.bo.SysOaDetailBo;
import com.ruoyi.oa.domain.vo.SysOaDetailVo;
import com.ruoyi.oa.domain.SysOaDetail;
import com.ruoyi.oa.mapper.SysOaDetailMapper;
import com.ruoyi.oa.service.ISysOaDetailService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 进出账明细Service业务层处理
 *
 * @author huangxing
 * @date 2024-01-17
 */
@RequiredArgsConstructor
@Service
public class SysOaDetailServiceImpl implements ISysOaDetailService {

    private final SysOaDetailMapper baseMapper;

    /**
     * 查询进出账明细
     */
    @Override
    public SysOaDetailVo queryById(Long detailId){
        return baseMapper.selectVoById(detailId);
    }

    /**
     * 查询进出账明细列表
     */
    @Override
    public TableDataInfo<SysOaDetailVo> queryPageList(SysOaDetailBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<SysOaDetail> lqw = buildQueryWrapper(bo);
        Page<SysOaDetailVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询进出账明细列表
     */
    @Override
    public List<SysOaDetailVo> queryList(SysOaDetailBo bo) {
        LambdaQueryWrapper<SysOaDetail> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<SysOaDetail> buildQueryWrapper(SysOaDetailBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<SysOaDetail> lqw = Wrappers.lambdaQuery();
//        lqw.eq(bo.getFinanceId() != null, SysOaDetail::getFinanceId, bo.getFinanceId());
        lqw.eq(StringUtils.isNotBlank(bo.getDetailTitle()), SysOaDetail::getDetailTitle, bo.getDetailTitle());
        return lqw;
    }

    /**
     * 新增进出账明细
     */
    @Override
    public Boolean insertByBo(SysOaDetailBo bo) {
        SysOaDetail add = BeanUtil.toBean(bo, SysOaDetail.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setDetailId(add.getDetailId());
        }
        return flag;
    }

    /**
     * 修改进出账明细
     */
    @Override
    public Boolean updateByBo(SysOaDetailBo bo) {
        SysOaDetail update = BeanUtil.toBean(bo, SysOaDetail.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(SysOaDetail entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除进出账明细
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
