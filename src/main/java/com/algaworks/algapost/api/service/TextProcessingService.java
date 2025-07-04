package com.algaworks.algapost.api.service;

import com.algaworks.algapost.api.model.PostMessage;
import com.algaworks.algapost.api.model.PostResult;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.algaworks.algapost.infrastructure.rabbitmq.RabbitMQConfig.RESULT_EXCHANGE;
import static com.algaworks.algapost.infrastructure.rabbitmq.RabbitMQConfig.RESULT_ROUTING_KEY;

@Service
@RequiredArgsConstructor
public class TextProcessingService {

    private final RabbitTemplate rabbitTemplate;

    public void receivePost(PostMessage postMessage) {
        var countWords = countWords(postMessage.postBody());
        var value = BigDecimal.valueOf(countWords(postMessage.postBody()) * 0.10);
        var postResult = new PostResult(postMessage.postId(), countWords, value);
        rabbitTemplate.convertAndSend(RESULT_EXCHANGE, RESULT_ROUTING_KEY, postResult);
    }

    private int countWords(String body) {
        return body == null || body.trim().isEmpty() ? 0 : body.trim().split("\\s+").length;
    }

}
