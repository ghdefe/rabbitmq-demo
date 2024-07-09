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

    @Test
    void fanoutSend() {
        rabbitmqProducer.fanoutSend();
    }

    @Test
    void routeSend() {
        rabbitmqProducer.routeSend();;
    }

    @Test
    void topicSend() {
        rabbitmqProducer.topicSend();
    }

    @Test
    void rpcSend() {
        rabbitmqProducer.rpcSend();
    }
}