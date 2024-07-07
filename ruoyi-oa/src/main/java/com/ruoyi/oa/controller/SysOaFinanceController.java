package com.ruoyi.oa.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import com.ruoyi.oa.domain.SysOaFinance;
import com.ruoyi.oa.domain.vo.SysOaProjectVo;
import com.ruoyi.oa.service.ISysOaDetailService;
import com.ruoyi.oa.service.ISysOaProjectService;
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
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.oa.domain.vo.SysOaFinanceVo;
import com.ruoyi.oa.domain.bo.SysOaFinanceBo;
import com.ruoyi.oa.service.ISysOaFinanceService;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 进出账管理
 *
 * @author huangxing
 * @date 2024-01-17
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/oa/finance")
public class SysOaFinanceController extends BaseController {

    private final ISysOaFinanceService iSysOaFinanceService;

    private ISysOaDetailService sysOaDetailService;

    private final ISysOaProjectService sysOaProjectService;

    /**
     * 查询进出账管理列表
     */
    @SaCheckPermission("oa:finance:list")
    @GetMapping("/list")
    public TableDataInfo<SysOaFinanceVo> list(SysOaFinanceBo bo, PageQuery pageQuery) {
        TableDataInfo<SysOaFinanceVo> sysOaFinanceVoTableDataInfo = iSysOaFinanceService.selectPageFinanceList(bo, pageQuery);
        return sysOaFinanceVoTableDataInfo;
    }

    @SaCheckPermission("oa:finance:list")
    @GetMapping("/listPro")
    public TableDataInfo<SysOaFinanceVo> listPro(SysOaFinanceBo bo, PageQuery pageQuery) {
        TableDataInfo<SysOaFinanceVo> sysOaFinanceList = iSysOaFinanceService.selectFinanceList(bo, pageQuery);
        return sysOaFinanceList;
    }

    /**
     * 根据时间查询
     */
    @SaCheckPermission("oa:finance:list")
    @GetMapping("/getFinanceByTime")
    public R<Map<String, Object>> findFinanceByTime(SysOaFinanceBo bo, @RequestParam Map<String, Object> params) {

        Map<String, Object> params1 = params;
        params1.put("financeType", "1");
        BigDecimal finance1 = iSysOaFinanceService.findFinanceByTime(bo, params1);

        Map<String, Object> params0 = params;
        params0.put("financeType", "0");

        BigDecimal finance0 = iSysOaFinanceService.findFinanceByTime(bo, params0);



        Map<String, Object> ajax = new HashMap<>();
        ajax.put("income", finance1);
        ajax.put("out", finance0);
        ajax.put("check", finance1.subtract(finance0));
        return R.ok("ok",ajax);
    }

    /**
     * 项目资金管理，根据时间查询
     */
    @SaCheckPermission("oa:finance:list")
    @GetMapping("/findFinanceByTimeAndProjectId")
    public R<Map<String, Object>> findFinanceByTimeAndProjectId(SysOaFinanceBo bo) {
        //项目总金额
        SysOaProjectVo sysOaProjectVo = sysOaProjectService.queryById(bo.getProjectId());
        BigDecimal funds = sysOaProjectVo.getFunds();

        //入账总额
        SysOaFinanceBo boOne = bo;
        boOne.setFinanceType("1");
        BigDecimal bigDecimalCome = iSysOaFinanceService.selectFinanceByProjectId(boOne);
        //出账总额
        SysOaFinanceBo boZero = bo;
        boZero.setFinanceType("0");
        BigDecimal bigDecimalOut = iSysOaFinanceService.selectFinanceByProjectId(boZero);
        //遍历子表，将价格遍历为数组并用map方法求和，该方法类型使用map比较妥当
        Map<String, Object> ajax = new HashMap<>();
        ajax.put("funds", sysOaProjectVo.getFunds()); //项目总额
        ajax.put("unliquidated", funds.subtract(bigDecimalCome));  //未结算
        ajax.put("come", bigDecimalCome); //入账
        ajax.put("out", bigDecimalOut);  //出账
        //subtract方法计算差
        ajax.put("check", bigDecimalCome.subtract(bigDecimalOut));

        return R.ok("ok", ajax);
    }

