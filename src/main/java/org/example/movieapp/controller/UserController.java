package org.example.movieapp.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.movieapp.model.User;
import org.example.movieapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody User request) {
        String message = userService.register(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(message);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody User request, HttpSession session) {
        String message = userService.login(request.getEmail(), request.getPassword(), session);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response, HttpSession session) {
        String message = userService.logout(response, session);
        return ResponseEntity.ok(message);
    }
}
