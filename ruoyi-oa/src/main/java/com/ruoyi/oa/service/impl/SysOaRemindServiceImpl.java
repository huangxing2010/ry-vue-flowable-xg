package com.ruoyi.oa.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.helper.LoginHelper;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.stereotype.Service;
import com.ruoyi.oa.domain.bo.SysOaRemindBo;
import com.ruoyi.oa.domain.vo.SysOaRemindVo;
import com.ruoyi.oa.domain.SysOaRemind;
import com.ruoyi.oa.mapper.SysOaRemindMapper;
import com.ruoyi.oa.service.ISysOaRemindService;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 事件提醒Service业务层处理
 *
 * @author huangxing
 * @date 2024-04-02
 */
@RequiredArgsConstructor
@Service
public class SysOaRemindServiceImpl implements ISysOaRemindService {

    private final SysOaRemindMapper baseMapper;

    /**
     * 查询事件提醒
     */
    @Override
    public SysOaRemindVo queryById(Long remindId){
        return baseMapper.selectVoById(remindId);
    }

    /**
     * 查询代办事件列表
     * @param bo
     * @return
     */
    @Override
    public R<List<SysOaRemindVo>> queryRemindList(SysOaRemindBo bo){
        LambdaQueryWrapper<SysOaRemind> lqw = buildQueryWrapper(bo);
        List<SysOaRemindVo> sysOaRemindVos = baseMapper.selectVoList(lqw);
        return R.ok(sysOaRemindVos);
    }

    @Override
    public Long queryRemindCount(SysOaRemindBo bo){
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<SysOaRemind> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getTaskStatus()), SysOaRemind::getTaskStatus, bo.getTaskStatus());
        lqw.between(params.get("beginTime") != null && params.get("endTime") != null, SysOaRemind::getTaskTime, params.get("beginTime"), params.get("endTime"));
        Long aLong = baseMapper.selectCount(lqw);
        return  aLong;
    }

    /**
     * 查询事件提醒列表
     */
    @Override
    public TableDataInfo<SysOaRemindVo> queryPageList(SysOaRemindBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<SysOaRemind> lqw = buildQueryWrapper(bo);
        Page<SysOaRemindVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询事件提醒列表
     */
    @Override
    public List<SysOaRemindVo> queryList(SysOaRemindBo bo) {
        LambdaQueryWrapper<SysOaRemind> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<SysOaRemind> buildQueryWrapper(SysOaRemindBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<SysOaRemind> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getTaskTitle()), SysOaRemind::getTaskTitle, bo.getTaskTitle());
//        lqw.eq(bo.getTaskTime() != null, SysOaRemind::getTaskTime, bo.getTaskTime());
        lqw.eq(StringUtils.isNotBlank(bo.getTaskStatus()), SysOaRemind::getTaskStatus, bo.getTaskStatus());
        lqw.between(params.get("beginTime") != null && params.get("endTime") != null, SysOaRemind::getTaskTime, params.get("beginTime"), params.get("endTime"));
        lqw.orderByDesc(SysOaRemind::getCreateTime);
        return lqw;
    }

    /**
     * 新增事件提醒
     */
    @Override
    public Boolean insertByBo(SysOaRemindBo bo) {
        bo.setCreateBy(LoginHelper.getUsername());
        bo.setCreateTime(new Date());
        SysOaRemind add = BeanUtil.toBean(bo, SysOaRemind.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setRemindId(add.getRemindId());
        }
        return flag;
    }

    /**
     * 修改事件提醒
     */
    @Override
    public Boolean updateByBo(SysOaRemindBo bo) {
        bo.setUpdateBy(LoginHelper.getUsername());
        bo.setUpdateTime(new Date());
        SysOaRemind update = BeanUtil.toBean(bo, SysOaRemind.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(SysOaRemind entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除事件提醒
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
