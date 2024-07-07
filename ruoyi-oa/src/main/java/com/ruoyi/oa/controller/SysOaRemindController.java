package com.ruoyi.oa.controller;

import java.util.List;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import com.ruoyi.oa.domain.SysOaRemind;
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
import com.ruoyi.oa.domain.vo.SysOaRemindVo;
import com.ruoyi.oa.domain.bo.SysOaRemindBo;
import com.ruoyi.oa.service.ISysOaRemindService;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 事件提醒
 *
 * @author huangxing
 * @date 2024-04-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/oa/remind")
public class SysOaRemindController extends BaseController {

    private final ISysOaRemindService iSysOaRemindService;

    /**
     * 查询代办事件列表
     * @param bo
     * @return
     */
    @SaCheckPermission("oa:remind:list")
    @GetMapping("/getRemindList")
    public R<List<SysOaRemindVo>> getRemindList(SysOaRemindBo bo){
        R<List<SysOaRemindVo>> remindList = iSysOaRemindService.queryRemindList(bo);
        return remindList;
    }

    /**
     * 查询事件统计
     * @param bo
     * @return
     */
    @SaCheckPermission("oa:remind:list")
    @GetMapping("/getRemindCount")
    public R getRemindCount(SysOaRemindBo bo){
        Long aLong = iSysOaRemindService.queryRemindCount(bo);
        return R.ok("okkkk", aLong);
    }

    /**
     * 查询事件提醒列表
     */
    @SaCheckPermission("oa:remind:list")
    @GetMapping("/list")
    public TableDataInfo<SysOaRemindVo> list(SysOaRemindBo bo, PageQuery pageQuery) {
        return iSysOaRemindService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出事件提醒列表
     */
    @SaCheckPermission("oa:remind:export")
    @Log(title = "事件提醒", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SysOaRemindBo bo, HttpServletResponse response) {
        List<SysOaRemindVo> list = iSysOaRemindService.queryList(bo);
        ExcelUtil.exportExcel(list, "事件提醒", SysOaRemindVo.class, response);
    }

    /**
     * 获取事件提醒详细信息
     *
     * @param remindId 主键
     */
    @SaCheckPermission("oa:remind:query")
    @GetMapping("/{remindId}")
    public R<SysOaRemindVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long remindId) {
        return R.ok(iSysOaRemindService.queryById(remindId));
    }

    /**
     * 新增事件提醒
     */
    @SaCheckPermission("oa:remind:add")
    @Log(title = "事件提醒", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody SysOaRemindBo bo) {
        return toAjax(iSysOaRemindService.insertByBo(bo));
    }

    /**
     * 修改事件提醒
     */
    @SaCheckPermission("oa:remind:edit")
    @Log(title = "事件提醒", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody SysOaRemindBo bo) {
        return toAjax(iSysOaRemindService.updateByBo(bo));
    }

    /**
     * 删除事件提醒
     *
     * @param remindIds 主键串
     */
    @SaCheckPermission("oa:remind:remove")
    @Log(title = "事件提醒", businessType = BusinessType.DELETE)
    @DeleteMapping("/{remindIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] remindIds) {
        return toAjax(iSysOaRemindService.deleteWithValidByIds(Arrays.asList(remindIds), true));
    }
}
