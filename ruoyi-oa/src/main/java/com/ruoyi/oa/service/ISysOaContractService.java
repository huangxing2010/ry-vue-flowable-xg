package com.ruoyi.oa.service;

import com.ruoyi.oa.domain.SysOaContract;
import com.ruoyi.oa.domain.vo.SysOaContractVo;
import com.ruoyi.oa.domain.bo.SysOaContractBo;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 合同管理Service接口
 *
 * @author huangxing
 * @date 2024-01-10
 */
public interface ISysOaContractService {

    /**
     * 查询合同管理
     */
    SysOaContractVo queryById(Long contractId);

    /**
     * 查询合同管理列表
     */
    TableDataInfo<SysOaContractVo> queryPageList(SysOaContractBo bo, PageQuery pageQuery);

    /**
     * 查询合同管理列表
     */
    List<SysOaContractVo> queryList(SysOaContractBo bo);

    /**
     * 根据项目id查询合同管理
     * @param bo
     * @return
     */
    SysOaContractVo findContractByProjectId(SysOaContractBo bo);

    /**
     * 根据项目id查询合同列表
     * @param bo
     * @return
     */
    List<SysOaContractVo> selectContractByProjectId(SysOaContractBo bo);

    /**
     * 新增合同管理
     */
    Boolean insertByBo(SysOaContractBo bo);

    /**
     * 合同流程结束自动保存
     * @param param
     * @return
     */
    void inserByProcess(Map<String, Object> param);

    /**
     * 修改合同管理
     */
    Boolean updateByBo(SysOaContractBo bo);

    /**
     * 校验并批量删除合同管理信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
