package com.ruoyi.oa.controller;

import java.util.*;
import java.util.concurrent.TimeUnit;

import com.ruoyi.oa.domain.SysOaTask;
import lombok.RequiredArgsConstructor;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.PageQuery;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import com.ruoyi.common.core.validate.QueryGroup;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.oa.domain.vo.SysOaTaskVo;
import com.ruoyi.oa.domain.bo.SysOaTaskBo;
import com.ruoyi.oa.service.ISysOaTaskService;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 任务管理
 *
 * @author huangxing
 * @date 2024-01-11
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/oa/task")
public class SysOaTaskController extends BaseController {

    private final ISysOaTaskService iSysOaTaskService;

    /**
     * 查询任务管理列表
     */
    @SaCheckPermission("oa:task:list")
    @GetMapping("/list")
    public TableDataInfo<SysOaTaskVo> list(SysOaTaskBo bo, PageQuery pageQuery) {
        return iSysOaTaskService.queryPageList(bo, pageQuery);
    }

    /**
     * 根据工作类型字典查询任务列表
     * @param pid
     * @return
     */
    @GetMapping("/getTaskByDictType/{pid}")
    public R taskByDictType(@PathVariable Long pid){
        R taskByDictList = iSysOaTaskService.queryTaskByDictList(pid);
        return R.ok("ok", taskByDictList);
    }

    /**
     * 根据工作类型查询列表
     * @param bo
     * @return
     */
    @SaCheckPermission("oa:task:list")
    @GetMapping("/listByType")
    public List<SysOaTaskVo> listByType(SysOaTaskBo bo){
        return iSysOaTaskService.queryListByType(bo);
    }

    /**
     * 导出任务管理列表
     */
    @SaCheckPermission("oa:task:export")
    @Log(title = "任务管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SysOaTaskBo bo, HttpServletResponse response) {
        List<SysOaTaskVo> list = iSysOaTaskService.queryList(bo);
        ExcelUtil.exportExcel(list, "任务管理", SysOaTaskVo.class, response);
    }

    /**
     * 获取任务管理详细信息
     *
     * @param taskId 主键
     */
    @SaCheckPermission("oa:task:query")
    @GetMapping("/{taskId}")
    public R<SysOaTaskVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long taskId) {
        return R.ok(iSysOaTaskService.queryById(taskId));
    }

    /**
     * 新增任务管理
     */
    @SaCheckPermission("oa:task:add")
    @Log(title = "任务管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody SysOaTaskBo bo) {
        return toAjax(iSysOaTaskService.insertByBo(bo));
    }

    /**
     * 修改任务管理
     */
    @SaCheckPermission("oa:task:edit")
    @Log(title = "任务管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody SysOaTaskBo bo) {
        return toAjax(iSysOaTaskService.updateByBo(bo));
    }

    /**
     * 删除任务管理
     *
     * @param taskIds 主键串
     */
    @SaCheckPermission("oa:task:remove")
    @Log(title = "任务管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{taskIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] taskIds) {
        return toAjax(iSysOaTaskService.deleteWithValidByIds(Arrays.asList(taskIds), true));
    }
}
