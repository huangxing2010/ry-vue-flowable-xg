package com.ruoyi.oa.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.annotation.DataColumn;
import com.ruoyi.common.annotation.DataPermission;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.oa.domain.SysOaFinance;
import com.ruoyi.oa.domain.bo.SysOaFinanceBo;
import com.ruoyi.oa.domain.vo.SysOaFinanceVo;
import com.ruoyi.common.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 进出账管理Mapper接口
 *
 * @author huangxing
 * @date 2024-01-17
 */
public interface SysOaFinanceMapper extends BaseMapperPlus<SysOaFinanceMapper, SysOaFinance, SysOaFinanceVo> {

    /**
     * 根据条件分页查询进出账列表
     * @param page
     * @param queryWrapper
     * @return
     */
    @DataPermission({
            @DataColumn(key = "detailTitle", value = "b.detail_id"),
            @DataColumn(key = "financeTitle", value = "a.finance_id")
    })
    Page<SysOaFinanceVo> selectPageFinanceList(@Param("page") Page<SysOaFinanceVo> page, @Param(Constants.WRAPPER) Wrapper<SysOaFinance> queryWrapper);


    /**
     * 项目进出账查询
     * @param page
     * @param queryWrapper
     * @return
     */
    Page<SysOaFinanceVo> selectFinanceList(@Param("page") Page<SysOaFinanceVo> page, @Param(Constants.WRAPPER) Wrapper<SysOaFinance> queryWrapper);




    /**
     * 关联查询根据主表id查询
     * @param financeId
     * @return
     */
    SysOaFinance selectFinanceById(Long financeId);


    List<SysOaFinance> selectFinanceByProjectId(SysOaFinanceBo bo);

    /**
     * 根据时间范围查询列表数据
     * @param params
     * @return
     */
    List<SysOaFinance> findFinanceByTime(Map params);

    /**
     * 项目资金管理，根据时间范围查询列表数据
     * @param params
     * @return
     */
//    List<SysOaFinance> findFinanceByTimeAndProjectId(Map params);

}
