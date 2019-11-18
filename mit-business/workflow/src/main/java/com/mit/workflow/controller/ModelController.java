package com.mit.workflow.controller;

import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mit.common.web.Result;
import com.mit.workflow.dto.ModelDTO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.BpmnAutoLayout;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.editor.constants.StencilConstants;
import org.flowable.editor.language.json.converter.BpmnJsonConverter;
import org.flowable.editor.language.json.converter.util.CollectionUtils;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.ui.common.security.SecurityUtils;
import org.flowable.ui.common.service.exception.BadRequestException;
import org.flowable.ui.common.service.exception.BaseModelerRestException;
import org.flowable.ui.common.service.exception.ConflictingRequestException;
import org.flowable.ui.common.service.exception.InternalServerErrorException;
import org.flowable.ui.common.service.exception.NotFoundException;
import org.flowable.ui.common.util.XmlUtil;
import org.flowable.ui.modeler.domain.AbstractModel;
import org.flowable.ui.modeler.domain.Model;
import org.flowable.ui.modeler.model.ModelKeyRepresentation;
import org.flowable.ui.modeler.model.ModelRepresentation;
import org.flowable.ui.modeler.repository.ModelRepository;
import org.flowable.ui.modeler.repository.ModelSort;
import org.flowable.ui.modeler.service.FlowableModelQueryService;
import org.flowable.ui.modeler.serviceapi.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.Date;
import java.util.UUID;

/**
 * @Description 流程模型定义
 */
@Slf4j
@Api(tags = "流程定义")
@Controller
@RequestMapping("/model")
public class ModelController {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private FlowableModelQueryService flowableModelQueryService;

    @Autowired
    private ModelService modelService;

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private BpmnXMLConverter bpmnXmlConverter = new BpmnXMLConverter();
    private BpmnJsonConverter bpmnJsonConverter = new BpmnJsonConverter();

    @GetMapping(value = "/design")
    public String getModelEditorPage() {
        return "index";
    }

    @GetMapping(value = "/{modelId}")
    public @ResponseBody Result getModelById(@PathVariable String modelId) {
        ModelRepresentation modelRepresentation = null;
        try {
            modelRepresentation = modelService.getModelRepresentation(modelId);
        } catch (NotFoundException e) {
            return Result.failed("不存在的模型");
        }
        return Result.succeed(modelRepresentation);
    }

    @GetMapping(value = "/list")
    public @ResponseBody Result getModels(HttpServletRequest request) {
        return Result.succeed(flowableModelQueryService.getModels("process", ModelSort.MODIFIED_DESC,
                AbstractModel.MODEL_TYPE_BPMN, request));
    }

    /**
     * 新增模型
     * @param modelDTO 新增模型参数对象
     * @return Result<ModelRepresentation>
     */
    @PostMapping
    public @ResponseBody Result addModel(@RequestBody @Valid ModelDTO modelDTO) {
        modelDTO.setKey(modelDTO.getKey().replaceAll(" ", ""));
        try {
            // 检查key是否存在
            checkForDuplicateKey(modelDTO);
        } catch (ConflictingRequestException e) {
            return Result.failed(e.getMessage());
        }

        ModelRepresentation modelRepresentation = new ModelRepresentation();
        BeanUtil.copyProperties(modelDTO, modelRepresentation);
        modelRepresentation.setModelType(AbstractModel.MODEL_TYPE_BPMN);
        String json = modelService.createModelJson(modelRepresentation);
        Model newModel = modelService.createModel(modelRepresentation, json, SecurityUtils.getCurrentUserObject());
        return Result.succeed(new ModelRepresentation(newModel));
    }

