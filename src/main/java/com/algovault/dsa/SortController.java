package com.algovault.dsa;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dsa/sort")
public class SortController {
    private final SortService sortService;
    public SortController(SortService sortService) {
        this.sortService = sortService;
    }
    @PostMapping("/bubble")
    public int[] BubbleSort(@RequestBody SortRequest request) {
        return sortService.bubbleSort(request.getArray());
    }

    @PostMapping("/merge")
    public ResponseEntity<int[]> MergeSort(@RequestBody SortRequest request) {
    int[] sorted = sortService.mergeSort(request.getArray());
    return ResponseEntity.ok(sorted);
    }

}
