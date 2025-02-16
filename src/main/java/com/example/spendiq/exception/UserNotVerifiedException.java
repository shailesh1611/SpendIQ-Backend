package com.example.spendiq.exception;

public class UserNotVerifiedException extends RuntimeException{
    public UserNotVerifiedException(String message) {
        super(message);
    }
}
