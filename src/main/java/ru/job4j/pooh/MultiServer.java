package ru.job4j.pooh;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.concurrent.*;

/**
 * Class MultiServer
 * Класс реализует мультисервер для работы обменника сообщений.
 * @author Dmitry Razumov
 * @version 1
 */
public class MultiServer implements Runnable {
    /**
     * Параметр определяет продолжать ли работать серверу.
     */
    private boolean isStopped = false;
    /**
     * Сокет сервера.
     */
    private ServerSocket serverSocket = null;
    /**
     * Пул потоков, в которых запускаются отдельно моносервера.
     */
    private final ExecutorService threadPool =
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    /**
     * Метод запускает работу мультисервера.
     */
    public void run() {
        openServerSocket();
        while (!isStopped) {
            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                if (isStopped) {
                    break;
                }
                e.printStackTrace();
            }
            threadPool.execute(new MonoServer(clientSocket));
        }
        this.threadPool.shutdown();
        System.out.println("Server Stopped.");
    }

    /**
     * Метод останавливает работу мультисервера.
     */
    public void stop() {
        isStopped = true;
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    /**
     * Метод создает сокет сервера.
     */
    private void openServerSocket() {
        try {
            serverSocket = new ServerSocket(9000);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port", e);
        }
    }
}
