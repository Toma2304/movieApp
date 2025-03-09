package org.example.movieapp.controller;

import jakarta.validation.Valid;
import org.example.movieapp.model.Favorite;
import org.example.movieapp.model.FavoritePostDTO;
import org.example.movieapp.model.FavoritePutDTO;
import org.example.movieapp.service.FavoritesService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorites")

public class FavoritesController {
    private final FavoritesService favoritesService;

    public FavoritesController(FavoritesService favoritesService) {
        this.favoritesService = favoritesService;
    }

    @PostMapping
    public Favorite addFavorite(@RequestBody FavoritePostDTO dto) {
        return favoritesService.addFavorite(dto.getItemId(), dto.getTitle(), dto.getPoster());
    }

    @PutMapping("/{favoriteId}")
    public Favorite updateFavorite(@PathVariable Long favoriteId, @Valid @RequestBody FavoritePutDTO dto) {
        return favoritesService.updateFavorite(favoriteId, dto.getRating(), dto.getImpression());
    }

    @GetMapping
    public List<Favorite> getFavorites() {
        return favoritesService.getFavoritesByUser();
    }

    @DeleteMapping("/{favoriteId}")
    public void removeFavorite(@PathVariable Long favoriteId) {
        favoritesService.removeFavorite(favoriteId);
    }
}