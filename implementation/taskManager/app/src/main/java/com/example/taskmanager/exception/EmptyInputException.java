package com.example.taskmanager.exception;

public class EmptyInputException extends IllegalArgumentException{
    public EmptyInputException(String s) {
        super(s);
    }
}
