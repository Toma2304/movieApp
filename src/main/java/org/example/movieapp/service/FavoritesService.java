package org.example.movieapp.service;

import org.example.movieapp.model.Favorite;
import org.example.movieapp.model.User;
import org.example.movieapp.repository.FavoritesRepository;
import org.example.movieapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class FavoritesService {

    @Autowired
    private FavoritesRepository favoritesRepository;

    @Autowired
    private UserRepository userRepository;

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            System.err.println("Authentication is missing or not authenticated: " + auth);
            throw new RuntimeException("No authenticated user found.");
        }

        Object principal = auth.getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            String email = userDetails.getUsername();
            return userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found by email: " + email));
        } else {
            throw new RuntimeException("Invalid principal: " + principal.getClass());
        }
    }

    public Favorite addFavorite(Long itemId, String title, String poster) {
        User user = getAuthenticatedUser();

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setItem_id(itemId);
        favorite.setTitle(title);
        favorite.setPoster(poster);
        favorite.setWatched(false);

        return favoritesRepository.save(favorite);
    }

    public List<Favorite> getFavoritesByUser() {
        User user = getAuthenticatedUser();

        return favoritesRepository.findByUserId(user.getId());
    }

    public Favorite updateFavorite(Long favoriteId, Integer rating, String impression) {
        User user = getAuthenticatedUser();

        Favorite favorite = favoritesRepository.findById(favoriteId)
                .orElseThrow(() -> new RuntimeException("Favorite not found"));

        if (!favorite.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You do not own this favorite");
        }

        favorite.setRating(rating);
        favorite.setImpression(impression);
        favorite.setWatched(true);
        favorite.setFinish_date(new Date());

        return favoritesRepository.save(favorite);
    }


    public void removeFavorite(Long favoriteId) {
        User user = getAuthenticatedUser();

        Favorite favorite = favoritesRepository.findById(favoriteId)
                .orElseThrow(() -> new RuntimeException("Favorite not found"));

        if (!favorite.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You do not own this favorite");
        }

        favoritesRepository.delete(favorite);
    }
}
