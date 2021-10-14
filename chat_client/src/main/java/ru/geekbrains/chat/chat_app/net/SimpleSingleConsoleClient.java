package ru.geekbrains.chat.chat_app.net;

import java.io.*;
import java.net.Socket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SimpleSingleConsoleClient {
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 8089;
    private DataOutputStream out;
    private DataInputStream in;
    private Thread clientConsoleThread;
    private static final Logger log = LogManager.getLogger(SimpleSingleConsoleClient.class);

    public static void main(String[] args) {
        new SimpleSingleConsoleClient().start();
    }

    private void start() {
        try (Socket socket = new Socket(HOST, PORT)) {
            System.out.println("Client started!");
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            startClientConsoleThread();

            while (true) {
                String message = in.readUTF();
                if (message.startsWith("/end")) {
                    shutdown();
                    break;
                }
                log.debug("Received: " + message);
            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                shutdown();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void shutdown() throws IOException {
        if (clientConsoleThread.isAlive()) clientConsoleThread.interrupt();
        log.debug("Client stopped");
    }

    private void startClientConsoleThread() {
        clientConsoleThread = new Thread(() -> {
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))) {
                System.out.print("Enter message for server >>>> \n");

                while (!Thread.currentThread().isInterrupted()) {
                    if (bufferedReader.ready()) {
                        String serverMessage = bufferedReader.readLine();
                        out.writeUTF(serverMessage);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        clientConsoleThread.start();
    }
}
