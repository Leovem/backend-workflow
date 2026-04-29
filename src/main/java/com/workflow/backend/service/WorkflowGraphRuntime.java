package com.workflow.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WorkflowGraphRuntime {

    private final List<Map<String, Object>> cells;

    @SuppressWarnings("unchecked")
    public WorkflowGraphRuntime(Map<String, Object> graphJson) {
        Object rawCells = graphJson.get("cells");

        if (rawCells instanceof List<?>) {
            this.cells = (List<Map<String, Object>>) rawCells;
        } else {
            this.cells = new ArrayList<>();
        }
    }

    public Map<String, Object> findInitialNode() {
        return cells.stream()
            .filter(this::isElement)
            .filter(cell -> "initial".equals(asString(cell.get("nodeType"))))
            .findFirst()
            .orElse(null);
    }

    public Map<String, Object> findNextActionNodeFrom(
        Map<String, Object> startNode
    ) {
        String startNodeId = asString(startNode.get("id"));

        if (startNodeId == null) {
            return null;
        }

        String nextNodeId = findFirstTargetFrom(startNodeId);

        while (nextNodeId != null) {
            Map<String, Object> nextNode = findCellById(nextNodeId);

            if (nextNode == null) {
                return null;
            }

            String nodeType = asString(nextNode.get("nodeType"));

            if ("action".equals(nodeType)) {
                return nextNode;
            }

            nextNodeId = findFirstTargetFrom(nextNodeId);
        }

        return null;
    }

    private String findFirstTargetFrom(String sourceNodeId) {
        return cells.stream()
            .filter(this::isLink)
            .filter(link -> {
                Map<String, Object> source = asMap(link.get("source"));
                return source != null && sourceNodeId.equals(asString(source.get("id")));
            })
            .map(link -> {
                Map<String, Object> target = asMap(link.get("target"));
                return target != null ? asString(target.get("id")) : null;
            })
            .filter(id -> id != null)
            .findFirst()
            .orElse(null);
    }

    private Map<String, Object> findCellById(String id) {
        return cells.stream()
            .filter(cell -> id.equals(asString(cell.get("id"))))
            .findFirst()
            .orElse(null);
    }

    private boolean isElement(Map<String, Object> cell) {
        return !isLink(cell);
    }

    private boolean isLink(Map<String, Object> cell) {
        return "link".equals(asString(cell.get("nodeType")));
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> asMap(Object value) {
        if (value instanceof Map<?, ?>) {
            return (Map<String, Object>) value;
        }

        return null;
    }

    private String asString(Object value) {
        return value != null ? String.valueOf(value) : null;
    }

    public Map<String, Object> findNextActionNodeAfter(String currentNodeId) {
    if (currentNodeId == null) {
        return null;
    }

    String nextNodeId = findFirstTargetFrom(currentNodeId);

    while (nextNodeId != null) {
        Map<String, Object> nextNode = findCellById(nextNodeId);

        if (nextNode == null) {
            return null;
        }

        String nodeType = asString(nextNode.get("nodeType"));

        if ("action".equals(nodeType)) {
            return nextNode;
        }

        if ("activityFinal".equals(nodeType) || "flowFinal".equals(nodeType)) {
            return null;
        }

        nextNodeId = findFirstTargetFrom(nextNodeId);
    }

    return null;
}
}