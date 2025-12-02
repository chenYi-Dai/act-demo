package com.lhj.workflow.listeners;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

public class RiskListener implements ExecutionListener {

    public void notify(DelegateExecution execution) {
        System.out.println("【Risk】" + execution.getEventName());
    }
}
