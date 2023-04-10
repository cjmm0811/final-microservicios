package com.dh.serie.queue;


import com.dh.serie.model.Serie;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class SerieSender {

    private final RabbitTemplate rabbitTemplate;

    private final Queue serieQueue;

    public void sendSerie(Serie serie) {
        this.rabbitTemplate.convertAndSend(this.serieQueue.getName(), serie);
    }
}
