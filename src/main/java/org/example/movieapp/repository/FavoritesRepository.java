package org.example.movieapp.repository;
import org.example.movieapp.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoritesRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUserId(Long userId);
}