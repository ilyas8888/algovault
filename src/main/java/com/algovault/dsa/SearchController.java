package com.algovault.dsa;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dsa/search")
public class SearchController {
    private final SearchService searchService;
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }
    @PostMapping("/linear")
    public int linearSearch(@RequestBody SearchRequest request){
        return searchService.linearSearch(request.getArray(), request.getTarget());
    }

    @PostMapping("/binary")
    public int binarySearch(@RequestBody SearchRequest request){
        return searchService.binarySearch(request.getArray(), request.getTarget());
    }
}
