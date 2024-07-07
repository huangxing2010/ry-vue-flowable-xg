package com.ruoyi.oa.service;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.oa.domain.SysOaTask;
import com.ruoyi.oa.domain.vo.SysOaTaskVo;
import com.ruoyi.oa.domain.bo.SysOaTaskBo;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 任务管理Service接口
 *
 * @author huangxing
 * @date 2024-01-11
 */
public interface ISysOaTaskService {

    /**
     * 查询任务管理
     */
    SysOaTaskVo queryById(Long taskId);

    /**
     * 查询任务管理列表
     */
    TableDataInfo<SysOaTaskVo> queryPageList(SysOaTaskBo bo, PageQuery pageQuery);

    /**
     * 查询任务管理列表
     */
    List<SysOaTaskVo> queryList(SysOaTaskBo bo);

    /****
     * 据工作类型字典查询任务列表
     * @param pid
     * @return
     */
    R queryTaskByDictList(Long pid);
    /**
     * 根据工作类型查询列表
     * @param bo
     * @return
     */
    List<SysOaTaskVo> queryListByType(SysOaTaskBo bo);

    /**
     * 新增任务管理
     */
    Boolean insertByBo(SysOaTaskBo bo);

    /**
     * 修改任务管理
     */
    Boolean updateByBo(SysOaTaskBo bo);

    /**
     * 校验并批量删除任务管理信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
