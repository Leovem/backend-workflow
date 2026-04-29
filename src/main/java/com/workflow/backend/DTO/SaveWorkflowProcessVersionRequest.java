package com.workflow.backend.DTO;

import java.util.Map;

public class SaveWorkflowProcessVersionRequest {

    private Map<String, Object> graphJson;

    public Map<String, Object> getGraphJson() {
        return graphJson;
    }

    public void setGraphJson(Map<String, Object> graphJson) {
        this.graphJson = graphJson;
    }
}