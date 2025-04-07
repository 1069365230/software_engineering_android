package com.example.taskmanager.exception;

public class UnknownTaskTypeException extends IllegalStateException {
    public UnknownTaskTypeException(String message) {
        super(message);
    }
}
