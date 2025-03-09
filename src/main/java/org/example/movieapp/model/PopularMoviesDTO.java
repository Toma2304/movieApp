package org.example.movieapp.model;

import lombok.Data;

import java.util.List;

@Data
public class PopularMoviesDTO {

    private Object popularMovies;
    private List<Favorite> userFavorites;

    public PopularMoviesDTO(Object popularMovies, List<Favorite> userFavorites) {
        this.popularMovies = popularMovies;
        this.userFavorites = userFavorites;
    }
}