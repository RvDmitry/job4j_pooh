package ru.job4j.pooh;

import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Class MonoServer
 * Класс реализует моносервер, в котором осуществляет работа обменника с сообщениями.
 * @author Dmitry Razumov
 * @version 1
 */
public class MonoServer implements Runnable {
    /**
     * Коллекция для хранения очередей.
     */
    private static final ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> QUEUES
            = new ConcurrentHashMap<>();
    /**
     * Сокет клиента.
     */
    private final Socket clientSocket;

    /**
     * Конструктор инициализирует сервер.
     * @param clientSocket Сокет клиента.
     */
    public MonoServer(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    /**
     * Метод запускает сервер.
     */
    public void run() {
        try (DataInputStream  in = new DataInputStream(clientSocket.getInputStream());
             DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream())) {
            String line = in.readUTF();
            if (line.equals("POST")) {
                sender(in, out);
            }
            if (line.equals("GET")) {
                recipient(in, out);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод отправляет сообщение в очередь.
     * Предварительно проверяется, имеется ли данная очередь в коллекции.
     * Если очереди нет, то она создается.
     * @param in Входящий поток.
     * @param out Исходящий поток.
     * @throws IOException Исключение.
     */
    private void sender(DataInputStream  in, DataOutputStream out) throws IOException {
        out.writeUTF("OK");
        String line = in.readUTF();
        Gson gson = new Gson();
        Message message = gson.fromJson(line, Message.class);
        ConcurrentLinkedQueue<String> queue = QUEUES.get(message.getQueue());
        while (true) {
            if (queue == null) {
                queue = new ConcurrentLinkedQueue<>();
                queue.offer(message.getText());
                var temp = QUEUES.putIfAbsent(message.getQueue(), queue);
                if (temp == null) {
                    break;
                }
                queue = QUEUES.get(message.getQueue());
            } else {
                queue.offer(message.getText());
                break;
            }
        }
    }

    /**
     * Метод вычитывает сообщение из очереди.
     * Предварительно проверяется, имеется ли данная очередь в коллекции.
     * Если очередь отсутствует либо в ней нет сообщений,
     * то возвращается пустая строка в поле сообщения.
     * @param in Входящий поток.
     * @param out Исходящий поток.
     * @throws IOException Исключение.
     */
    private void recipient(DataInputStream  in, DataOutputStream out) throws IOException {
        out.writeUTF("OK");
        String name = in.readUTF();
        Gson gson = new Gson();
        Queue<String> queue = QUEUES.get(name);
        if (queue == null) {
            out.writeUTF(gson.toJson(new Message(name, "")));
        } else {
            String text = queue.poll();
            out.writeUTF(gson.toJson(new Message(name, text != null ? text : "")));
        }
    }
}
