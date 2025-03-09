package org.example.movieapp.model;

import lombok.Data;

@Data
public class FavoritePostDTO {
    private Long itemId;
    private String poster;
    private String title;
}

