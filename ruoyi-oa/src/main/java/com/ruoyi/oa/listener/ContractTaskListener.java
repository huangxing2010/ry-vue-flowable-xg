package com.ruoyi.oa.listener;

import ch.qos.logback.core.util.ContextUtil;
import com.ruoyi.oa.domain.bo.SysOaContractBo;
import com.ruoyi.oa.service.ISysOaContractService;
import com.ruoyi.oa.service.impl.SysOaBidServiceImpl;
import lombok.AllArgsConstructor;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngines;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component(value = "contractTaskListener")
@AllArgsConstructor
public class ContractTaskListener implements TaskListener {

//    private final ISysOaContractService sysOaContractService;

    @Override
    public void notify(DelegateTask delegateTask) {
        //TODO 实现你的任务监听器逻辑
        System.out.println("执行任务监听器...");
        System.out.println("delegateTask===" + delegateTask.getId());
        Map<String, Object> variables = delegateTask.getVariables();
        System.out.println("SysOaContractBo===" + variables);
//        sysOaContractService.inserByProcess(variables);






    }
}
