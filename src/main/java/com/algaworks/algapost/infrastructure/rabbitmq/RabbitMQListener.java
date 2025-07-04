package com.algaworks.algapost.infrastructure.rabbitmq;

import com.algaworks.algapost.api.model.PostMessage;
import com.algaworks.algapost.api.service.TextProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitMQListener {

    private final TextProcessingService textProcessingService;

    @RabbitListener(queues = RabbitMQConfig.POST_QUEUE)
    public void consumeTextProcessing(@Payload PostMessage postMessage) {
        textProcessingService.receivePost(postMessage);
    }

}
