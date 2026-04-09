package com.algovault.dsa;

import java.util.Map;

public class DijkstraRequest {
    private Map<Integer, Map<Integer, Integer>> graph;
    private int startNode;

    public Map<Integer, Map<Integer, Integer>> getGraph() {
        return graph;
    }

    public void setGraph(Map<Integer, Map<Integer, Integer>> graph) {
        this.graph = graph;
    }

    public int getStartNode() {
        return startNode;
    }

    public void setStartNode(int startNode) {
        this.startNode = startNode;
    }
}