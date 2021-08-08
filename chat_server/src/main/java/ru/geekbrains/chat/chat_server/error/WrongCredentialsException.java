package ru.geekbrains.chat.chat_server.error;

public class WrongCredentialsException extends RuntimeException{
    public WrongCredentialsException(String message) {
        super(message);
    }
}
