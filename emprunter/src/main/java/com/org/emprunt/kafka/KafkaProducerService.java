package com.org.emprunt.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        logger.info("KafkaProducerService initialisé - kafkaTemplate: {}", kafkaTemplate != null);
    }

    public void sendEmpruntCreatedEvent(EmpruntEvent event) {
        logger.info("=== ENVOI ÉVÉNEMENT KAFKA ===");
        logger.info("Topic: emprunt-created");
        logger.info("Event: {}", event);

        try {
            kafkaTemplate.send("emprunt-created", event);
            logger.info("=== ÉVÉNEMENT ENVOYÉ AVEC SUCCÈS ===");
        } catch (Exception e) {
            logger.error("=== ERREUR LORS DE L'ENVOI KAFKA ===", e);
        }
    }
}