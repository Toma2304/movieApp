package org.example.movieapp.controller;

import org.example.movieapp.model.Favorite;
import org.example.movieapp.model.PopularMoviesDTO;
import org.example.movieapp.model.User;
import org.example.movieapp.service.ApiService;
import org.example.movieapp.service.FavoritesService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")

public class ApiController {

    private final ApiService apiService;
    private final FavoritesService favoritesService;

    public ApiController(ApiService apiService, FavoritesService favoritesService) {
        this.apiService = apiService;
        this.favoritesService = favoritesService;
    }

    @GetMapping("/popular-movies")
    public ResponseEntity<PopularMoviesDTO> getPopularMovies(@RequestParam(defaultValue = "1") int page) {
        Object popularMovies = apiService.getPopularMovies(page);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return ResponseEntity.ok(new PopularMoviesDTO(popularMovies, null));
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetails userDetails)) {
            return ResponseEntity.ok(new PopularMoviesDTO(popularMovies, null));
        }

        User user = favoritesService.getUserByEmail(userDetails.getUsername());
        if (user == null) {
            return ResponseEntity.ok(new PopularMoviesDTO(popularMovies, null));
        }

        List<Favorite> userMovieFavorites = favoritesService.getFavoritesByUser();

        PopularMoviesDTO response = new PopularMoviesDTO(popularMovies, userMovieFavorites);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/popular-movies/{id}")
    public Object getPopularMovieDetails(@PathVariable("id") String id) {
        return apiService.getPopularMovieDetails(id);
    }

    @GetMapping("/movies/genres")
    public Object getMoviesGenres() {
        return apiService.getMoviesGenres();
    }

    @GetMapping("/popular-series")
    public Object getPopularSeries(@RequestParam(defaultValue = "1") int page) {
        return apiService.getPopularSeries(page);
    }

    @GetMapping("/popular-series/{id}")
    public Object getPopularSeriesDetails(@PathVariable("id") String id) {
        return apiService.getPopularSeriesDetails(id);
    }

    @GetMapping("/series/genres")
    public Object getSeriesGenres() {
        return apiService.getSeriesGenres();
    }
}