    /**
     * 修改模型
     * @param modelDTO 参数对象
     * @return Result<ModelRepresentation>
     */
    @PutMapping
    public @ResponseBody Result updateModel(@RequestBody @Valid ModelDTO modelDTO) {
        if (StringUtils.isEmpty(modelDTO.getModelId())) {
            return Result.failed("参数不符，modelId必传");
        }
        Model model = modelService.getModel(modelDTO.getModelId());
        if (null == model) {
            return Result.failed("模型不存在");
        }

        modelDTO.setKey(modelDTO.getKey().replaceAll(" ", ""));
        try {
            // 检查key是否存在
            checkForDuplicateKey(modelDTO);
        } catch (ConflictingRequestException e) {
            return Result.failed(e.getMessage());
        }

        model.setName(modelDTO.getName());
        model.setKey(modelDTO.getKey());
        model.setDescription(modelDTO.getDescription());
        model.setLastUpdated(new Date());
        model.setLastUpdatedBy(SecurityUtils.getCurrentUserId());

        try {
            if (model.getModelType() != null) {
                ObjectNode modelNode = (ObjectNode) objectMapper.readTree(model.getModelEditorJson());
                modelNode.put("name", model.getName());
                modelNode.put("key", model.getKey());

                if (AbstractModel.MODEL_TYPE_BPMN == model.getModelType()) {
                    ObjectNode propertiesNode = (ObjectNode) modelNode.get("properties");
                    propertiesNode.put("process_id", model.getKey());
                    propertiesNode.put("name", model.getName());
                    if (StringUtils.isNotEmpty(model.getDescription())) {
                        propertiesNode.put("documentation", model.getDescription());
                    }
                    modelNode.set("properties", propertiesNode);
                }
                model.setModelEditorJson(modelNode.toString());
            }
            modelRepository.save(model);
        } catch (Exception e) {
            return Result.failed("更新失败" + e.getMessage());
        }
        return Result.succeed(new ModelRepresentation(model));
    }

    private void checkForDuplicateKey(ModelDTO modelDTO) {
        ModelKeyRepresentation modelKeyInfo = modelService.validateModelKey(null, AbstractModel.MODEL_TYPE_BPMN,
                modelDTO.getKey());
        if (modelKeyInfo.isKeyAlreadyExists()) {
            throw new ConflictingRequestException("模型key值已存在: " + modelDTO.getKey());
        }
    }

    /**
     * 删除模型
     * @param modelId 模型ID
     * @return Result
     */
    @DeleteMapping(value = "/{modelId}")
    public @ResponseBody Result deleteModel(@PathVariable String modelId) {
        Model model = modelService.getModel(modelId);
        if (null == model) {
            return Result.failed("模型不存在");
        }
        try {
            modelService.deleteModel(model.getId());
        } catch (Exception e) {
            log.error("Error while deleting: ", e);
            return Result.failed("删除失败" + e.getMessage());
        }
        return Result.succeed();
    }

    /**
     * 根据模型部署流程
     * @param modelId 模型ID
     * @return Result
     */
    @PostMapping(value = "/{modelId}/deploy")
    public @ResponseBody Result deploy(@PathVariable String modelId) {
        if (StringUtils.isBlank(modelId)) {
            return Result.failed("参数错误");
        }
        Model modelData = modelService.getModel(modelId);
        if (modelData == null) {
            return Result.failed("模型不存在");
        }

        try {
            byte[] bpmnBytes = modelService.getBpmnXML(modelData);
            String processName = modelData.getName() + ".bpmn20.xml";
            Deployment deployment = repositoryService.createDeployment().name(modelData.getName())
                    .key(modelData.getKey())
                    .addString(processName, new String(bpmnBytes)).deploy();
            return Result.succeed(deployment.getId());
        } catch (Exception e) {
            return Result.failed("根据模型部署流程失败"+e.getMessage());
        }
    }

