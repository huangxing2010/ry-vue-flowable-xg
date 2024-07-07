package com.ruoyi.oa.service;

import com.ruoyi.oa.domain.SysOaFinance;
import com.ruoyi.oa.domain.vo.SysOaFinanceVo;
import com.ruoyi.oa.domain.bo.SysOaFinanceBo;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.domain.PageQuery;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 进出账管理Service接口
 *
 * @author huangxing
 * @date 2024-01-17
 */
public interface ISysOaFinanceService {


    /**
     * 根据分页查询列表
     * @param bo
     * @param pageQuery
     * @return
     */
    TableDataInfo<SysOaFinanceVo> selectPageFinanceList(SysOaFinanceBo bo, PageQuery pageQuery);


    /**
     * 项目进出账查询
     * @param bo
     * @param pageQuery
     * @return
     */
    TableDataInfo<SysOaFinanceVo> selectFinanceList(SysOaFinanceBo bo, PageQuery pageQuery);


    /**
     * 根据id查询详细信息
     * @param financeId
     * @return
     */
    SysOaFinance selectFinanceById(Long financeId);


    BigDecimal selectFinanceByProjectId(SysOaFinanceBo bo);
    /**
     * 查询进出账管理
     */
    SysOaFinanceVo queryById(Long financeId);

    /**
     * 查询进出账管理列表
     */
    TableDataInfo<SysOaFinanceVo> queryPageList(SysOaFinanceBo bo, PageQuery pageQuery);

    /**
     * 查询进出账管理列表
     */
    List<SysOaFinanceVo> queryList(SysOaFinanceBo bo);

    /**
     * 根据时间范围查询列表
     */
   BigDecimal findFinanceByTime(SysOaFinanceBo bo, Map paramsMap);


    /**
     * 新增进出账管理
     */
    Boolean insertByBo(SysOaFinanceBo bo);

    /**
     * 修改进出账管理
     */
    Boolean updateByBo(SysOaFinanceBo bo);

    /**
     * 校验并批量删除进出账管理信息
     */
//    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 根据id删除单条数据，并删除关联数据
     * @param financeId
     * @return
     */
    Boolean deleteSysOaFinanceById(Long financeId);
}
