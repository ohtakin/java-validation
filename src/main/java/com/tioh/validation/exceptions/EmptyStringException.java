package com.tioh.validation.exceptions;

public class EmptyStringException extends IllegalArgumentException {

    public EmptyStringException(String s) {
        super(s);
    }
}
