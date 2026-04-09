package com.algovault.challenge;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ChallengeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void getAll_retourneLesChallengesInitiaux() throws Exception {
        mockMvc.perform(get("/challenges"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    @WithMockUser
    void creerPuisRecupererUnChallenge() throws Exception {
        Challenge challenge = new Challenge("Quick Sort", "MEDIUM", "Arrays");

        // Créer
        String response = mockMvc.perform(post("/challenges")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(challenge)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Quick Sort"))
                .andReturn().getResponse().getContentAsString();

        // Récupérer l'id créé
        Long id = objectMapper.readTree(response).get("id").asLong();

        // Vérifier qu'il existe
        mockMvc.perform(get("/challenges/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Quick Sort"));
    }

    @Test
    @WithMockUser
    void supprimerUnChallenge_puisVerifier404() throws Exception {
        // Créer d'abord
        Challenge challenge = new Challenge("To Delete", "EASY", "Strings");
        String response = mockMvc.perform(post("/challenges")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(challenge)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        // Supprimer
        mockMvc.perform(delete("/challenges/" + id).with(csrf()))
                .andExpect(status().isOk());

        // Vérifier 404
        mockMvc.perform(get("/challenges/" + id))
                .andExpect(status().isNotFound());
    }
}