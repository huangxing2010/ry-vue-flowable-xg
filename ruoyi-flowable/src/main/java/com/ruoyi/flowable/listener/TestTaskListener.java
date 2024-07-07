package com.ruoyi.flowable.listener;


import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;

import java.util.Map;


public class TestTaskListener implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        // 事件名称如下:
//        EVENTNAME_CREATE 任务创建后
//        EVENTNAME_COMPLETE 任务完成后
//        EVENTNAME_DELETE 任务删除后
//        EVENTNAME_ASSIGNMENT 任务指派后
        if (EVENTNAME_COMPLETE.equals(delegateTask.getEventName()))
        {
            // 任务完成
            System.out.println("--- 任务完成 ---");
            Map<String, Object> variables = delegateTask.getVariables();

        }
    }
}
