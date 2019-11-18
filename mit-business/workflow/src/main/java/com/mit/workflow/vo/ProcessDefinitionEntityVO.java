package com.mit.workflow.vo;

import lombok.Data;
import org.flowable.engine.impl.persistence.entity.ProcessDefinitionEntityImpl;

import java.io.Serializable;
import java.util.Date;

@Data
public class ProcessDefinitionEntityVO implements Serializable {

    private static final long serialVersionUID = 1L;

    String  id;
    String  name;
    String  key;
    String  category;
    String  categoryName;
    Integer version;
    String  deploymentId;
    String  resourceName;
    String  diagramResourceName;
    Integer suspensionState;
    Date deploymentTime;

    public ProcessDefinitionEntityVO(ProcessDefinitionEntityImpl procDef) {
        this.id = procDef.getId();
        this.name = procDef.getName();
        this.key = procDef.getKey();
        this.version = procDef.getVersion();
        this.deploymentId = procDef.getDeploymentId();
        this.resourceName = procDef.getResourceName();
        this.diagramResourceName = procDef.getDiagramResourceName();
        this.suspensionState = procDef.getSuspensionState();
    }

}