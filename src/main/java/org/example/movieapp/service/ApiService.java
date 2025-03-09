package org.example.movieapp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${tmdb.api.key}")
    private String apiKey;

    public Object getPopularMovies(int page) {
        String url =  "https://api.themoviedb.org/3/movie/popular?api_key=" + apiKey + "&page=" + page;
        return restTemplate.getForObject(url, Object.class);
    }

    public Object getPopularMovieDetails(String id) {
        String url =  "https://api.themoviedb.org/3/movie/"+id + "?api_key=" + apiKey;
        return restTemplate.getForObject(url, Object.class);
    }

    public Object getMoviesGenres() {
        String url =  "https://api.themoviedb.org/3/genre/movie/list?api_key=" + apiKey;
        return restTemplate.getForObject(url, Object.class);
    }

    public Object getPopularSeries(int page) {
        String url =  "https://api.themoviedb.org/3/tv/popular?api_key="+ apiKey + "&page=" + page;
        return restTemplate.getForObject(url, Object.class);
    }

    public Object getPopularSeriesDetails(String id) {
        String url =  "https://api.themoviedb.org/3/tv/"+id + "?api_key=" + apiKey;
        return restTemplate.getForObject(url, Object.class);
    }

    public Object getSeriesGenres() {
        String url =  "https://api.themoviedb.org/3/genre/tv/list?api_key=" + apiKey;
        return restTemplate.getForObject(url, Object.class);
    }
}