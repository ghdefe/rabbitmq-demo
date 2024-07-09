package com.example.consumer2.config;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FanoutRabbitmqConfiguration {
    /**
     * 广播交换机
     */
    @Bean
    public Exchange fanoutExchange() {
        return ExchangeBuilder.fanoutExchange("fanout-exchange")
                .durable(true)
                .build();
    }

    /**
     * 广播队列1
     * @return
     */
    @Bean
    public Queue fanout2Queue() {
        return QueueBuilder.durable("fanout-queue2")
                .build();
    }

    /**
     * 将广播队列绑定到广播交换机
     */
    @Bean
    public Binding fanoutBinding(@Qualifier("fanout2Queue") Queue fanout1Queue, @Qualifier("fanoutExchange") Exchange fanoutExchange) {
        return BindingBuilder.bind(fanout1Queue).to(fanoutExchange).with("").noargs();
    }
}
