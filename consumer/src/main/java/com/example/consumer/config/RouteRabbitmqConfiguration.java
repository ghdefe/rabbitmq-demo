package com.example.consumer.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 路由模式
 */
@Configuration
public class RouteRabbitmqConfiguration {

    @Bean
    public Exchange routingExchange() {
        return ExchangeBuilder.directExchange("routing-exchange")
                .durable(true)
                .build();
    }

    @Bean
    public Queue routingQueue1() {
        return QueueBuilder.durable("routing-queue1")
                .build();
    }

    @Bean
    public Queue routingQueue2() {
        return QueueBuilder.durable("routing-queue2")
                .build();
    }

    @Bean
    public Binding routingBinding1(@Qualifier("routingQueue1") Queue routingQueue, @Qualifier("routingExchange") Exchange routingExchange) {
        return BindingBuilder.bind(routingQueue).to(routingExchange).with("key-aaa").noargs();
    }

    @Bean
    public Binding routingBinding2(@Qualifier("routingQueue1") Queue routingQueue, @Qualifier("routingExchange") Exchange routingExchange) {
        return BindingBuilder.bind(routingQueue).to(routingExchange).with("key-bbb").noargs();
    }

    @Bean
    public Binding routingBinding3(@Qualifier("routingQueue2") Queue routingQueue, @Qualifier("routingExchange") Exchange routingExchange) {
        return BindingBuilder.bind(routingQueue).to(routingExchange).with("key-ccc").noargs();
    }


}
