package com.example.producer.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RabbitmqProducerTest {

    @Autowired
    RabbitmqProducer rabbitmqProducer;
    @Test
    void singleSend() {
        rabbitmqProducer.singleSend();
    }


    @Test
    void workSend() {
        rabbitmqProducer.workSend();
    }
}