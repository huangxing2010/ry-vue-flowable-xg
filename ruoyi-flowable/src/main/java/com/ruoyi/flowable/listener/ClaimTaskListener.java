package com.ruoyi.flowable.listener;

import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.helper.LoginHelper;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.stereotype.Component;

/**
 * 用户任务监听器
 *
 * @author KonBAI
 * @since 2023/5/13
 */
@Component(value = "claimTaskListener")
public class ClaimTaskListener implements TaskListener {
    /**
     * 注入字段（名称与流程设计时字段名称一致）
     */
    // private FixedValue field;
    @Override
    public void notify(DelegateTask delegateTask) {
        //报销审批流转条件  getDeptId
        LoginUser user = LoginHelper.getLoginUser();
        delegateTask.setVariable("deptState", user.getDeptId());
        // TODO 实现你的任务监听器逻辑
        //System.out.println("执行任务监听器..." + user.getDeptId());
    }
}
