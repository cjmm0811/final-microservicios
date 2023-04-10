package com.dh.catalog.queue;

import com.dh.catalog.model.Serie;
import com.dh.catalog.repository.SerieRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class SerieListener {
    private final SerieRepository repositorySerie;
    private final RabbitTemplate rabbitTemplate;
    private final Logger logger = LoggerFactory.getLogger(SerieListener.class);

    public SerieListener(RabbitTemplate rabbitTemplate, SerieRepository repositorySerie) {
        this.rabbitTemplate = rabbitTemplate;
        this.repositorySerie = repositorySerie;
    }

    @RabbitListener(queues = {"${queue.serie.name}"})
    public void receiveSerie(@Payload Serie serie) {
        try {
            logger.info("Leyendo cola :" + serie);
            repositorySerie.save(serie);
        } catch (Exception e) {
            logger.error("Error al crear la serie: {}", e.getMessage());
            rabbitTemplate.convertAndSend("error.exchange", "error.routingKey", serie);
        }
    }
}

