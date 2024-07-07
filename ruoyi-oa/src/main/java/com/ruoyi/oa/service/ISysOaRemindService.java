package com.ruoyi.oa.service;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.oa.domain.SysOaRemind;
import com.ruoyi.oa.domain.vo.SysOaRemindVo;
import com.ruoyi.oa.domain.bo.SysOaRemindBo;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 事件提醒Service接口
 *
 * @author huangxing
 * @date 2024-04-02
 */
public interface ISysOaRemindService {


    /**
     * 查询事件提醒
     */
    SysOaRemindVo queryById(Long remindId);

    /**
     * 查询代办事件列表
     * @param bo
     * @return
     */
    R<List<SysOaRemindVo>> queryRemindList(SysOaRemindBo bo);

    Long queryRemindCount(SysOaRemindBo bo);

    /**
     * 查询事件提醒列表
     */
    TableDataInfo<SysOaRemindVo> queryPageList(SysOaRemindBo bo, PageQuery pageQuery);

    /**
     * 查询事件提醒列表
     */
    List<SysOaRemindVo> queryList(SysOaRemindBo bo);

    /**
     * 新增事件提醒
     */
    Boolean insertByBo(SysOaRemindBo bo);

    /**
     * 修改事件提醒
     */
    Boolean updateByBo(SysOaRemindBo bo);

    /**
     * 校验并批量删除事件提醒信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
