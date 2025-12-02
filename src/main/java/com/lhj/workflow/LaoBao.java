package com.lhj.workflow;

import com.lhj.workflow.user.AssigneeUtils;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LaoBao {
    public static void main(String[] args) throws Exception {

        ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
                .setJdbcUrl("jdbc:mysql://9.134.94.134:3306/activity?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8&nullCatalogMeansCurrent=true")
                .setJdbcUsername("root")
                .setJdbcPassword("ProjectFuture@2024")
                .setJdbcDriver("com.mysql.cj.jdbc.Driver")
                .setDatabaseType("mysql")
                .setProcessEngineName("activiti")
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

        ProcessEngine processEngine = cfg.buildProcessEngine();
        String pName = processEngine.getName();
        String ver = ProcessEngine.VERSION;

        System.out.println("ProcessEngine [" + pName + "] Version: [" + ver + "]");
        System.out.println();

        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 流程部署
        Deployment deployment = repositoryService
                .createDeployment()
                .addClasspathResource("LaoBao.bpmn")
                .name("老鲍的示例流程")
                .category("开户")
                .key("acopen-demo")
                .tenantId("hkvb")
                .deploy();

        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println("【流程部署】 LaoBao.bpmn: ");
        System.out.printf("Name: %s, ID: %s, Key: %s, Category: %s\n", deployment.getName(), deployment.getId(), deployment.getKey(), deployment.getCategory());

        ProcessDefinition processDefinition = repositoryService
                .createProcessDefinitionQuery()
                .deploymentId(deployment.getId())
                .singleResult();

        System.out.println("=========================================================================================");
        System.out.println("【流程定义】 LaoBao.bpmn: ");
        System.out.println("流程名称 ： [" + processDefinition.getName() + "]， 流程ID ： [" + processDefinition.getId() + "], 流程KEY : " + processDefinition.getKey());

        // 启动流程
        RuntimeService runtimeService = processEngine.getRuntimeService();

        // 分配任务的人员
        Map<String, Object> assigneeMap = new HashMap<String, Object>();
        assigneeMap.put("ops", AssigneeUtils.opsAsignees);

//        identityService.setAuthenticatedUserId("createUserId");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKeyAndTenantId("acopen-demo", "CorpAcOpen", assigneeMap, "hkvb");

        System.out.println("流程实例ID = " + processInstance.getId());
        System.out.println("正在活动的流程节点ID = " + processInstance.getActivityId());
        System.out.println("流程定义ID = " + processInstance.getProcessDefinitionId());

        // 查询指定人的任务
        // ============ 会签任务开始 ===========
        Map mapConfirm = new HashMap();
        mapConfirm.put("confirmSts", 1);

        TaskService taskService = processEngine.getTaskService();

        List<Task> taskList = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
        System.out.println("---------------------------------------");
        System.out.println("taskList1 = " + taskList);

        Task task = taskList.get(0);
        taskService.claim(task.getId(), "tom");
        taskService.setVariablesLocal(task.getId(), mapConfirm);
        taskService.complete(task.getId());

        System.out.printf("Task 1: %s, 处理人: %s\n", task.getName(), task.getAssignee());

        taskList = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
        System.out.println("---------------------------------------");
        System.out.println("taskList2 = " + taskList);
        Map mapConfirm1 = new HashMap();
        mapConfirm1.put("confirmSts", 1);
        Task task2 = taskList.get(0);
        taskService.setVariablesLocal(task2.getId(), mapConfirm1);
        taskService.complete(task2.getId());

        taskList = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
        System.out.println("---------------------------------------");
        System.out.println("taskList3 = " + taskList);

        Task task3 = taskList.get(0);
        taskService.setVariablesLocal(task3.getId(), mapConfirm1);
        taskService.complete(task3.getId());
        // ============ 会签任务结束 ===========

        // 部门主任
        List<Task> taskListDept1 = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
        System.out.println("---------------------------------------");
        System.out.println("taskLis4 = " + taskListDept1);

        task = taskListDept1.get(0);
        taskService.addComment(task.getId(), processInstance.getId(), "同意");
        taskService.complete(taskListDept1.get(0).getId());

        System.out.println("---------------------------------------");


        // ==================流程结束======================

        // 历史任务查询
        List<HistoricActivityInstance> historicActivityInstances = processEngine.getHistoryService()
                // 创建历史活动实例查询
                .createHistoricActivityInstanceQuery()
                //.finished() // 查询已经完成的任务
                .orderByHistoricActivityInstanceEndTime()
                .asc()
                .list();

        for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
            System.out.println("任务ID:" + historicActivityInstance.getId());
            System.out.println("流程实例ID:" + historicActivityInstance.getProcessInstanceId());
            System.out.println("活动名称：" + historicActivityInstance.getActivityName());
            System.out.println("办理人：" + historicActivityInstance.getAssignee());
            System.out.println("开始时间：" + historicActivityInstance.getStartTime());
            System.out.println("结束时间：" + historicActivityInstance.getEndTime());
            System.out.println("===========================");
        }

        for (HistoricTaskInstance historicTaskInstance : processEngine.getHistoryService().createHistoricTaskInstanceQuery().taskDefinitionKey("usertask1").list()) {
            System.out.println("historicTaskInstance = " + historicTaskInstance);
        }

    }
}
