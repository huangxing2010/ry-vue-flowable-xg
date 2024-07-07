package com.ruoyi.oa.listener;

import com.ruoyi.oa.config.ContextUtil;
import com.ruoyi.oa.domain.bo.SysOaContractBo;
import com.ruoyi.oa.service.ISysOaContractService;
import com.ruoyi.oa.service.impl.SysOaContractServiceImpl;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ContractListener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        if (EVENTNAME_COMPLETE.equals(delegateTask.getEventName())){
            System.out.println("--- 任务完成 ---");
            Map<String, Object> variables = delegateTask.getVariables();
            SysOaContractBo sysOaContractBo = new SysOaContractBo();
            sysOaContractBo.setContractName(variables.get("contract_name").toString());
            sysOaContractBo.setContractNum(variables.get("contract_no").toString());
            sysOaContractBo.setFirstName(variables.get("first_name").toString());
            sysOaContractBo.setSecondName(variables.get("second_name").toString());
            sysOaContractBo.setRemark(variables.get("remark").toString());
            ISysOaContractService oaContractService = ContextUtil.getBean(SysOaContractServiceImpl.class);
            oaContractService.insertByBo(sysOaContractBo);
        }
    }
}
