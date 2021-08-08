package ru.geekbrains.chat.chat_server.error;

public class BadRequestException extends RuntimeException{
    public BadRequestException(String message) {
        super(message);
    }
}
