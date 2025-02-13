package dev.hutchison.rsvp.service.repository;

import dev.hutchison.rsvp.service.entity.GuestResponseEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface GuestResponseRepository extends MongoRepository<GuestResponseEntity, String> {
    Optional<GuestResponseEntity> findByUserIdAndIndividualId(long userId, long individualId);
}
