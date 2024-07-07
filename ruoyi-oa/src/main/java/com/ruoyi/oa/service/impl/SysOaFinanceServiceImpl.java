package com.ruoyi.oa.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.oa.config.ContextUtil;
import com.ruoyi.oa.domain.SysOaDetail;
import com.ruoyi.oa.domain.bo.SysOaDetailBo;
import com.ruoyi.oa.mapper.SysOaDetailMapper;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.runtime.Desc;
import org.springframework.stereotype.Service;
import com.ruoyi.oa.domain.bo.SysOaFinanceBo;
import com.ruoyi.oa.domain.vo.SysOaFinanceVo;
import com.ruoyi.oa.domain.SysOaFinance;
import com.ruoyi.oa.mapper.SysOaFinanceMapper;
import com.ruoyi.oa.service.ISysOaFinanceService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 进出账管理Service业务层处理
 *
 * @author huangxing
 * @date 2024-01-17
 */
@RequiredArgsConstructor
@Service
public class SysOaFinanceServiceImpl implements ISysOaFinanceService {

    private final SysOaFinanceMapper baseMapper;


    private final SysOaDetailMapper sysOaDetailMapper;


    /**
     * 关联进出账明细查询列表
     *
     * @param bo
     * @param pageQuery
     * @return
     */
    @Override
    public TableDataInfo<SysOaFinanceVo> selectPageFinanceList(SysOaFinanceBo bo, PageQuery pageQuery) {
        Page<SysOaFinanceVo> sysOaFinanceVoPage = baseMapper.selectPageFinanceList(pageQuery.build(), this.buildQueryWrapper(bo));
        return TableDataInfo.build(sysOaFinanceVoPage);
    }

    /**
     * 项目进出账查询
     *
     * @param bo
     * @param pageQuery
     * @return
     */
    @Override
    public TableDataInfo<SysOaFinanceVo> selectFinanceList(SysOaFinanceBo bo, PageQuery pageQuery) {
        Page<SysOaFinanceVo> sysOaFinanceVoList = baseMapper.selectFinanceList(pageQuery.build(), this.buildQueryWrapperByFinance(bo));
        return TableDataInfo.build(sysOaFinanceVoList);
    }

    private Wrapper<SysOaFinance> buildQueryWrapperByFinance(SysOaFinanceBo bo) {
        Map<String, Object> params = bo.getParams();
        QueryWrapper<SysOaFinance> lqw = Wrappers.query();
        //前端设定了projectId的值为0和1，0：非项目进出账（添加时候默认为0），1：项目进出账（项目的id是数字串整数，添加时候该字段存储；gt大于0表示项目字段值
//        lqw.ge("a.project_id ",bo.getProjectId());
        lqw.eq(ObjectUtil.isNotNull(bo.getProjectId()), "a.project_id", bo.getProjectId());
        lqw.eq(StringUtils.isNotBlank(bo.getFinanceType()), "a.finance_type", bo.getFinanceType());
        lqw.orderByDesc("a.create_time");
        return lqw;
    }

    /**
     * 根据时间范围(非项目)查询进出账核算
     */
    @Override
    public BigDecimal findFinanceByTime(SysOaFinanceBo bo, Map params) {
        //通过参数查询主表列表，同时一对多查出子表
        Map<String, Object> paramsMap = bo.getParams();
        paramsMap.put("financeType", params.get("financeType"));
        paramsMap.put("beginTime", params.get("beginTime"));
        paramsMap.put("endTime", params.get("endTime"));
        List<SysOaFinance> financeByTime = baseMapper.findFinanceByTime(paramsMap);
        //遍历子表，将价格遍历为数组并用map方法求和，该方法类型使用map比较妥当
        ArrayList<BigDecimal> objects = new ArrayList<>();
        financeByTime.forEach(v -> {
            BigDecimal reduce = v.getDetailList().stream().map(s -> s.getPrice()).reduce(BigDecimal.ZERO, BigDecimal::add);
            objects.add(reduce);
        });
        //主表再次求和
        BigDecimal reduce = objects.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        return reduce;
    }


    /**
     * 根据id查询列表
     *
     * @param financeId
     * @return
     */
    @Override
    public SysOaFinance selectFinanceById(Long financeId) {
        return baseMapper.selectFinanceById(financeId);
    }


