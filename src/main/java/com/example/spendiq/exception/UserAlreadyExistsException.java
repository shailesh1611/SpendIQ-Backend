package com.example.spendiq.exception;

public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException(String message) {
        super(message);  // Use RuntimeException's built-in message storage
    }
}
