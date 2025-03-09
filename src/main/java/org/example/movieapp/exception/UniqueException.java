package org.example.movieapp.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UniqueException extends RuntimeException {
    private final String field;

    public UniqueException(String field, String message) {
        super(message);
        this.field = field;
    }
}
