package com.algovault.dsa;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dsa/graph")
public class GraphController {

    private final GraphService graphService;

    public GraphController(GraphService graphService) {
        this.graphService = graphService;
    }

    @PostMapping("/dfs")
    public List<Integer> dfs(@RequestBody GraphRequest request) {
        return graphService.dfs(request.getGraph(), request.getStartNode());
    }

    @PostMapping("/bfs")
    public List<Integer> bfs(@RequestBody GraphRequest request) {
        return graphService.bfs(request.getGraph(), request.getStartNode());
    }

    @PostMapping("/dijkstra")
    public Map<Integer, Integer> dijkstra(@RequestBody DijkstraRequest request) {
        return graphService.dijkstra(request.getGraph(), request.getStartNode());
    }
}
