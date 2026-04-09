package com.algovault.challenge;

import com.algovault.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChallengeService {

    private final ChallengeRepository repository;

    @Autowired
    @Lazy
    private ChallengeService self;

    public ChallengeService(ChallengeRepository repository) {
        this.repository = repository;
    }

    // Conversion entité → DTO
    private ChallengeResponse toResponse(Challenge c) {
        return new ChallengeResponse(c.getId(), c.getTitle(), c.getDifficulty(), c.getCategory());
    }

    // Conversion DTO → entité
    private Challenge toEntity(ChallengeRequest req) {
        return new Challenge(req.getTitle(), req.getDifficulty(), req.getCategory());
    }

    @Cacheable("challenges")
    public List<Challenge> findAllCached() {
        return repository.findAll();
    }

    public Page<ChallengeResponse> findAll(int page, int size) {
        List<Challenge> all = self.findAllCached();
        Pageable pageable = PageRequest.of(page, size);
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), all.size());
        List<ChallengeResponse> responses = all.subList(start, end)
                .stream().map(this::toResponse).toList();
        return new PageImpl<>(responses, pageable, all.size());
    }

    @Cacheable(value = "challenge", key = "#id")
    public ChallengeResponse findById(Long id) {
        Challenge challenge = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Challenge with id " + id + " not found"));
        return toResponse(challenge);
    }

    @CacheEvict(value = {"challenges", "challenge"}, allEntries = true)
    public ChallengeResponse create(ChallengeRequest request) {
        Challenge saved = repository.save(toEntity(request));
        return toResponse(saved);
    }

    @CacheEvict(value = {"challenges", "challenge"}, allEntries = true)
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @CacheEvict(value = {"challenges", "challenge"}, allEntries = true)
    public ChallengeResponse update(Long id, ChallengeRequest request) {
        Challenge challenge = toEntity(request);
        challenge.setId(id);
        return toResponse(repository.save(challenge));
    }
}