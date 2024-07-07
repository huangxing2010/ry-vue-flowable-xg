package com.ruoyi.oa.controller;

import java.util.List;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

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
import com.ruoyi.oa.domain.vo.SysOaCategoryVo;
import com.ruoyi.oa.domain.bo.SysOaCategoryBo;
import com.ruoyi.oa.service.ISysOaCategoryService;

/**
 * 知识分类
 *
 * @author huangxing
 * @date 2024-01-17
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/oa/category")
public class SysOaCategoryController extends BaseController {

    private final ISysOaCategoryService iSysOaCategoryService;

    /**
     * 查询知识分类列表
     */
    @SaCheckPermission("oa:category:list")
    @GetMapping("/list")
    public R<List<SysOaCategoryVo>> list(SysOaCategoryBo bo) {
        List<SysOaCategoryVo> list = iSysOaCategoryService.queryList(bo);
        return R.ok(list);
    }

    /**
     * 导出知识分类列表
     */
    @SaCheckPermission("oa:category:export")
    @Log(title = "知识分类", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SysOaCategoryBo bo, HttpServletResponse response) {
        List<SysOaCategoryVo> list = iSysOaCategoryService.queryList(bo);
        ExcelUtil.exportExcel(list, "知识分类", SysOaCategoryVo.class, response);
    }

    /**
     * 获取知识分类详细信息
     *
     * @param categoryId 主键
     */
    @SaCheckPermission("oa:category:query")
    @GetMapping("/{categoryId}")
    public R<SysOaCategoryVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long categoryId) {
        return R.ok(iSysOaCategoryService.queryById(categoryId));
    }

    /**
     * 新增知识分类
     */
    @SaCheckPermission("oa:category:add")
    @Log(title = "知识分类", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody SysOaCategoryBo bo) {
        return toAjax(iSysOaCategoryService.insertByBo(bo));
    }

    /**
     * 修改知识分类
     */
    @SaCheckPermission("oa:category:edit")
    @Log(title = "知识分类", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody SysOaCategoryBo bo) {
        return toAjax(iSysOaCategoryService.updateByBo(bo));
    }

    /**
     * 删除知识分类
     *
     * @param categoryIds 主键串
     */
    @SaCheckPermission("oa:category:remove")
    @Log(title = "知识分类", businessType = BusinessType.DELETE)
    @DeleteMapping("/{categoryIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] categoryIds) {
        return toAjax(iSysOaCategoryService.deleteWithValidByIds(Arrays.asList(categoryIds), true));
    }
}
