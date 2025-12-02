package com.lhj.workflow.listeners;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.impl.el.FixedValue;

public class GatewayListener implements ExecutionListener {

    private FixedValue status;

    public FixedValue getStatus() {
        return status;
    }

    public void setStatus(FixedValue status) {
        this.status = status;
    }

    @Override
    public void notify(DelegateExecution execution) {
        System.out.println("【Gateway】 " + status.getExpressionText() + ", 【EVENT】 " + execution.getEventName());
    }
}
