package src.main.notification.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import src.main.notification.dto.EmpruntEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);
    private final ObjectMapper objectMapper;

    public KafkaConsumerService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @KafkaListener(topics = "emprunt-created", groupId = "notification-group")
    public void consumeEmpruntEvent(String message) {
        try {
            EmpruntEvent event = objectMapper.readValue(message, EmpruntEvent.class);
            logger.info("==============================================");
            logger.info("NOTIFICATION REÇUE !");
            logger.info("==============================================");
            logger.info("Type d'événement: {}", event.getEventType());
            logger.info("ID Emprunt: {}", event.getEmpruntId());
            logger.info("ID Utilisateur: {}", event.getUserId());
            logger.info("ID Livre: {}", event.getBookId());
            logger.info("Timestamp: {}", event.getTimestamp());
            logger.info("==============================================");
            logger.info("Notification envoyée avec succès à l'utilisateur {}", event.getUserId());
            logger.info("==============================================");
        } catch (Exception e) {
            logger.error("Erreur lors du traitement du message Kafka: {}", e.getMessage());
        }
    }
}