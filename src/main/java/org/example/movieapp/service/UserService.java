package org.example.movieapp.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.example.movieapp.exception.InvalidCredentialsException;
import org.example.movieapp.exception.UniqueException;
import org.example.movieapp.model.User;
import org.example.movieapp.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public String register(String email, String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UniqueException("email", "Email is already in use");
        }

        String hash = BCrypt.hashpw(password, BCrypt.gensalt());

        User user = new User();
        user.setEmail(email);
        user.setPassword(hash);
        userRepository.save(user);

        return "User registered successfully!";
    }

    public String login(String email, String password, HttpSession session) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new InvalidCredentialsException("email", "Invalid credentials.");
        }
        User user = optionalUser.get();

        if (!BCrypt.checkpw(password, user.getPassword())) {
            throw new InvalidCredentialsException("password", "Invalid credentials.");
        }

        session.setAttribute("userId", user.getId());
        return "Logged in successfully!";
    }

    public String logout(HttpServletResponse response, HttpSession session) {
        session.invalidate();

        Cookie cookie = new Cookie("JSESSIONID", "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return "Logged out successfully!";
    }
}
