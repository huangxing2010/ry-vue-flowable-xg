package com.ruoyi.oa.service.impl;

import cn.dev33.satoken.fun.SaParamRetFunction;
import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.core.domain.entity.SysDictType;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.helper.LoginHelper;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.system.service.ISysDictTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.ruoyi.oa.domain.bo.SysOaTaskBo;
import com.ruoyi.oa.domain.vo.SysOaTaskVo;
import com.ruoyi.oa.domain.SysOaTask;
import com.ruoyi.oa.mapper.SysOaTaskMapper;
import com.ruoyi.oa.service.ISysOaTaskService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 任务管理Service业务层处理
 *
 * @author huangxing
 * @date 2024-01-11
 */
@RequiredArgsConstructor
@Service
public class SysOaTaskServiceImpl implements ISysOaTaskService {

    private final SysOaTaskMapper baseMapper;

    private final ISysDictTypeService dictTypeService;

    /**
     * 查询任务管理
     */
    @Override
    public SysOaTaskVo queryById(Long taskId){
        return baseMapper.selectVoById(taskId);
    }

    /**
     * 查询任务管理列表
     */
    @Override
    public TableDataInfo<SysOaTaskVo> queryPageList(SysOaTaskBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<SysOaTask> lqw = buildQueryWrapper(bo);
        //查询当前用户下的数据列表
        lqw.eq(StringUtils.isNotBlank(LoginHelper.getLoginUser().getUsername()), SysOaTask::getCreateBy,LoginHelper.getLoginUser().getUsername());
        Page<SysOaTaskVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 据工作类型字典查询任务列表
     * @param pid
     * @return
     */
    @Override
    public R queryTaskByDictList(Long pid){
        List<Object> objects = new ArrayList<>();
        List<SysDictData> sysWorkType = dictTypeService.selectDictDataByType("sys_work_type");
        sysWorkType.forEach(item -> {
            SysOaTaskBo sysOaTaskBo = new SysOaTaskBo();
            sysOaTaskBo.setProjectId(pid);
            sysOaTaskBo.setTaskType(item.getDictValue());
            List<SysOaTaskVo> sysOaTaskVos = this.queryListByType(sysOaTaskBo);
            Map<String, Object> ajax = new HashMap<>();
            ajax.put("dictLabel", item.getDictLabel());
            ajax.put("dictValue", item.getDictValue());
            ajax.put("taskList", sysOaTaskVos);
            ajax.put("size", "large");
            ajax.put("type", "primary");
            ajax.put("icon", "el-icon-more");
            ajax.put("color", "#cccccc");
            objects.add(ajax);
        });

        //根据项目id获取任务列表类型属性最大的值
        SysOaTaskBo task = new SysOaTaskBo();
        task.setProjectId(pid);
        List<SysOaTaskVo> taskVos = this.queryListByType(task);
        List<Integer> collect = taskVos.stream().map(v -> Integer.parseInt(v.getTaskType())).collect(Collectors.toList());
        OptionalInt max = collect.stream().mapToInt(Integer::intValue).max();
        Map<String, Object> map = new HashMap<>();
        if(max.isPresent()){
            map.put("active", max.getAsInt());
        }else {
            map.put("active", 0);
        }
        map.put("taskList", objects);
        return R.ok(map);
    }

    /**
     * 根据工作类型查询列表
     * @param bo
     * @return
     */
    @Override
    public List<SysOaTaskVo> queryListByType(SysOaTaskBo bo){
        LambdaQueryWrapper<SysOaTask> lqw = buildQueryWrapper(bo);
        lqw.eq(StringUtils.isNotEmpty(bo.getProjectId().toString()), SysOaTask::getProjectId, bo.getProjectId());
        lqw.eq(StringUtils.isNotBlank(bo.getTaskType()),SysOaTask::getTaskType, bo.getTaskType());
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 查询任务管理列表
     */
    @Override
    public List<SysOaTaskVo> queryList(SysOaTaskBo bo) {
        LambdaQueryWrapper<SysOaTask> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<SysOaTask> buildQueryWrapper(SysOaTaskBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<SysOaTask> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getProjectId().toString()), SysOaTask::getProjectId, bo.getProjectId());
        lqw.eq(StringUtils.isNotBlank(bo.getTaskType()), SysOaTask::getTaskType, bo.getTaskType());
        lqw.like(StringUtils.isNotBlank(bo.getTaskTitle()), SysOaTask::getTaskTitle, bo.getTaskTitle());
        lqw.orderByDesc(SysOaTask::getCreateTime);
        return lqw;
    }

    /**
     * 新增任务管理
     */
    @Override
    public Boolean insertByBo(SysOaTaskBo bo) {
        SysOaTask add = BeanUtil.toBean(bo, SysOaTask.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setTaskId(add.getTaskId());
        }
        return flag;
    }

    /**
     * 修改任务管理
     */
    @Override
    public Boolean updateByBo(SysOaTaskBo bo) {
        SysOaTask update = BeanUtil.toBean(bo, SysOaTask.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(SysOaTask entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除任务管理
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
