package com.ruoyi.oa.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.oa.domain.SysOaBid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.oa.domain.bo.SysOaContractBo;
import com.ruoyi.oa.domain.vo.SysOaContractVo;
import com.ruoyi.oa.domain.SysOaContract;
import com.ruoyi.oa.mapper.SysOaContractMapper;
import com.ruoyi.oa.service.ISysOaContractService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 合同管理Service业务层处理
 *
 * @author huangxing
 * @date 2024-01-10
 */
@RequiredArgsConstructor
@Service
public class SysOaContractServiceImpl implements ISysOaContractService {

    @Autowired
    private SysOaContractMapper contractMapper;


    /**
     * 查询合同管理
     */
    @Override
    public SysOaContractVo queryById(Long contractId){
        return contractMapper.selectVoById(contractId);
    }

    /**
     * 查询合同管理列表
     */
    @Override
    public TableDataInfo<SysOaContractVo> queryPageList(SysOaContractBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<SysOaContract> lqw = buildQueryWrapper(bo);
        Page<SysOaContractVo> result = contractMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询合同管理列表
     */
    @Override
    public List<SysOaContractVo> queryList(SysOaContractBo bo) {
        LambdaQueryWrapper<SysOaContract> lqw = buildQueryWrapper(bo);
        return contractMapper.selectVoList(lqw);
    }

    /**
     * 根据项目id查询合同
     * @param bo
     * @return
     */
    @Override
    public SysOaContractVo findContractByProjectId(SysOaContractBo bo){
        LambdaQueryWrapper<SysOaContract> sysOaContractLambdaQueryWrapper = buildContractWrapper(bo);
        return contractMapper.selectVoOne(sysOaContractLambdaQueryWrapper);
    }

    /**
     * 根据项目id查询合同列表
     * @param bo
     * @return
     */
    @Override
    public List<SysOaContractVo> selectContractByProjectId(SysOaContractBo bo){
        LambdaQueryWrapper<SysOaContract> sysOaContractLambdaQueryWrapper = buildContractWrapper(bo);        ;
        return contractMapper.selectVoList(sysOaContractLambdaQueryWrapper);
    }

    private LambdaQueryWrapper<SysOaContract> buildQueryWrapper(SysOaContractBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<SysOaContract> lqw = Wrappers.lambdaQuery();
        lqw.eq(SysOaContract::getContractType, '0');
        lqw.eq(StringUtils.isNotBlank(bo.getContractNum()), SysOaContract::getContractNum, bo.getContractNum());
        lqw.like(StringUtils.isNotBlank(bo.getContractName()), SysOaContract::getContractName, bo.getContractName());
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
                SysOaContract::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));
        lqw.orderByDesc(SysOaContract::getCreateTime);
        return lqw;
    }

    private LambdaQueryWrapper<SysOaContract> buildContractWrapper(SysOaContractBo bo) {
        LambdaQueryWrapper<SysOaContract> lqw = Wrappers.lambdaQuery();
        lqw.eq(SysOaContract::getProjectId, bo.getProjectId());
        lqw.eq(SysOaContract::getContractType, bo.getContractType());

        return lqw;
    }



    /**
     * 新增合同管理
     */
    @Override
    public Boolean insertByBo(SysOaContractBo bo) {
        SysOaContract add = BeanUtil.toBean(bo, SysOaContract.class);
        validEntityBeforeSave(add);
        boolean flag = contractMapper.insert(add) > 0;
        if (flag) {
            bo.setContractId(add.getContractId());
        }
        return flag;
    }

    /**
     * 合同流程结束自动保存
     * @param param
     * @return
     */
    @Override
    public void inserByProcess(Map<String, Object> param){
        System.out.println("map=====" + param);
        /*if(param.size() > 0){
            return true;
        }else {
            return false;
        }*/

    }

    /**
     * 修改合同管理
     */
    @Override
    public Boolean updateByBo(SysOaContractBo bo) {
        SysOaContract update = BeanUtil.toBean(bo, SysOaContract.class);
        validEntityBeforeSave(update);
        return contractMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(SysOaContract entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除合同管理
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return contractMapper.deleteBatchIds(ids) > 0;
    }
}
