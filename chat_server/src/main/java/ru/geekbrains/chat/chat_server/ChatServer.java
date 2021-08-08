package ru.geekbrains.chat.chat_server;

import ru.geekbrains.chat.chat_server.auth.AuthService;
import ru.geekbrains.chat.chat_server.auth.InMemoryAuthService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
    private static final int PORT = 8089;
    private AuthService authService;
    private List<ChatClientHandler> handlers;

    public ChatServer() {
        this.authService = new InMemoryAuthService();
        this.handlers = new ArrayList<>();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server start!");
            while (true) {
                System.out.println("Waiting for connection......");
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");
                new ChatClientHandler(socket, this).handle();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcastMessage(String message) {
        for (ChatClientHandler handler : handlers) {
            handler.sendMessage(message);
        }
    }

    public synchronized void removeAuthorizedClientFromList(ChatClientHandler handler) {
        this.handlers.remove(handler);
        sendClientsOnline();
    }

    public synchronized void addAuthorizedClientToList(ChatClientHandler handler) {
        this.handlers.add(handler);
        sendClientsOnline();
    }
    public AuthService getAuthService() {
        return authService;
    }

    public void sendClientsOnline() {
        StringBuilder sb = new StringBuilder("$.list: ");
        for (ChatClientHandler handler : handlers) {
            sb.append(handler.getCurrentUser()).append(" ");
        }
        broadcastMessage(sb.toString());
    }
}
