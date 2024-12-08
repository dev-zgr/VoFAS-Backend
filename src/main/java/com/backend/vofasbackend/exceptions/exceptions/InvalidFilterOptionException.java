package com.backend.vofasbackend.exceptions.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)

public class InvalidFilterOptionException extends RuntimeException{
    public InvalidFilterOptionException(String filterName, String value) {
        super(String.format("Filter option, '%s':'%s' is not in valid format.", filterName, value));
    }
}
