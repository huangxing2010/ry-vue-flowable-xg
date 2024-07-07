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
import com.ruoyi.oa.domain.vo.SysOaDetailVo;
import com.ruoyi.oa.domain.bo.SysOaDetailBo;
import com.ruoyi.oa.service.ISysOaDetailService;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 进出账明细
 *
 * @author huangxing
 * @date 2024-01-17
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/oa/detail")
public class SysOaDetailController extends BaseController {

    private final ISysOaDetailService iSysOaDetailService;

    /**
     * 查询进出账明细列表
     */
    @SaCheckPermission("oa:detail:list")
    @GetMapping("/list")
    public TableDataInfo<SysOaDetailVo> list(SysOaDetailBo bo, PageQuery pageQuery) {
        return iSysOaDetailService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出进出账明细列表
     */
    @SaCheckPermission("oa:detail:export")
    @Log(title = "进出账明细", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SysOaDetailBo bo, HttpServletResponse response) {
        List<SysOaDetailVo> list = iSysOaDetailService.queryList(bo);
        ExcelUtil.exportExcel(list, "进出账明细", SysOaDetailVo.class, response);
    }

    /**
     * 获取进出账明细详细信息
     *
     * @param detailId 主键
     */
    @SaCheckPermission("oa:detail:query")
    @GetMapping("/{detailId}")
    public R<SysOaDetailVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long detailId) {
        return R.ok(iSysOaDetailService.queryById(detailId));
    }

    /**
     * 新增进出账明细
     */
    @SaCheckPermission("oa:detail:add")
    @Log(title = "进出账明细", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody SysOaDetailBo bo) {
        return toAjax(iSysOaDetailService.insertByBo(bo));
    }

    /**
     * 修改进出账明细
     */
    @SaCheckPermission("oa:detail:edit")
    @Log(title = "进出账明细", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody SysOaDetailBo bo) {
        return toAjax(iSysOaDetailService.updateByBo(bo));
    }

    /**
     * 删除进出账明细
     *
     * @param detailIds 主键串
     */
    @SaCheckPermission("oa:detail:remove")
    @Log(title = "进出账明细", businessType = BusinessType.DELETE)
    @DeleteMapping("/{detailIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] detailIds) {
        return toAjax(iSysOaDetailService.deleteWithValidByIds(Arrays.asList(detailIds), true));
    }
}
