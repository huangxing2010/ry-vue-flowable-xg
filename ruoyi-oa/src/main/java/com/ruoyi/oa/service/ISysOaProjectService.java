package com.ruoyi.oa.service;

import com.ruoyi.oa.domain.SysOaProject;
import com.ruoyi.oa.domain.vo.SysOaProjectVo;
import com.ruoyi.oa.domain.bo.SysOaProjectBo;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 项目管理Service接口
 *
 * @author huangxing
 * @date 2024-01-11
 */
public interface ISysOaProjectService {

    /**
     * 查询项目管理
     */
    SysOaProjectVo queryById(Long projectId);

    /**
     * 查询项目管理列表
     */
    TableDataInfo<SysOaProjectVo> queryPageList(SysOaProjectBo bo, PageQuery pageQuery);

    /**
     * 查询项目管理列表
     */
    List<SysOaProjectVo> queryList(SysOaProjectBo bo);

    /**
     * 新增项目管理
     */
    Boolean insertByBo(SysOaProjectBo bo);

    /**
     * 修改项目管理
     */
    Boolean updateByBo(SysOaProjectBo bo);

    /**
     * 校验并批量删除项目管理信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
