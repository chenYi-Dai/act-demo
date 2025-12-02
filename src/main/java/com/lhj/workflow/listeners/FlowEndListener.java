package com.lhj.workflow.listeners;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

public class FlowEndListener implements ExecutionListener {

    public void notify(DelegateExecution execution) {
        System.out.println("【FLOW END】");
    }
}
