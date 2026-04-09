package com.algovault.challenge;

public class ChallengeResponse {

    private Long id;
    private String title;
    private String difficulty;
    private String category;

    public ChallengeResponse(Long id, String title, String difficulty, String category) {
        this.id = id;
        this.title = title;
        this.difficulty = difficulty;
        this.category = category;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDifficulty() { return difficulty; }
    public String getCategory() { return category; }
}