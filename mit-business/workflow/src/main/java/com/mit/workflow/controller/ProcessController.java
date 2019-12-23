package com.mit.workflow.controller;

import com.mit.common.web.Result;
import com.mit.workflow.utils.IOUtils;
import com.mit.workflow.vo.ProcessDefinitionEntityVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.impl.persistence.entity.ProcessDefinitionEntityImpl;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.ui.task.model.runtime.ProcessDefinitionRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description 流程管理
 */
@Slf4j
@Api(tags = "流程管理")
@RestController
@RequestMapping("/process")
public class ProcessController {

    // 查看流程定义资源类型
    private static final String PROCESS_RESOURCE_IMG = "img";
    private static final String PROCESS_RESOURCE_XML = "xml";

    @Autowired
    private RepositoryService repositoryService;

    @Resource
    private ProcessEngine processEngine;

    /**
     * 分页获取流程定义
     * @param pageNumber 当前页
     * @param pageSize 页大小
     * @return Result
     */
    @GetMapping(value = "/pageList")
    public Result getProcessDefPageList(@RequestParam int pageNumber, @RequestParam int pageSize) {
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
                .latestVersion().orderByProcessDefinitionKey().asc();
        List<ProcessDefinition> list = processDefinitionQuery.listPage(pageSize * (pageNumber - 1), pageSize);
        List<ProcessDefinitionEntityVO> resultList = new ArrayList<>();
        // 必须要转换，ProcessDefinitionEntity中的List<IdentityLinkEntity> getIdentityLinks()为懒加载，会出现json转换空指针错误
        for(ProcessDefinition processDefinition : list) {
            ProcessDefinitionEntityVO processDefinitionEntityVO = new ProcessDefinitionEntityVO((ProcessDefinitionEntityImpl) processDefinition);
            resultList.add(processDefinitionEntityVO);
        }
        return Result.succeed(resultList);
    }

    /**
     * 删除部署的流程，将级联删除流程实例、流程图等资源
     * @param deploymentId 部署ID
     * @return Result
     */
    @DeleteMapping(value = "/deployment/{deploymentId}")
    public Result deleteDeployment(@PathVariable String deploymentId) {
        Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
        if (null == deployment) {
            return Result.failed("流程不存在");
        }
        repositoryService.deleteDeployment(deploymentId);
        return Result.succeed();
    }

    /**
     * 激活流程
     * @param proDefId 流程定义ID
     * @return Result
     */
    @PostMapping(value = "/{proDefId}/active")
    public Result activeProcessDef(@PathVariable String proDefId) {
        ProcessDefinition processDefinition = repositoryService.getProcessDefinition(proDefId);
        if (null == processDefinition) {
            return Result.failed("流程不存在");
        }
        repositoryService.activateProcessDefinitionById(proDefId, true, new Date());
        return Result.succeed();
    }

    /**
     * 挂起流程
     * @param proDefId 流程定义ID
     * @return Result
     */
    @PostMapping(value = "/{proDefId}/suspend")
    public Result suspendProcessDef(@PathVariable String proDefId) {
        ProcessDefinition processDefinition = repositoryService.getProcessDefinition(proDefId);
        if (null == processDefinition) {
            return Result.failed("流程不存在");
        }
        repositoryService.suspendProcessDefinitionById(proDefId, true, new Date());
        return Result.succeed();
    }

    /**
     * 获取资源，xml或者流程图
     * @param proDefId 流程定义ID
     * @param type 资源类型，为img或xml
     * @return Result
     */
    @GetMapping(value = "/{proDefId}/resource/{type}")
    public Result getProcessResource(@PathVariable String proDefId, @PathVariable String type,
                                     HttpServletResponse response) {
        ProcessDefinition processDefinition = repositoryService.getProcessDefinition(proDefId);
        if (null == processDefinition) {
            return Result.failed("流程不存在");
        }
        if (!PROCESS_RESOURCE_IMG.equals(type) && !PROCESS_RESOURCE_XML.equals(type)) {
            return Result.failed("只支持img、xml类型");
        }
        try {
            if (PROCESS_RESOURCE_IMG.equals(type)) {
                getProcessResImg(proDefId, response);
            } else {
                getProcessResXml(proDefId, response);
            }
        } catch (Exception e) {
            return Result.failed("读取失败，" + e.getMessage());
        }
        return null;
    }

    /*
     * 获取流程图
     */
    private void getProcessResImg(String proDefId, HttpServletResponse response) throws IOException {
        BpmnModel bpmnModel = repositoryService.getBpmnModel(proDefId);
        ProcessEngineConfiguration engConf = processEngine.getProcessEngineConfiguration();
        ProcessDiagramGenerator diagramGenerator = engConf.getProcessDiagramGenerator();
        InputStream in = diagramGenerator.generateDiagram(bpmnModel, "png", engConf.getActivityFontName(),
                engConf.getLabelFontName(), engConf.getAnnotationFontName(), engConf.getClassLoader(), 1.0,false);

        response.setHeader("Content-Type", "image/jpeg");
        IOUtils.transferInputStream2Res(in, response);
    }

    /*
     * 获取xml
     */
    private void getProcessResXml(String proDefId, HttpServletResponse response) throws IOException {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(proDefId).singleResult();
        InputStream in = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(),
                processDefinition.getResourceName());

        response.setHeader("Content-Type", "text/xml");
        IOUtils.transferInputStream2Res(in, response);
    }


}
