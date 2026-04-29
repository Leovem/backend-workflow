package com.workflow.backend.DTO;

import java.util.Map;

public class UpdateDiagramSnapshotRequest {

    private Map<String, Object> diagramSnapshot;

    public Map<String, Object> getDiagramSnapshot() {
        return diagramSnapshot;
    }

    public void setDiagramSnapshot(Map<String, Object> diagramSnapshot) {
        this.diagramSnapshot = diagramSnapshot;
    }
}