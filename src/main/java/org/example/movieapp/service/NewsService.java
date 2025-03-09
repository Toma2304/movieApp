package org.example.movieapp.service;

import org.example.movieapp.model.NewsArticle;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class NewsService {

    public List<NewsArticle> scrapeMovieNews(int page) {
        List<NewsArticle> articles = new ArrayList<>();
        final int pageSize = 10;

        try {
            Document doc = Jsoup.connect("https://www.nytimes.com/section/movies?page=" + page).get();

            Elements items = doc.select(".css-14ee9cx");

            int startIndex = (page - 1) * pageSize;
            int endIndex   = page * pageSize;

            if (startIndex >= items.size()) {
                return articles;
            }
            if (endIndex > items.size()) {
                endIndex = items.size();
            }

            Elements pageItems = new Elements(items.subList(startIndex, endIndex));

            for (Element item : pageItems) {
                String headline = item.select("h3").text();
                String description = item.select(".css-1pga48a").text();
                String link = item.select(".css-8hzhxf").attr("href");
                String image = item.select("img").attr("src");
                String source = item.select(".css-1n7hynb").text();

                NewsArticle article = new NewsArticle(headline, description, source, link, image);
                articles.add(article);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return articles;
    }
}
