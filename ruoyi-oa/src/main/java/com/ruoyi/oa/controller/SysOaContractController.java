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
import com.ruoyi.oa.domain.vo.SysOaContractVo;
import com.ruoyi.oa.domain.bo.SysOaContractBo;
import com.ruoyi.oa.service.ISysOaContractService;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 合同管理
 *
 * @author huangxing
 * @date 2024-01-10
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/oa/oaContract")
public class SysOaContractController extends BaseController {

    private final ISysOaContractService iSysOaContractService;

    /**
     * 查询合同管理列表
     */
    @SaCheckPermission("oa:oaContract:list")
    @GetMapping("/list")
    public TableDataInfo<SysOaContractVo> list(SysOaContractBo bo, PageQuery pageQuery) {
        return iSysOaContractService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出合同管理列表
     */
    @SaCheckPermission("oa:oaContract:export")
    @Log(title = "合同管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SysOaContractBo bo, HttpServletResponse response) {
        List<SysOaContractVo> list = iSysOaContractService.queryList(bo);
        ExcelUtil.exportExcel(list, "合同管理", SysOaContractVo.class, response);
    }

    /**
     * 获取合同管理详细信息
     *
     * @param contractId 主键
     */
    @SaCheckPermission("oa:oaContract:query")
    @GetMapping("/{contractId}")
    public R<SysOaContractVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long contractId) {
        return R.ok(iSysOaContractService.queryById(contractId));
    }

    /**
     * 根据项目id获取合同信息
     * @param bo
     * @return
     */
    @SaCheckPermission("oa:oaContract:query")
    @GetMapping("/findContract")
    public R<SysOaContractVo> findContractByProjectId(SysOaContractBo bo){
        return R.ok(iSysOaContractService.findContractByProjectId(bo));
    }

    /**
     * 根据项目id查询合同列表
     * @param bo
     * @return
     */
    @SaCheckPermission("oa:oaContract:query")
    @GetMapping("/selectContract")
    public R<List<SysOaContractVo>> selectContractByProjectId(SysOaContractBo bo){
        return R.ok(iSysOaContractService.selectContractByProjectId(bo));
    }

    /**
     * 新增合同管理
     */
    @SaCheckPermission("oa:oaContract:add")
    @Log(title = "合同管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody SysOaContractBo bo) {
        return toAjax(iSysOaContractService.insertByBo(bo));
    }

    /**
     * 修改合同管理
     */
    @SaCheckPermission("oa:oaContract:edit")
    @Log(title = "合同管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody SysOaContractBo bo) {
        return toAjax(iSysOaContractService.updateByBo(bo));
    }

    /**
     * 删除合同管理
     *
     * @param contractIds 主键串
     */
    @SaCheckPermission("oa:oaContract:remove")
    @Log(title = "合同管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{contractIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] contractIds) {
        return toAjax(iSysOaContractService.deleteWithValidByIds(Arrays.asList(contractIds), true));
    }
}
