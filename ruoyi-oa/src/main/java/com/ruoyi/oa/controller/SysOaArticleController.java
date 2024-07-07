package com.ruoyi.oa.controller;

import java.util.List;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import com.ruoyi.oa.domain.SysOaArticle;
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
import com.ruoyi.oa.domain.vo.SysOaArticleVo;
import com.ruoyi.oa.domain.bo.SysOaArticleBo;
import com.ruoyi.oa.service.ISysOaArticleService;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 知识管理
 *
 * @author huangxing
 * @date 2024-01-17
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/oa/article")
public class SysOaArticleController extends BaseController {

    private final ISysOaArticleService iSysOaArticleService;

    /**
     * 查询知识管理列表
     */
    @SaCheckPermission("oa:article:list")
    @GetMapping("/list")
    public TableDataInfo<SysOaArticleVo> list(SysOaArticleBo bo, PageQuery pageQuery) {
        return iSysOaArticleService.queryPageList(bo, pageQuery);
    }

    /**
     * 自定义查询知识管理列表
     */
    @SaCheckPermission("oa:article:list")
    @GetMapping("/listArticle")
    public TableDataInfo<SysOaArticleVo> listArticle(SysOaArticleBo bo, PageQuery pageQuery) {
        return iSysOaArticleService.selectArticlePageList(bo, pageQuery);
    }

    /**
     * 导出知识管理列表
     */
    @SaCheckPermission("oa:article:export")
    @Log(title = "知识管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SysOaArticleBo bo, HttpServletResponse response) {
        List<SysOaArticleVo> list = iSysOaArticleService.queryList(bo);
        ExcelUtil.exportExcel(list, "知识管理", SysOaArticleVo.class, response);
    }

    /**
     * 获取知识管理详细信息
     *
     * @param articleId 主键
     */
    @SaCheckPermission("oa:article:query")
    @GetMapping("/{articleId}")
    public R<SysOaArticle> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long articleId) {
        SysOaArticle sysOaArticle = iSysOaArticleService.selectArticleById(articleId);
        return R.ok(sysOaArticle);
    }

    /**
     * 新增知识管理
     */
    @SaCheckPermission("oa:article:add")
    @Log(title = "知识管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody SysOaArticleBo bo) {
        return toAjax(iSysOaArticleService.insertByBo(bo));
    }

    /**
     * 修改知识管理
     */
    @SaCheckPermission("oa:article:edit")
    @Log(title = "知识管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody SysOaArticleBo bo) {
        return toAjax(iSysOaArticleService.updateByBo(bo));
    }

    /**
     * 删除知识管理
     *
     * @param articleIds 主键串
     */
    @SaCheckPermission("oa:article:remove")
    @Log(title = "知识管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{articleIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] articleIds) {
        return toAjax(iSysOaArticleService.deleteWithValidByIds(Arrays.asList(articleIds), true));
    }
}
