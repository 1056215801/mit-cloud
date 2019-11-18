package com.mit.workflow.controller;

import com.google.common.collect.Sets;
import com.mit.common.utils.SerializeUtils;
import com.mit.common.web.Result;
import com.mit.workflow.constant.FlowConstant;
import com.mit.workflow.utils.IOUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.HistoryService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.ui.common.security.SecurityUtils;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.flowable.variable.service.impl.persistence.entity.HistoricVariableInstanceEntityImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 流程实例操作
 */
@Slf4j
@Api(tags = "流程实例")
@RestController
@RequestMapping("/process-instance")
public class ProcessInstanceController {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Resource
    private ProcessEngine processEngine;

    @Autowired
    private HistoryService historyService;

    /**
     * 启动流程
     * @param procDefKey 流程key
     * @return 流程实例ID
     */
    @PostMapping(value = "/start")
    public Result startInstance(@RequestParam String procDefKey, String formData) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(procDefKey).latestVersion().singleResult();
        if (null == processDefinition) {
            return Result.failed("不存在的流程或流程未部署");
        }

        Map<String, Object> vars = new HashMap<>();
        vars.put(FlowConstant.SUBMITTER, SecurityUtils.getCurrentUserId());
        vars.put(FlowConstant.FORM_DATA, formData);
        Authentication.setAuthenticatedUserId(SecurityUtils.getCurrentUserId());
        return Result.succeed((Object) runtimeService.startProcessInstanceByKey(procDefKey, vars).getId());
    }

    /**
     * 获取流程实例的流程图
     * @param processInstanceId 流程实例ID
     * @param response response
     * @throws Exception
     */
    @GetMapping(value = "/{processInstanceId}/diagram")
    public Result genProcessInstanceDiagram(@PathVariable String processInstanceId, HttpServletResponse response) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        // 流程走完的不显示图
        if (processInstance == null) {
            return Result.failed("流程实例不存在或已结束");
        }

        InputStream in = getProcessDiagramInput(processInstance);
        response.setHeader("Content-Type", "image/jpeg");
        try {
            IOUtils.transferInputStream2Res(in, response);
        } catch (Exception e) {
            return Result.failed("获取流程图失败，" + e.getMessage());
        }
        return null;
    }

    private InputStream getProcessDiagramInput(ProcessInstance processInstance) {
        // 查询正在执行的执行对象表
        List<Execution> executions = runtimeService.createExecutionQuery()
                .processInstanceId(processInstance.getId()).list();

        // 得到正在执行的Activity的Id
        List<String> activityIds = new ArrayList<>();
        List<String> flows = new ArrayList<>();
        for (Execution exe : executions) {
            List<String> ids = runtimeService.getActiveActivityIds(exe.getId());
            activityIds.addAll(ids);
        }

        //获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
        ProcessEngineConfiguration engConf = processEngine.getProcessEngineConfiguration();
        ProcessDiagramGenerator diagramGenerator = engConf.getProcessDiagramGenerator();

        return diagramGenerator.generateDiagram(bpmnModel, "png", activityIds, flows,
                engConf.getActivityFontName(), engConf.getLabelFontName(), engConf.getAnnotationFontName(),
                engConf.getClassLoader(), 1.0,false);
    }

    /**
     * 获取流转历史
     * @param processInstanceId 流程实例ID
     * @return
     */
    @GetMapping(value = "/{processInstanceId}/flow-historic")
    public Result getProcInsFlowHistoric(@PathVariable String processInstanceId) {
        String[] activityTypes = {"startEvent", "userTask", "endEvent"};
        List<HistoricActivityInstance> historicActivityInstances = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId).activityTypes(Sets.newHashSet(activityTypes))
                .orderByHistoricActivityInstanceStartTime().asc().list();

        return Result.succeed(historicActivityInstances);
    }

    /**
     * 获取表单数据
     * @param processInstanceId
     * @return
     */
    @GetMapping(value = "/{processInstanceId}/form-data")
    public Result getFormData(@PathVariable String processInstanceId) {
        Object object = null;
        List<HistoricVariableInstance> list = historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(processInstanceId).list();
        for (HistoricVariableInstance historicVariableInstance : list) {
            if (FlowConstant.FORM_DATA.equals(historicVariableInstance.getVariableName())) {
                object = historicVariableInstance.getValue();
            }
        }
        System.out.println(object);
        return Result.succeed(object);
    }

}
