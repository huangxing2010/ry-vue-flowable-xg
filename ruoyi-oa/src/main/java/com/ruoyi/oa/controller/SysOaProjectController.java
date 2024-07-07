package com.ruoyi.oa.controller;

import java.util.List;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.ruoyi.oa.mapper.SysOaTaskMapper;
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
import com.ruoyi.oa.domain.vo.SysOaProjectVo;
import com.ruoyi.oa.domain.bo.SysOaProjectBo;
import com.ruoyi.oa.service.ISysOaProjectService;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 项目管理
 *
 * @author huangxing
 * @date 2024-01-11
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/oa/project")
public class SysOaProjectController extends BaseController {

    private final ISysOaProjectService iSysOaProjectService;
    private final SysOaTaskMapper sysOaTaskMapper;

    /**
     * 查询项目管理列表
     */
    @SaCheckPermission("oa:project:list")
    @GetMapping("/list")
    public TableDataInfo<SysOaProjectVo> list(SysOaProjectBo bo, PageQuery pageQuery) {
        return iSysOaProjectService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出项目管理列表
     */
    @SaCheckPermission("oa:project:export")
    @Log(title = "项目管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SysOaProjectBo bo, HttpServletResponse response) {
        List<SysOaProjectVo> list = iSysOaProjectService.queryList(bo);
        ExcelUtil.exportExcel(list, "项目管理", SysOaProjectVo.class, response);
    }

    /**
     * 获取项目管理详细信息
     *
     * @param projectId 主键
     */
    @SaCheckPermission("oa:project:query")
    @GetMapping("/{projectId}")
    public R<SysOaProjectVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long projectId) {
        return R.ok(iSysOaProjectService.queryById(projectId));
    }

    /**
     * 新增项目管理
     */
    @SaCheckPermission("oa:project:add")
    @Log(title = "项目管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody SysOaProjectBo bo) {
        System.out.println("date=====" + bo);
        return toAjax(iSysOaProjectService.insertByBo(bo));
    }

    /**
     * 修改项目管理
     */
    @SaCheckPermission("oa:project:edit")
    @Log(title = "项目管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody SysOaProjectBo bo) {
        return toAjax(iSysOaProjectService.updateByBo(bo));
    }

    /**
     * 删除项目管理,同时删除任务数据
     * 该方法在列表中单条删除，多条删除也可以但是没有测试
     *
     * @param projectIds 主键串
     */
    @SaCheckPermission("oa:project:remove")
    @Log(title = "项目管理", businessType = BusinessType.DELETE)
    @DSTransactional
    @DeleteMapping("/{projectIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] projectIds) {
        Arrays.asList(projectIds).forEach(v ->{
            sysOaTaskMapper.deleteSysOaTaskByProjectId(v);
        });
        return toAjax(iSysOaProjectService.deleteWithValidByIds(Arrays.asList(projectIds), true));
    }
}
