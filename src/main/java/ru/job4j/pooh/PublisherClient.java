package ru.job4j.pooh;

import java.io.*;
import java.net.Socket;

/**
 * Class PublisherClient
 * Класс реализует отправителя сообщений.
 * @author Dmitry Razumov
 * @version 1
 */
public class PublisherClient implements Runnable {
    /**
     * Сообщение.
     */
    private final String message;

    /**
     * Конструктор задает сообщение для отправителя.
     * @param message Сообщение.
     */
    public PublisherClient(String message) {
        this.message = message;
    }

    /**
     * Метод выполняет отправку сообщения на сервер.
     * Предварительно проверяя готовность сервера к приему сообщения.
     */
    @Override
    public void run() {
        try (Socket clientSocket = new Socket("localhost", 9000);
             DataInputStream  in = new DataInputStream(clientSocket.getInputStream());
             DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream())) {
            out.writeUTF("POST");
            String line = in.readUTF();
            if (line.equals("OK")) {
                out.writeUTF(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
