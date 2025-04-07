package com.example.taskmanager.exception;

public class InvalidDateException extends IllegalArgumentException{
    public InvalidDateException(String message) {
        super(message);
    }
}
