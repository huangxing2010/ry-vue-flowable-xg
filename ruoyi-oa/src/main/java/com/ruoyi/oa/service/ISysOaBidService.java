package com.ruoyi.oa.service;

import com.ruoyi.oa.domain.SysOaBid;
import com.ruoyi.oa.domain.vo.SysOaBidVo;
import com.ruoyi.oa.domain.bo.SysOaBidBo;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 投标管理Service接口
 *
 * @author huangxing
 * @date 2024-02-23
 */
public interface ISysOaBidService {

    /**
     * 查询投标管理
     */
    SysOaBidVo queryById(Long bidId);

    /**
     * 查询投标管理列表
     */
    TableDataInfo<SysOaBidVo> queryPageList(SysOaBidBo bo, PageQuery pageQuery);

    /**
     * 查询投标管理列表
     */
    List<SysOaBidVo> queryList(SysOaBidBo bo);

    /**
     * 新增投标管理
     */
    Boolean insertByBo(SysOaBidBo bo);

    /**
     * 修改投标管理
     */
    Boolean updateByBo(SysOaBidBo bo);

    /**
     * 校验并批量删除投标管理信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
