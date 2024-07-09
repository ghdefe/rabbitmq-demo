package com.example.producer.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitmqProducer {

    @Autowired
    RabbitTemplate rabbitTemplate;

    public void singleSend() {
        for (int i = 0; i < 10; i++) {
            rabbitTemplate.convertAndSend("single-queue", "来自简单模式的消息: " + i);
        }
    }

    public void workSend() {
        for (int i = 0; i < 10; i++) {
            rabbitTemplate.convertAndSend("work-queue", "来自工作模式的消息: " + i);
        }
    }
}