    /**
     * 项目核算
     *
     * @param bo
     * @return
     */
    @Override
    public BigDecimal selectFinanceByProjectId(SysOaFinanceBo bo) {
        List<SysOaFinance> sysOaFinances = baseMapper.selectFinanceByProjectId(bo);
        System.out.println("sysOaFinances===" + sysOaFinances);
        System.out.println("sysOaFinances000===" + sysOaFinances.size());
        if (sysOaFinances.size() > 0) {
            ArrayList<BigDecimal> objects = new ArrayList<>();
            sysOaFinances.forEach(v -> {
                System.out.println("vvvv" + v.getDetailList());
                BigDecimal reduce = v.getDetailList().stream().map(s -> s.getPrice() == null ? new BigDecimal(0) : s.getPrice()).reduce(BigDecimal.ZERO, BigDecimal::add);
                objects.add(reduce);
            });
            //主表再次求和
            BigDecimal reduce = objects.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            System.out.println("hehehehe999===" + reduce);
            return reduce;
        } else {
            BigDecimal reduce = new BigDecimal(0);
            System.out.println("hehehehe===" + reduce);
            return reduce;

        }

    }

    /**
     * 查询进出账管理
     */
    @Override
    public SysOaFinanceVo queryById(Long financeId) {
        return baseMapper.selectVoById(financeId);
    }

    /**
     * 查询进出账管理列表
     */
    @Override
    public TableDataInfo<SysOaFinanceVo> queryPageList(SysOaFinanceBo bo, PageQuery pageQuery) {
//        LambdaQueryWrapper<SysOaFinance> lqw = buildQueryWrapper(bo);
        Wrapper<SysOaFinance> lqw = buildQueryWrapper(bo);
        Page<SysOaFinanceVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询进出账管理列表
     */
    @Override
    public List<SysOaFinanceVo> queryList(SysOaFinanceBo bo) {
        Wrapper<SysOaFinance> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    //    private LambdaQueryWrapper<SysOaFinance> buildQueryWrapper(SysOaFinanceBo bo) {
    private Wrapper<SysOaFinance> buildQueryWrapper(SysOaFinanceBo bo) {
        Map<String, Object> params = bo.getParams();
        QueryWrapper<SysOaFinance> lqw = Wrappers.query();
        //前端设定了projectId的值为0和1，0：非项目进出账（添加时候默认为0），1：项目进出账（项目的id是数字串整数，添加时候该字段存储；gt大于0表示项目字段值
        lqw.eq("a.project_id", bo.getProjectId());
        lqw.like(StringUtils.isNotBlank(bo.getFinanceTitle()), "a.finance_title", bo.getFinanceTitle());
        lqw.like(StringUtils.isNotBlank(bo.getFinanceParties()), "a.finance_parties", bo.getFinanceParties());
        lqw.eq(StringUtils.isNotBlank(bo.getFinanceType()), "a.finance_type", bo.getFinanceType());
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
                "a.create_time", params.get("beginCreateTime"), params.get("endCreateTime"));
        lqw.orderByDesc("create_time");
        return lqw;
    }

    /**
     * 新增进出账管理
     */
    @Override
    public Boolean insertByBo(SysOaFinanceBo bo) {
        SysOaFinance add = BeanUtil.toBean(bo, SysOaFinance.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setFinanceId(add.getFinanceId());
        }
        //子表数据插入
        insertSysOaDetail(bo);
        return flag;
    }

    /**
     * 修改进出账管理
     */
    @Override
    public Boolean updateByBo(SysOaFinanceBo bo) {
        SysOaFinance update = BeanUtil.toBean(bo, SysOaFinance.class);
        validEntityBeforeSave(update);
        //先删除原来数据
        sysOaDetailMapper.deleteSysOaDetailByfinanceId(bo.getFinanceId());
        //再插入修改后的数据
        insertSysOaDetail(bo);
        return baseMapper.updateById(update) > 0;
    }


    /**
     * 关联子表的数据新增和修改
     *
     * @param bo
     */
    public void insertSysOaDetail(SysOaFinanceBo bo) {
        List<SysOaDetail> detailList = bo.getDetailList();
        Long financeId = bo.getFinanceId();
        if (detailList.size() > 0) {
            List<SysOaDetail> sysOaDetails = new ArrayList<>();
            for (SysOaDetail list : detailList) {
                list.setFinanceId(financeId);
                sysOaDetails.add(list);
            }
            if (sysOaDetails.size() > 0) {
                sysOaDetailMapper.batchSysOaDetail(sysOaDetails);
            }
        }
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(SysOaFinance entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 根据id删除单条数据，并删除关联数据
     *
     * @param financeId
     * @return
     */
    @Override
    public Boolean deleteSysOaFinanceById(Long financeId) {
        sysOaDetailMapper.deleteSysOaDetailByfinanceId(financeId);
        return baseMapper.deleteById(financeId) > 0;
    }

    /**
     * 批量删除进出账管理
     */
    /*@Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }*/
}
