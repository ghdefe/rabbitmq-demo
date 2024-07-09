package com.example.producer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitmqProducer {

    @Autowired
    RabbitTemplate rabbitTemplate;

    public void singleSend() {
        for (int i = 0; i < 5; i++) {
            rabbitTemplate.convertAndSend("single-queue", "来自简单模式的消息: " + i);
        }
    }

    public void workSend() {
        for (int i = 0; i < 5; i++) {
            rabbitTemplate.convertAndSend("work-queue", "来自工作模式的消息: " + i);
        }
    }

    public void fanoutSend() {
        for (int i = 0; i < 5; i++) {
            rabbitTemplate.convertAndSend("fanout-exchange", "", "来自广播模式的消息: " + i);
        }
    }

    public void routeSend() {
        for (int i = 0; i < 5; i++) {
            rabbitTemplate.convertAndSend("routing-exchange", "key-aaa", "来自路由模式的消息: key-aaa" + i);
            rabbitTemplate.convertAndSend("routing-exchange", "key-bbb", "来自路由模式的消息: key-bbb" + i);
            rabbitTemplate.convertAndSend("routing-exchange", "key-ccc", "来自路由模式的消息: key-ccc" + i);
        }
    }

    public void topicSend() {
        rabbitTemplate.convertAndSend("topic-exchange", "aaa.111", "来自主题模式的消息: key: aaa.111");
        rabbitTemplate.convertAndSend("topic-exchange", "aaa.222", "来自主题模式的消息: key: aaa.222");
        rabbitTemplate.convertAndSend("topic-exchange", "aaa.xxx.111", "来自主题模式的消息: key: aaa.xxx.111");
    }

    private static final Logger log = LoggerFactory.getLogger(RabbitmqProducer.class);

    /**
     * RPC模式
     */
    public void rpcSend() {
        Object receive = rabbitTemplate.convertSendAndReceive("rpc-queue", "来自RPC模式的消息");
        log.info("生产者收到回调消息: {}", receive);
    }
}
