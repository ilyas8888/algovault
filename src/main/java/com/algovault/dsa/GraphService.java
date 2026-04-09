package com.algovault.dsa;

import java.util.*;

import org.springframework.stereotype.Service;

@Service
public class GraphService {
    public List<Integer> dfs(Map<Integer, List<Integer>> graph, int startNode) {
        List<Integer> result = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        dfsHelper(graph, startNode, visited, result);
        return result;
    }

    private void dfsHelper(Map<Integer, List<Integer>> graph, int node, Set<Integer> visited, List<Integer> result) {
        visited.add(node);
        result.add(node);
        List<Integer> neighbors = graph.getOrDefault(node, Collections.emptyList());
        for (int neighbor : neighbors) {
            if (!visited.contains(neighbor)) {
                dfsHelper(graph, neighbor, visited, result);
            }
        }
    }

    public List<Integer> bfs(Map<Integer, List<Integer>> graph, int startNode) {
        List<Integer> result = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();

        visited.add(startNode);
        queue.add(startNode);

        while (!queue.isEmpty()) {
            int node = queue.poll();
            result.add(node);

            List<Integer> neighbors = graph.getOrDefault(node, Collections.emptyList());
            for (int neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }
        return result;
    }

    public Map<Integer, Integer> dijkstra(Map<Integer, Map<Integer, Integer>> graph, int startNode) {
        Map<Integer, Integer> distances = new HashMap<>();
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));

        for (int node : graph.keySet()) {
            distances.put(node, Integer.MAX_VALUE);
        }
        distances.put(startNode, 0);
        pq.add(new int[]{startNode, 0});

        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int node = current[0];
            int dist = current[1];

            if (dist > distances.get(node)) continue;

            Map<Integer, Integer> neighbors = graph.getOrDefault(node, Collections.emptyMap());
            for (Map.Entry<Integer, Integer> entry : neighbors.entrySet()) {
                int neighbor = entry.getKey();
                int newDist = distances.get(node) + entry.getValue();
                if (distances.containsKey(neighbor) && newDist < distances.get(neighbor)) {
                    distances.put(neighbor, newDist);
                    pq.add(new int[]{neighbor, newDist});
                }
            }
        }
        return distances;
    }
}
