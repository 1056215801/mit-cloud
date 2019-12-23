package com.mit.workflow.controller;

import com.mit.common.web.Result;
import com.mit.workflow.constant.FlowConstant;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.HistoryService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.flowable.ui.common.security.SecurityUtils;
import org.flowable.ui.task.model.runtime.TaskRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 任务管理
 */
@Slf4j
@Api(tags = "任务管理")
@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    /**
     * 获取用户待办任务
     * @param userId 用户ID
     * @param deptId 用户所在部门ID
     * @param pageNumber 当前页
     * @param pageSize 页大小
     * @return
     */
    @GetMapping(value = "/{userId}/todoPageList")
    public Result todoList(@PathVariable String userId, @RequestParam String deptId,
                           @RequestParam int pageNumber, @RequestParam int pageSize) {
        TaskQuery taskQuery = taskService.createTaskQuery().includeProcessVariables().active()
                .or().taskAssignee(userId).taskCandidateUser(userId).taskCandidateGroup(deptId).endOr()
                .orderByTaskCreateTime().desc();
        List<Task> tasks = taskQuery.listPage(pageSize * (pageNumber - 1), pageSize);
        List<TaskRepresentation> taskRepresentations = new ArrayList<>();
        tasks.forEach(task -> taskRepresentations.add(new TaskRepresentation(task)));
        return Result.succeed(taskRepresentations);
    }

    /**
     * 获取用户已办任务
     * @param userId 用户ID
     * @param pageNumber 当前页
     * @param pageSize 页大小
     * @return
     */
    @GetMapping(value = "/{userId}/donePageList")
    public Result doneList(@PathVariable String userId, @RequestParam int pageNumber, @RequestParam int pageSize) {
        HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(userId).finished().includeProcessVariables()
                .orderByHistoricTaskInstanceEndTime().desc();
        List<HistoricTaskInstance> historicTaskInstances = historicTaskInstanceQuery
                .listPage(pageSize * (pageNumber - 1), pageSize);
        List<TaskRepresentation> taskRepresentations = new ArrayList<>();
        historicTaskInstances.forEach(historicTaskInstance -> taskRepresentations.add(
                new TaskRepresentation(historicTaskInstance)));
        return Result.succeed(taskRepresentations);
    }

    /**
     * 获取用户发起的任务
     * @param userId 用户ID
     * @param pageNumber 当前页
     * @param pageSize 页大小
     * @return
     */
    @GetMapping(value = "/{userId}/initiatedPageList")
    public Result initiatedList(@PathVariable String userId, @RequestParam int pageNumber, @RequestParam int pageSize) {
        HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery()
                .startedBy(userId).orderByProcessInstanceStartTime().desc();
        List<HistoricProcessInstance> historicProcessInstances = historicProcessInstanceQuery
                .listPage(pageSize * (pageNumber - 1), pageSize);
        return Result.succeed(historicProcessInstances);
    }

    /**
     * 任务审核
     * @param taskId 任务ID
     * @param formData 参数
     * @return
     */
    @PostMapping(value = "/{taskId}/approve")
    public Result approveTask(@PathVariable String taskId, @RequestParam boolean status, String formData) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            return Result.failed("任务不存在");
        }
        Authentication.setAuthenticatedUserId(SecurityUtils.getCurrentUserId());
        Map<String, Object> variables = new HashMap<>();
        variables.put(FlowConstant.APPROVED, status);
        variables.put(FlowConstant.FORM_DATA, formData);
        taskService.complete(taskId, variables);
        return Result.succeed();
    }

    /**
     * 转派任务
     * @param taskId 任务ID
     * @param userIds 接收人ID
     * @return
     */
    @PostMapping(value = "/{taskId}/transfer")
    public Result transferTask(@PathVariable String taskId, @RequestParam("userIds") String[] userIds) {
        if (null == userIds || userIds.length == 0) {
            return Result.failed("接收人ID不能为空");
        }
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            return Result.failed("任务不存在");
        }
        Authentication.setAuthenticatedUserId(SecurityUtils.getCurrentUserId());
        taskService.addComment(taskId, task.getProcessInstanceId(), "[转派]");
        Arrays.asList(userIds).forEach(userId -> taskService.addCandidateUser(taskId, userId));
        return Result.succeed();
    }
}
