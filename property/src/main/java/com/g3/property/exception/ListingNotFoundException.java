package com.g3.property.exception;

public class ListingNotFoundException extends RuntimeException {
    public ListingNotFoundException(String message) {
        super(message);
    }
}
