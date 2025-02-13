package dev.hutchison.rsvp.service.repository;

import dev.hutchison.rsvp.service.entity.GuestEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface GuestRepository extends MongoRepository<GuestEntity, String> {
    Optional<GuestEntity> findById(long id);

    Optional<GuestEntity> findByWordSet(String wordSet);
}
