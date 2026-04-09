package com.algovault.challenge;

import com.algovault.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChallengeServiceTest {

    @Mock
    private ChallengeRepository repository;

    @InjectMocks
    private ChallengeService service;

    @Test
    void findById_retourneLeChallengeExistant() {
        Challenge challenge = new Challenge(1L, "Two Sum", "EASY", "Arrays");
        when(repository.findById(1L)).thenReturn(Optional.of(challenge));

        ChallengeResponse result = service.findById(1L);

        assertEquals("Two Sum", result.getTitle());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void findById_lanceExceptionSiInexistant() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findById(99L));
    }

    @Test
    void create_sauvegardeLeChallengeEtLeRetourne() {
        ChallengeRequest request = new ChallengeRequest();
        request.setTitle("Two Sum");
        request.setDifficulty("EASY");
        request.setCategory("Arrays");

        Challenge saved = new Challenge(1L, "Two Sum", "EASY", "Arrays");
        when(repository.save(any(Challenge.class))).thenReturn(saved);

        ChallengeResponse result = service.create(request);

        assertEquals(1L, result.getId());
        verify(repository, times(1)).save(any(Challenge.class));
    }

    @Test
    void deleteById_appelleLeRepository() {
        service.deleteById(1L);

        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void update_modifieLeChallengeEtLeRetourne() {
        ChallengeRequest request = new ChallengeRequest();
        request.setTitle("Binary Search");
        request.setDifficulty("MEDIUM");
        request.setCategory("Arrays");

        Challenge saved = new Challenge(1L, "Binary Search", "MEDIUM", "Arrays");
        when(repository.save(any(Challenge.class))).thenReturn(saved);

        ChallengeResponse result = service.update(1L, request);

        assertEquals("Binary Search", result.getTitle());
        assertEquals(1L, result.getId());
        verify(repository, times(1)).save(any(Challenge.class));
    }
}
