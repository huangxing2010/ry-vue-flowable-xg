package com.ruoyi.oa.mapper;

import com.ruoyi.oa.domain.SysOaTask;
import com.ruoyi.oa.domain.vo.SysOaTaskVo;
import com.ruoyi.common.core.mapper.BaseMapperPlus;

/**
 * 任务管理Mapper接口
 *
 * @author huangxing
 * @date 2024-01-11
 */
public interface SysOaTaskMapper extends BaseMapperPlus<SysOaTaskMapper, SysOaTask, SysOaTaskVo> {

    /**
     * 批量删除
     *
     * @param projectId 需要删除的数据主键集合
     * @return 结果
     */
    int deleteSysOaTaskByProjectId(Long projectId);

}
