[![Build Status](https://travis-ci.com/RvDmitry/job4j_pooh.svg?branch=master)](https://travis-ci.com/RvDmitry/job4j_pooh)
[![codecov](https://codecov.io/gh/RvDmitry/job4j_pooh/branch/master/graph/badge.svg?token=DK4JCJ3JIC)](https://codecov.io/gh/RvDmitry/job4j_pooh)
# Pooh JMS

**О проекте:**

Проект представляет упрощенный аналог RabbitMQ.
- Реализованы очериди типа Queue
- Реализован мультипоточный сервер
- Реализованы два типа клиентов: получатель и отправитель

Отправитель подключает к серверу и отправляет сообщение в формате JSON с указанием очереди.
Получатель также подключившись к серверу пытается получить сообщение из определенной очереди.
Если сообщение в заданной очереди имеется, то сервер отдает сообщение и получив подтверждение 
о получении сообщения, удаляет его из очереди. Взаимодействие между сервером и клиентами 
осуществляется с использованием Socket.

**Используемые технологии:**

- Java 14 (Core, Sockets, Concurrent, ThreadPool)
- JUnit
- Apache Maven