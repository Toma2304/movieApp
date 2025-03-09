package org.example.movieapp.model;

import lombok.Data;

@Data
public class NewsArticle {
    private String headline;
    private String description;
    private String source;

    private String link;
    private String image;

    public NewsArticle(String headline, String description, String source, String link, String image) {
        this.headline = headline;
        this.description = description;
        this.source = source;
        this.link = link;
        this.image = image;
    }
}
