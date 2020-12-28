package ru.job4j.pooh;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Class CunsumerClient
 * Класс реализует получателя сообщений.
 * @author Dmitry Razumov
 * @version 1
 */
public class CunsumerClient implements Runnable {
    /**
     * Имя очереди.
     */
    private final String queue;
    /**
     * Сообщение, полученное из обменника.
     */
    private String message;

    /**
     * Конструктор задает имя очереди из которой нужно получить сообщение.
     * @param queue Имя очереди.
     */
    public CunsumerClient(String queue) {
        this.queue = queue;
    }

    /**
     * Метод возвращает сообщение.
     * @return Сообщение.
     */
    public String getMessage() {
        synchronized (this) {
            return message;
        }
    }

    /**
     * Метод осуществляет получение сообщения от сервера.
     */
    @Override
    public void run() {
        try (Socket clientSocket = new Socket("localhost", 9000);
             DataInputStream in = new DataInputStream(clientSocket.getInputStream());
             DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream())) {
            out.writeUTF("GET");
            String line = in.readUTF();
            if (line.equals("OK")) {
                out.writeUTF(queue);
                synchronized (this) {
                    message = in.readUTF();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
