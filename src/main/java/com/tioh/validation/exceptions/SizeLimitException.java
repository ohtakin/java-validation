package com.tioh.validation.exceptions;

public class SizeLimitException extends IllegalArgumentException {

    public SizeLimitException(String message) {
        super(message);
    }
}
