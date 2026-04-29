package com.workflow.backend.DTO;

import java.util.Map;

public class WorkflowProcessEditorResponse {

    private String processId;
    private String processVersionId;
    private String name;
    private String description;
    private Integer versionNumber;
    private String versionStatus;
    private Map<String, Object> graphJson;

    public WorkflowProcessEditorResponse() {}

    public WorkflowProcessEditorResponse(
        String processId,
        String processVersionId,
        String name,
        String description,
        Integer versionNumber,
        String versionStatus,
        Map<String, Object> graphJson
    ) {
        this.processId = processId;
        this.processVersionId = processVersionId;
        this.name = name;
        this.description = description;
        this.versionNumber = versionNumber;
        this.versionStatus = versionStatus;
        this.graphJson = graphJson;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getProcessVersionId() {
        return processVersionId;
    }

    public void setProcessVersionId(String processVersionId) {
        this.processVersionId = processVersionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Integer versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getVersionStatus() {
        return versionStatus;
    }

    public void setVersionStatus(String versionStatus) {
        this.versionStatus = versionStatus;
    }

    public Map<String, Object> getGraphJson() {
        return graphJson;
    }

    public void setGraphJson(Map<String, Object> graphJson) {
        this.graphJson = graphJson;
    }
}