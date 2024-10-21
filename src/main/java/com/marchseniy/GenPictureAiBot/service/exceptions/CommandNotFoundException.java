package com.marchseniy.GenPictureAiBot.service.exceptions;

public class CommandNotFoundException extends Exception {
    public CommandNotFoundException(String message) {
        super(message);
    }

    public CommandNotFoundException() { }
}
