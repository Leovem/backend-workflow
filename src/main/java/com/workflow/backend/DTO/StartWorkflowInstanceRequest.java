package com.workflow.backend.DTO;

import java.util.Map;

public class StartWorkflowInstanceRequest {

    private String processId;
    private String processVersionId;
    private String title;
    private Map<String, Object> initialData;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Map<String, Object> getInitialData() {
        return initialData;
    }

    public void setInitialData(Map<String, Object> initialData) {
        this.initialData = initialData;
    }
}