    /**
     * 最近6个月的支出/收入情况
     */
    @SaCheckPermission("oa:finance:list")
    @PostMapping("/findFinanceByMonth")
    public R<List<Object>> findFinanceByMonth(SysOaFinanceBo bo) {
        LocalDate now = LocalDate.now();
        /* 建立一个数组 */
        int[] integers = {0,1, 2, 3, 4, 5};
        List<Object> objects = new ArrayList<>();
        /* 开始遍历 */
        for (int j = 0; j<integers.length; j++){
            int i = integers[j];
            LocalDate date = now.plusMonths(-i);
            String onMonth = date.getYear() + "年" + date.getMonthValue() + "月";
            //上个月的第一天,再上个月继续加1
            LocalDate with = now.minus(i, ChronoUnit.MONTHS).with(TemporalAdjusters.firstDayOfMonth());
            String firstDay = with + " 00:00:00";
            LocalDate with1 = now.minus(i, ChronoUnit.MONTHS).with(TemporalAdjusters.lastDayOfMonth());
            String lastDay = with1 + " 23:59:59";

            //支出
            Map<String, Object> ajax = new HashMap<>();
            Map<String, Object> outMap = new HashMap<>();
            outMap.put("financeType", '0');
            outMap.put("beginTime", firstDay);
            outMap.put("endTime", lastDay);
            BigDecimal financeOutReduce = iSysOaFinanceService.findFinanceByTime(bo, outMap);

            //收入
            Map<String, Object> comeMap = new HashMap<>();
            comeMap.put("financeType", '1');
            comeMap.put("beginTime", firstDay);
            comeMap.put("endTime", lastDay);
            BigDecimal financeComeReduce = iSysOaFinanceService.findFinanceByTime(bo, comeMap);
            //收入减支出
            BigDecimal subtract = financeComeReduce.subtract(financeOutReduce);



            ajax.put("onMonth", onMonth);
            ajax.put("financeOut", financeOutReduce);
            ajax.put("financeCome", financeComeReduce);
            ajax.put("count", subtract);

            objects.add(ajax);
        }
        return R.ok(objects);
    }

    /**
     * 导出进出账管理列表
     */
    @SaCheckPermission("oa:finance:export")
    @Log(title = "进出账管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SysOaFinanceBo bo, HttpServletResponse response) {
        List<SysOaFinanceVo> list = iSysOaFinanceService.queryList(bo);
        ExcelUtil.exportExcel(list, "进出账管理", SysOaFinanceVo.class, response);
    }

    /**
     * 根据id获取进出账管理详细信息
     *
     * @param financeId 主键
     */
    @SaCheckPermission("oa:finance:query")
    @GetMapping("/{financeId}")
    public R<SysOaFinance> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long financeId) {
        return R.ok(iSysOaFinanceService.selectFinanceById(financeId));
    }

    /**
     * 新增进出账管理
     */
    @SaCheckPermission("oa:finance:add")
    @Log(title = "进出账管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody SysOaFinanceBo bo) {
        return toAjax(iSysOaFinanceService.insertByBo(bo));
    }

    /**
     * 修改进出账管理
     */
    @SaCheckPermission("oa:finance:edit")
    @Log(title = "进出账管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody SysOaFinanceBo bo) {
        return toAjax(iSysOaFinanceService.updateByBo(bo));
    }

    /**
     * 删除进出账管理
     *
     * @param financeId 主键串
     */
    @SaCheckPermission("oa:finance:remove")
    @Log(title = "进出账管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{financeId}")
    public Boolean removeData(@NotNull(message = "主键不能为空") @PathVariable Long financeId) {
        return iSysOaFinanceService.deleteSysOaFinanceById(financeId);
    }
    /*  已经改造为单条关联删除以下代码不需要了
    @SaCheckPermission("oa:finance:remove")
    @Log(title = "进出账管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{financeIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] financeIds) {
        return toAjax(iSysOaFinanceService.deleteWithValidByIds(Arrays.asList(financeIds), true));
    }*/
}
