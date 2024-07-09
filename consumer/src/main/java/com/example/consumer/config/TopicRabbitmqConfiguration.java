package com.example.consumer.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 主题模式
 */
@Configuration
public class TopicRabbitmqConfiguration {


    @Bean
    public Exchange topicExchange() {
        return ExchangeBuilder.topicExchange("topic-exchange")
                .durable(true)
                .build();
    }


    @Bean
    public Queue topicQueue1() {
        return QueueBuilder.durable("topic-queue1")
                .build();
    }

    /**
     * 队列1匹配所有键
     */
    @Bean
    public Binding topicBinding1(@Qualifier("topicQueue1") Queue topicQueue, @Qualifier("topicExchange") Exchange topicExchange) {
        return BindingBuilder.bind(topicQueue).to(topicExchange).with("#").noargs();
    }


    @Bean
    public Queue topicQueue2() {
        return QueueBuilder.durable("topic-queue2")
                .build();
    }


    /**
     * 队列2匹配 <b>aaa.*</b> 开头的
     * 命中：[ 'aaa.111', 'aaa.222' ]  没命中：[ 'aaa.xxx.111' ]
     */
    @Bean
    public Binding topicBinding2(@Qualifier("topicQueue2") Queue topicQueue, @Qualifier("topicExchange") Exchange topicExchange) {
        return BindingBuilder.bind(topicQueue).to(topicExchange).with("aaa.*").noargs();
    }

    @Bean
    public Queue topicQueue3(){
        return QueueBuilder.durable("topic-queue3")
                .build();
    }


    /**
     * 队列3匹配 <b>aaa.#</b> 开头的
     * 命中: [ 'aaa.111', 'aaa.222', aaa.xxx.111 ]  没命中: [  ]
     */
    @Bean
    public Binding topicBinding3(@Qualifier("topicQueue3") Queue topicQueue, @Qualifier("topicExchange") Exchange topicExchange) {
        return BindingBuilder.bind(topicQueue).to(topicExchange).with("aaa.#").noargs();
    }

}
