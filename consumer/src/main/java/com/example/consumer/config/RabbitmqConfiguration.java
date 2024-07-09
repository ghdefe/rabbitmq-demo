package com.example.consumer.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfiguration {

//    @Bean
//    public Exchange directExchange() {
//        return ExchangeBuilder.directExchange("directExchange")
//                .durable(true)
//                .build();
//    }
//
//    @Bean
//    public Queue queue1() {
//        return new Queue("queue1");
//    }
//
//    @Bean
//    public Binding binding(@Qualifier("queue1") Queue queue1, @Qualifier("directExchange") Exchange directExchange) {
//        return BindingBuilder.bind(queue1).to(directExchange).with("hello").noargs();
//    }

}
