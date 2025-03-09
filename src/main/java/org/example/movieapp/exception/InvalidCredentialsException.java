package org.example.movieapp.exception;

import lombok.Getter;

@Getter
public class InvalidCredentialsException extends RuntimeException {
    private final String field;

    public InvalidCredentialsException(String field, String message) {
        super(message);
        this.field = field;
    }
}
