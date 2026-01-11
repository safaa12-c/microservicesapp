package com.org.emprunt.service;

import com.org.emprunt.DTO.EmpruntDetailsDTO;
import com.org.emprunt.entities.Emprunter;
import com.org.emprunt.feign.BookClient;
import com.org.emprunt.feign.UserClient;
import com.org.emprunt.repositories.EmpruntRepository;
import com.org.emprunt.kafka.EmpruntEvent;
import com.org.emprunt.kafka.KafkaProducerService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class EmpruntService {

    private final EmpruntRepository repo;
    private final UserClient userClient;
    private final BookClient bookClient;
    private final KafkaProducerService kafkaProducer;

    public EmpruntService(
            EmpruntRepository repo,
            UserClient userClient,
            BookClient bookClient,
            KafkaProducerService kafkaProducer) {

        this.repo = repo;
        this.userClient = userClient;
        this.bookClient = bookClient;
        this.kafkaProducer = kafkaProducer;
    }

    public Emprunter createEmprunt(Long userId, Long bookId) {

        userClient.getUser(userId);

        bookClient.getBook(bookId);

        Emprunter emprunt = new Emprunter();
        emprunt.setUserId(userId);
        emprunt.setBookId(bookId);

        Emprunter savedEmprunt = repo.save(emprunt);

        EmpruntEvent event = new EmpruntEvent(
                savedEmprunt.getId(),
                userId,
                bookId,
                "EMPRUNT_CREATED",
                LocalDateTime.now()
        );

        kafkaProducer.sendEmpruntCreatedEvent(event);

        return savedEmprunt;
    }

    public List<EmpruntDetailsDTO> getAllEmprunts() {

        return repo.findAll().stream().map(e -> {

            var user = userClient.getUser(e.getUserId());
            var book = bookClient.getBook(e.getBookId());

            return new EmpruntDetailsDTO(
                    e.getId(),
                    user.getName(),
                    book.getTitle(),
                    e.getEmpruntDate()
            );

        }).collect(Collectors.toList());
    }
}
