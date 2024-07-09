package com.example.consumer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class RabbitmqConsumer {

    private static final Logger log = LoggerFactory.getLogger(RabbitmqConsumer.class);

    @Value("${spring.application.name}")
    private String appName;

    /**
     * 简单模式
     */
    @RabbitListener(queuesToDeclare = @Queue(name = "single-queue"))
    public void consumerSimpleMessage(String content) {
        log.info("简单模式, 收到消息:{}", content);
    }

    /**
     * 工作模式
     */
    @RabbitListener(queuesToDeclare = @Queue(name = "work-queue"))
    public void consumerWorkMessage1(String content) {
        log.info("工作模式, 消费者-{}, 收到消息:{}", appName, content);
    }


    /**
     * 发布/订阅模式
     */
    @RabbitListener(queues = "fanout-queue1")
    public void consumerFanoutMessage1(String content) {
        log.info("发布/订阅模式, 消费者-{}, 收到消息:{}", appName, content);
    }

    /**
     * 路由模式
     */
    @RabbitListener(queues = "routing-queue1")
    public void consumerRoutingMessage1(String content) {
        log.info("路由模式, 队列: {}, 收到消息:{}", "routing-queue1", content);
    }

    @RabbitListener(queues = "routing-queue2")
    public void consumerRoutingMessage2(String content) {
        log.info("路由模式, 队列: {}, 收到消息:{}", "routing-queue2", content);
    }

    /**
     * 主题模式
     */

    /**
     * RPC模式
     */
}
