package com.algovault.dsa;
import java.util.List;
import java.util.Map;


public class GraphRequest {
 private  Map<Integer, List<Integer>> graph;
 private int startNode;

    public Map<Integer, List<Integer>> getGraph() {
        return graph;
    }

    public void setGraph(Map<Integer, List<Integer>> graph) {
        this.graph = graph;
    }

    public int getStartNode() {
        return startNode;
    }

    public void setStartNode(int startNode) {
        this.startNode = startNode;
    }
}
