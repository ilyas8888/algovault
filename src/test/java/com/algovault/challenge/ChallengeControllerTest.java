package com.algovault.challenge;

import com.algovault.security.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChallengeController.class)
class ChallengeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChallengeService service;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void getById_retourne200AvecLeChallenge() throws Exception {
        ChallengeResponse response = new ChallengeResponse(1L, "Two Sum", "EASY", "Arrays");
        when(service.findById(1L)).thenReturn(response);

        mockMvc.perform(get("/challenges/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Two Sum"))
                .andExpect(jsonPath("$.difficulty").value("EASY"));
    }

    @Test
    @WithMockUser
    void getById_retourne404SiInexistant() throws Exception {
        when(service.findById(99L)).thenThrow(new com.algovault.exception.ResourceNotFoundException("Challenge with id 99 not found"));

        mockMvc.perform(get("/challenges/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void create_retourne201AvecLeChallengeCree() throws Exception {
        ChallengeRequest request = new ChallengeRequest();
        request.setTitle("Two Sum");
        request.setDifficulty("EASY");
        request.setCategory("Arrays");

        ChallengeResponse response = new ChallengeResponse(1L, "Two Sum", "EASY", "Arrays");
        when(service.create(any(ChallengeRequest.class))).thenReturn(response);

        mockMvc.perform(post("/challenges")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser
    void delete_retourne200() throws Exception {
        ChallengeResponse response = new ChallengeResponse(1L, "Two Sum", "EASY", "Arrays");
        when(service.findById(1L)).thenReturn(response);

        mockMvc.perform(delete("/challenges/1")
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(service, times(1)).deleteById(1L);
    }
}
