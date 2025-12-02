//package com.lhj.workflow.controller;
//
//import com.lhj.workflow.utils.RestMessgae;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiImplicitParam;
//import io.swagger.annotations.ApiImplicitParams;
//import io.swagger.annotations.ApiOperation;
//import org.activiti.api.task.runtime.TaskRuntime;
//import org.activiti.bpmn.model.FormProperty;
//import org.activiti.bpmn.model.UserTask;
//import org.activiti.engine.RepositoryService;
//import org.activiti.engine.TaskService;
//import org.activiti.engine.impl.persistence.entity.TaskEntityImpl;
//import org.activiti.engine.task.Task;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///***
// * @description 任务执行
// * @author LHJ
// * @date 2020/2/27 18:18
// */
//
//@RestController
//@Api(tags = "任务相关接口")
//public class TaskController {
//
//    @Autowired
//    private TaskService taskService;
//
//    @Autowired
//    private RepositoryService repositoryService;
//
//
//    @PostMapping(path = "findTaskByAssignee")
//    @ApiOperation(value = "根据流程assignee查询当前人的个人任务", notes = "根据流程assignee查询当前人的个人任务")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "assignee", value = "代理人（当前用户）", dataType = "String", paramType = "query", example = ""),
//    })
//    public RestMessgae findTaskByAssignee(@RequestParam("assignee") String assignee) {
//        RestMessgae restMessgae = new RestMessgae();
//
//        //创建任务查询对象
//        List<Task> taskList;
//        try {
//            taskList = taskService.createTaskQuery()
//                    //指定个人任务查询
//                    .taskAssignee(assignee)
//                    .list();
//        } catch (Exception e) {
//            restMessgae = RestMessgae.fail("查询失败", e.getMessage());
//            e.printStackTrace();
//            return restMessgae;
//        }
//
//        if (taskList != null && taskList.size() > 0) {
//            List<Map<String, String>> resultList = new ArrayList<>();
//            for (Task task : taskList) {
//                Map<String, String> resultMap = new HashMap<>(7);
//                /* 任务ID */
//                resultMap.put("taskID", task.getId());
//
//                /* 任务名称 */
//                resultMap.put("taskName", task.getName());
//
//                /* 任务的创建时间 */
//                resultMap.put("taskCreateTime", task.getCreateTime().toString());
//
//                /* 任务的办理人 */
//                resultMap.put("taskAssignee", task.getAssignee());
//
//                /* 流程实例ID */
//                resultMap.put("processInstanceId", task.getProcessInstanceId());
//
//                /* 执行对象ID */
//                resultMap.put("executionId", task.getExecutionId());
//
//                /* 流程定义ID */
//                resultMap.put("processDefinitionId", task.getProcessDefinitionId());
//                resultList.add(resultMap);
//            }
//            restMessgae = RestMessgae.success("查询成功", resultList);
//        } else {
//            restMessgae = RestMessgae.success("查询成功", null);
//        }
//
//        return restMessgae;
//    }
//
//    @PostMapping(path = "completeTask")
//    @ApiOperation(value = "完成任务", notes = "完成任务，任务进入下一个节点")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "taskId", value = "任务ID", dataType = "String", paramType = "query", example = ""),
//            @ApiImplicitParam(name = "days", value = "请假天数", dataType = "int", paramType = "query", example = ""),
//    })
//    public RestMessgae completeTask(@RequestParam("taskId") String taskId,
//                                    @RequestParam("days") int days) {
//
//        RestMessgae restMessgae = new RestMessgae();
//
//        try {
//            HashMap<String, Object> variables = new HashMap<>(1);
//            variables.put("days", days);
//            taskService.complete(taskId, variables);
//        } catch (Exception e) {
//            restMessgae = RestMessgae.fail("提交失败", e.getMessage());
//            e.printStackTrace();
//            return restMessgae;
//        }
//        restMessgae = RestMessgae.fail("提交成功", taskId);
//        return restMessgae;
//    }
//
//    @PostMapping(path = "getTaskProperties")
//    @ApiOperation(value = "获取节点动态表单属性", notes = "获取节点动态表单属性")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "taskId", value = "任务ID", dataType = "String", paramType = "query", example = ""),
//    })
//    public RestMessgae getTaskProperties (@RequestParam("taskId") String taskId) {
//
//        RestMessgae restMessgae = new RestMessgae();
//        List<FormProperty> formProperties = null;
//        try {
//            TaskEntityImpl task = (TaskEntityImpl) taskService.createTaskQuery().taskId(taskId).singleResult();
//            UserTask userTask =(UserTask)repositoryService.getBpmnModel(task.getProcessDefinitionId()).getFlowElement(task.getTaskDefinitionKey());
//            formProperties = userTask.getFormProperties();
//        } catch (Exception e) {
//            restMessgae = RestMessgae.fail("提交失败", e.getMessage());
//            e.printStackTrace();
//            return restMessgae;
//        }
//        restMessgae = RestMessgae.fail("提交成功", formProperties);
//        return restMessgae;
//    }
//}
