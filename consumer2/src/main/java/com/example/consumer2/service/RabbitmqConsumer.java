package com.example.consumer2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitmqConsumer {

    private static final Logger log = LoggerFactory.getLogger(RabbitmqConsumer.class);

    @Value("${spring.application.name}")
    private String appName;


    /**
     * 发布/订阅模式
     */
    @RabbitListener(queues = "fanout-queue2")
    public void consumerFanoutMessage1(String content) {
        log.info("发布/订阅模式, 消费者-{}, 收到消息:{}", appName, content);
    }

}
