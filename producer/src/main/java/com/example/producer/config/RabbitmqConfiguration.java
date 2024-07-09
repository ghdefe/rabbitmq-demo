package com.example.producer.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfiguration {

    @Bean
    public Exchange directExchange() {
        return ExchangeBuilder.directExchange("directExchange")
                .durable(true)
                .build();
    }

}
