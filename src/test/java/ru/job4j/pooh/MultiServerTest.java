package ru.job4j.pooh;

import com.google.gson.Gson;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Class MultiServerTest
 * Класс тестирует работу мультисервера.
 * @author Dmitry Razumov
 * @version 1
 */
public class MultiServerTest {

    @Test
    public void whenSendAndGetMessage() throws InterruptedException {
        MultiServer server = new MultiServer();
        new Thread(server).start();
        Message message = new Message("weather", "temperature +18 C");
        Gson gson = new Gson();
        String json = gson.toJson(message);
        PublisherClient publisher = new PublisherClient(json);
        Thread pub = new Thread(publisher);
        pub.start();
        Thread.sleep(1000);
        CunsumerClient cunsumer = new CunsumerClient("weather");
        Thread cun = new Thread(cunsumer);
        cun.start();
        cun.join();
        String jn = cunsumer.getMessage();
        Message msg = gson.fromJson(jn, Message.class);
        server.stop();
        assertThat(msg.getQueue(), is("weather"));
        assertThat(msg.getText(), is("temperature +18 C"));
    }

    @Test
    public void whenNoMessage() throws InterruptedException {
        MultiServer server = new MultiServer();
        new Thread(server).start();
        Gson gson = new Gson();
        CunsumerClient cunsumer = new CunsumerClient("weather");
        Thread cun = new Thread(cunsumer);
        cun.start();
        cun.join();
        String jn = cunsumer.getMessage();
        Message msg = gson.fromJson(jn, Message.class);
        server.stop();
        assertThat(msg.getQueue(), is("weather"));
        assertThat(msg.getText(), is(""));
    }

    @Test
    public void whenMultipleCunsumers() throws InterruptedException {
        MultiServer server = new MultiServer();
        new Thread(server).start();
        Message message = new Message("weather", "temperature +18 C");
        Gson gson = new Gson();
        String json = gson.toJson(message);
        PublisherClient publisher = new PublisherClient(json);
        Thread pub = new Thread(publisher);
        pub.start();
        Thread.sleep(1000);
        CunsumerClient first = new CunsumerClient("weather");
        Thread f = new Thread(first);
        f.start();
        f.join();
        String jn = first.getMessage();
        Message msgFirst = gson.fromJson(jn, Message.class);
        CunsumerClient second = new CunsumerClient("weather");
        Thread s = new Thread(second);
        s.start();
        s.join();
        jn = second.getMessage();
        Message msgSecond = gson.fromJson(jn, Message.class);
        server.stop();
        assertThat(msgFirst.getText(), is("temperature +18 C"));
        assertThat(msgSecond.getText(), is(""));
    }
}