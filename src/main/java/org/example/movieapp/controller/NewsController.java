package org.example.movieapp.controller;

import org.example.movieapp.model.NewsArticle;
import org.example.movieapp.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/news")
public class NewsController {

    @Autowired
    private NewsService newsService;


    @GetMapping
    public List<NewsArticle> getMovieNews(@RequestParam(defaultValue = "1") int page) {
        return newsService.scrapeMovieNews(page);
    }
}
