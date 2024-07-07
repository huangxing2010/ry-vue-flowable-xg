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
import com.ruoyi.oa.domain.vo.SysOaBidVo;
import com.ruoyi.oa.domain.bo.SysOaBidBo;
import com.ruoyi.oa.service.ISysOaBidService;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 投标管理
 *
 * @author huangxing
 * @date 2024-02-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/oa/oaBid")
public class SysOaBidController extends BaseController {

    private final ISysOaBidService iSysOaBidService;

    /**
     * 查询投标管理列表
     */
    @SaCheckPermission("oa:oaBid:list")
    @GetMapping("/list")
    public TableDataInfo<SysOaBidVo> list(SysOaBidBo bo, PageQuery pageQuery) {
        return iSysOaBidService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出投标管理列表
     */
    @SaCheckPermission("oa:oaBid:export")
    @Log(title = "投标管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SysOaBidBo bo, HttpServletResponse response) {
        List<SysOaBidVo> list = iSysOaBidService.queryList(bo);
        ExcelUtil.exportExcel(list, "投标管理", SysOaBidVo.class, response);
    }

    /**
     * 获取投标管理详细信息
     *
     * @param bidId 主键
     */
    @SaCheckPermission("oa:oaBid:query")
    @GetMapping("/{bidId}")
    public R<SysOaBidVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long bidId) {
        return R.ok(iSysOaBidService.queryById(bidId));
    }

    /**
     * 新增投标管理
     */
    @SaCheckPermission("oa:oaBid:add")
    @Log(title = "投标管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody SysOaBidBo bo) {
        return toAjax(iSysOaBidService.insertByBo(bo));
    }

    /**
     * 修改投标管理
     */
    @SaCheckPermission("oa:oaBid:edit")
    @Log(title = "投标管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody SysOaBidBo bo) {
        return toAjax(iSysOaBidService.updateByBo(bo));
    }

    /**
     * 删除投标管理
     *
     * @param bidIds 主键串
     */
    @SaCheckPermission("oa:oaBid:remove")
    @Log(title = "投标管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{bidIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] bidIds) {
        return toAjax(iSysOaBidService.deleteWithValidByIds(Arrays.asList(bidIds), true));
    }
}
