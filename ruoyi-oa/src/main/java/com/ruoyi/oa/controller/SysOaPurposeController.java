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
import com.ruoyi.oa.domain.vo.SysOaPurposeVo;
import com.ruoyi.oa.domain.bo.SysOaPurposeBo;
import com.ruoyi.oa.service.ISysOaPurposeService;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 采购意向
 *
 * @author huangxing
 * @date 2024-03-31
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/oa/purpose")
public class SysOaPurposeController extends BaseController {

    private final ISysOaPurposeService iSysOaPurposeService;

    /**
     * 查询采购意向列表
     */
    @SaCheckPermission("oa:purpose:list")
    @GetMapping("/list")
    public TableDataInfo<SysOaPurposeVo> list(SysOaPurposeBo bo, PageQuery pageQuery) {
        return iSysOaPurposeService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出采购意向列表
     */
    @SaCheckPermission("oa:purpose:export")
    @Log(title = "采购意向", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SysOaPurposeBo bo, HttpServletResponse response) {
        List<SysOaPurposeVo> list = iSysOaPurposeService.queryList(bo);
        ExcelUtil.exportExcel(list, "采购意向", SysOaPurposeVo.class, response);
    }

    /**
     * 获取采购意向详细信息
     *
     * @param purposeId 主键
     */
    @SaCheckPermission("oa:purpose:query")
    @GetMapping("/{purposeId}")
    public R<SysOaPurposeVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long purposeId) {
        return R.ok(iSysOaPurposeService.queryById(purposeId));
    }

    /**
     * 新增采购意向
     */
    @SaCheckPermission("oa:purpose:add")
    @Log(title = "采购意向", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody SysOaPurposeBo bo) {
        return toAjax(iSysOaPurposeService.insertByBo(bo));
    }

    /**
     * 修改采购意向
     */
    @SaCheckPermission("oa:purpose:edit")
    @Log(title = "采购意向", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody SysOaPurposeBo bo) {
        return toAjax(iSysOaPurposeService.updateByBo(bo));
    }

    /**
     * 删除采购意向
     *
     * @param purposeIds 主键串
     */
    @SaCheckPermission("oa:purpose:remove")
    @Log(title = "采购意向", businessType = BusinessType.DELETE)
    @DeleteMapping("/{purposeIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] purposeIds) {
        return toAjax(iSysOaPurposeService.deleteWithValidByIds(Arrays.asList(purposeIds), true));
    }
}
