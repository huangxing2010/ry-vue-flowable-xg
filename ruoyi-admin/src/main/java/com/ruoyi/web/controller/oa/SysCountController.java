package com.ruoyi.web.controller.oa;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.PageQuery;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.flowable.core.domain.ProcessQuery;
import com.ruoyi.workflow.domain.vo.WfTaskVo;
import com.ruoyi.workflow.service.IWfProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/workflow")
@RestController
public class SysCountController extends BaseController {


    private final IWfProcessService processService;


    /**
     * 我待流程统计
     * @param processQuery
     * @return
     */
    @SaCheckPermission("workflow:process:ownList")
    @GetMapping(value = "/ownListCount")
    public R ownListCount(ProcessQuery processQuery){
        List<WfTaskVo> wfTaskVos = processService.selectOwnProcessList(processQuery);
        long count = wfTaskVos.stream().count();
        return R.ok(count);
    }

    /**
     * 已办任务统计
     * @param processQuery
     * @return
     */
    @SaCheckPermission("workflow:process:finishedList")
    @GetMapping(value = "/finishedCount")
    public R finishedCount(ProcessQuery processQuery){
        List<WfTaskVo> wfTaskVos = processService.selectFinishedProcessList(processQuery);
        long count = wfTaskVos.stream().count();
        return R.ok(count);
    }

    /**
     * 待办任务统计
     * @param processQuery
     * @return
     */
    @SaCheckPermission("workflow:process:todoList")
    @GetMapping(value = "/todoListCount")
    public R todoListCount(ProcessQuery processQuery){
        List<WfTaskVo> wfTaskVos = processService.selectTodoProcessList(processQuery);
        long count = wfTaskVos.stream().count();
        return R.ok(count);
    }



}
