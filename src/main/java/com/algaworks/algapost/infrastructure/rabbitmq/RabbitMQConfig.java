package com.algaworks.algapost.infrastructure.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String POST_EXCHANGE = "text-processor-service.post-processing.v1.ex";
    public static final String POST_QUEUE = "text-processor-service.post-processing.v1.q";
    public static final String POST_DLQ = "text-processor-service.post-processing.v1.dlq";
    public static final String POST_ROUTING_KEY = "post.created";

    public static final String RESULT_EXCHANGE = "post-service.post-processing-result.v1.ex";
    public static final String RESULT_QUEUE = "post-service.post-processing-result.v1.q";
    public static final String RESULT_DLQ = "post-service.post-processing-result.v1.dlq";
    public static final String RESULT_ROUTING_KEY = "post.processed";

    public static final String DLX = "text-processor-service.dead-letter.v1.ex";

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public DirectExchange postExchange() {
        return new DirectExchange(POST_EXCHANGE);
    }
    @Bean
    public DirectExchange resultExchange() {
        return new DirectExchange(RESULT_EXCHANGE);
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DLX);
    }

    @Bean
    public Queue postQueue() {
        return QueueBuilder.durable(POST_QUEUE)
                .withArgument("x-dead-letter-exchange", DLX)
                .withArgument("x-dead-letter-routing-key", POST_DLQ)
                .build();
    }

    @Bean
    public Queue resultQueue() {
        return QueueBuilder.durable(RESULT_QUEUE)
                .withArgument("x-dead-letter-exchange", DLX)
                .withArgument("x-dead-letter-routing-key", RESULT_DLQ)
                .build();
    }

    @Bean
    public Queue postDlq() {
        return QueueBuilder.durable(POST_DLQ).build();
    }

    @Bean
    public Queue resultDlq() {
        return QueueBuilder.durable(RESULT_DLQ).build();
    }

    @Bean
    public Binding bindPostQueue() {
        return BindingBuilder.bind(postQueue())
                .to(postExchange())
                .with(POST_ROUTING_KEY);
    }

    @Bean
    public Binding bindResultQueue() {
        return BindingBuilder.bind(resultQueue())
                .to(resultExchange())
                .with(RESULT_ROUTING_KEY);
    }

    @Bean
    public Binding bindPostDlq() {
        return BindingBuilder.bind(postDlq())
                .to(deadLetterExchange())
                .with(POST_DLQ);
    }

    @Bean
    public Binding bindResultDlq() {
        return BindingBuilder.bind(resultDlq())
                .to(deadLetterExchange())
                .with(RESULT_DLQ);
    }

}
