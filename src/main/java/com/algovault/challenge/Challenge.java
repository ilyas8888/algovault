package com.algovault.challenge;

import jakarta.persistence.*;


@Entity
@Table(name = "challenges")
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String difficulty;  // "EASY", "MEDIUM", "HARD"

    private String category;    // "Arrays", "Strings", "Graphs"...

    public Challenge(Long id, String title, String difficulty, String category) {
        this.id = id;
        this.title = title;
        this.difficulty = difficulty;
        this.category = category;
    }
    public Challenge(String title,  String difficulty, String category) {
        this .title = title;
        this.difficulty = difficulty;
        this.category = category;
    }

    public Challenge() {}
    public Long getId()           { return id; }
    public void setId(Long id){
        this.id = id;
    }
    public String getTitle()      { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    public String getCategory()   { return category; }
    public void setCategory(String category) { this.category = category; }

}