    /**
     * 导入模型
     * @param file 文件名
     * @param request 请求
     * @return Result
     */
    @PostMapping(value = "/import-process-model")
    public @ResponseBody Result importProcessModel(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        String fileName = file.getOriginalFilename();
        if (StringUtils.isEmpty(fileName) || (!fileName.endsWith(".bpmn") && !fileName.endsWith(".bpmn20.xml"))) {
            return Result.failed("不支持的文件，仅支持.bpmn和.bpmn20.xml文件");
        }
        try {
            XMLInputFactory xif = XmlUtil.createSafeXmlInputFactory();
            InputStreamReader xmlIn = new InputStreamReader(file.getInputStream(), "UTF-8");
            XMLStreamReader xtr = xif.createXMLStreamReader(xmlIn);
            BpmnModel bpmnModel = bpmnXmlConverter.convertToBpmnModel(xtr);
            if (CollectionUtils.isEmpty(bpmnModel.getProcesses())) {
                return Result.failed("找不到定义的流程 " + fileName);
            }

            if (bpmnModel.getLocationMap().size() == 0) {
                BpmnAutoLayout bpmnLayout = new BpmnAutoLayout(bpmnModel);
                bpmnLayout.execute();
            }

            ObjectNode modelNode = bpmnJsonConverter.convertToJson(bpmnModel);

            org.flowable.bpmn.model.Process process = bpmnModel.getMainProcess();
            String name = process.getId();
            if (StringUtils.isNotEmpty(process.getName())) {
                name = process.getName();
            }
            String description = process.getDocumentation();

            ModelRepresentation model = new ModelRepresentation();
            model.setKey(process.getId());
            model.setName(name);
            model.setDescription(description);
            model.setModelType(AbstractModel.MODEL_TYPE_BPMN);

            ModelKeyRepresentation modelKeyInfo = modelService.validateModelKey(null, AbstractModel.MODEL_TYPE_BPMN,
                    process.getId());
            if (modelKeyInfo.isKeyAlreadyExists()) {
                String key = UUID.randomUUID().toString();
                ObjectNode propertiesNode = (ObjectNode) modelNode.get("properties");
                propertiesNode.put(StencilConstants.PROPERTY_PROCESS_ID, key);
            }

            Model newModel = modelService.createModel(model, modelNode.toString(), SecurityUtils.getCurrentUserObject());
            return Result.succeed(new ModelRepresentation(newModel));
        } catch (BadRequestException e) {
            return Result.failed("导入失败 " + e.getMessage());
        } catch (Exception e) {
            log.error("导入失败，文件名: {} \r\n {}", fileName, e);
            return Result.failed("导入失败, " + e.getMessage());
        }
    }

    /**
     * 导出 bpmn 2.0 xml文档
     * @param modelId 模型ID
     * @param response HttpServletResponse
     */
    @GetMapping(value = "/{modelId}/bpmn20")
    public @ResponseBody void getProcessModelBpmn20Xml(@PathVariable String modelId, HttpServletResponse response) {
        if (StringUtils.isEmpty(modelId)) {
            throw new BadRequestException("参数错误，模型Id必传");
        }
        Model model = modelService.getModel(modelId);

        String name = model.getName().replaceAll(" ", "_") + ".bpmn20.xml";
        String encodedName = null;
        try {
            encodedName = "UTF-8''" + URLEncoder.encode(name, "UTF-8");
        } catch (Exception e) {
            log.warn("Failed to encode name " + name);
        }

        String contentDispositionValue = "attachment; filename=" + name;
        if (encodedName != null) {
            contentDispositionValue += "; filename*=" + encodedName;
        }

        response.setHeader("Content-Disposition", contentDispositionValue);
        if (model.getModelEditorJson() != null) {
            try {
                ServletOutputStream servletOutputStream = response.getOutputStream();
                response.setContentType("application/xml");

                BpmnModel bpmnModel = modelService.getBpmnModel(model);
                byte[] xmlBytes = modelService.getBpmnXML(bpmnModel);
                BufferedInputStream in = new BufferedInputStream(new ByteArrayInputStream(xmlBytes));

                byte[] buffer = new byte[8096];
                while (true) {
                    int count = in.read(buffer);
                    if (count == -1) {
                        break;
                    }
                    servletOutputStream.write(buffer, 0, count);
                }
                // Flush and close stream
                servletOutputStream.flush();
                servletOutputStream.close();
            } catch (BaseModelerRestException e) {
                throw e;
            } catch (Exception e) {
                log.error("Could not generate BPMN 2.0 XML", e);
                throw new InternalServerErrorException("Could not generate BPMN 2.0 xml");
            }
        }
    }


}
