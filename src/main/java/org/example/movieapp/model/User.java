package org.example.movieapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "credentials")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Must be a valid email address")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Password hash cannot be blank")
    @Size(min = 8, message = "Password must have at least 8 characters")
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Favorite> favorites = new ArrayList<>();

    public void addFavorite(Favorite fav) {
        favorites.add(fav);
        fav.setUser(this);
    }
}
