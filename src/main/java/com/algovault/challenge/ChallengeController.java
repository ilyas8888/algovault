package com.algovault.challenge;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/challenges")
public class ChallengeController {

    private final ChallengeService service;

    public ChallengeController(ChallengeService service) {
        this.service = service;
    }

    @GetMapping
    public Page<ChallengeResponse> getAll(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        return service.findAll(page, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChallengeResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<ChallengeResponse> create(@RequestBody @Valid ChallengeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ChallengeResponse> delete(@PathVariable Long id) {
        ChallengeResponse challenge = service.findById(id);
        service.deleteById(id);
        return ResponseEntity.ok(challenge);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChallengeResponse> update(@PathVariable Long id,
                                                    @RequestBody @Valid ChallengeRequest request) {
        service.findById(id);
        return ResponseEntity.ok(service.update(id, request));
    }
}