package com.ruoyi.oa.service;

import com.ruoyi.oa.domain.SysOaPurpose;
import com.ruoyi.oa.domain.vo.SysOaPurposeVo;
import com.ruoyi.oa.domain.bo.SysOaPurposeBo;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 采购意向Service接口
 *
 * @author huangxing
 * @date 2024-03-31
 */
public interface ISysOaPurposeService {

    /**
     * 查询采购意向
     */
    SysOaPurposeVo queryById(Long purposeId);

    /**
     * 查询采购意向列表
     */
    TableDataInfo<SysOaPurposeVo> queryPageList(SysOaPurposeBo bo, PageQuery pageQuery);

    /**
     * 查询采购意向列表
     */
    List<SysOaPurposeVo> queryList(SysOaPurposeBo bo);

    /**
     * 新增采购意向
     */
    Boolean insertByBo(SysOaPurposeBo bo);

    /**
     * 修改采购意向
     */
    Boolean updateByBo(SysOaPurposeBo bo);

    /**
     * 校验并批量删除采购意向信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
