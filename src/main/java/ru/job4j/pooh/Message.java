package ru.job4j.pooh;

/**
 * Class Message
 * Класс описывает сообщение.
 * @author Dmitry Razumov
 * @version 1
 */
public class Message {
    /**
     * Имя очереди в которое будет отправлено сообщение.
     */
    private final String queue;
    /**
     * Текст сообщения.
     */
    private final String text;

    /**
     * Конструктор создает сообщение.
     * @param queue Имя очереди.
     * @param text Текст сообщения.
     */
    public Message(String queue, String text) {
        this.queue = queue;
        this.text = text;
    }

    /**
     * Метод возвращает имя очереди.
     * @return Имя очереди.
     */
    public String getQueue() {
        return queue;
    }

    /**
     * Метод возвращает текст сообщения.
     * @return Текст сообщения.
     */
    public String getText() {
        return text;
    }
}
