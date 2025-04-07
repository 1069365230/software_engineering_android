package com.example.taskmanager.exception;

public class NoTaskFoundPosition extends IndexOutOfBoundsException{
    public NoTaskFoundPosition(String message) {
        super(message);
    }
}
