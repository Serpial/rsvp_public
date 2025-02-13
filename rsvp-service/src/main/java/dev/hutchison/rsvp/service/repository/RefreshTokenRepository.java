package dev.hutchison.rsvp.service.repository;

import dev.hutchison.rsvp.service.entity.RefreshTokenEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends MongoRepository<RefreshTokenEntity, String> {
    Optional<RefreshTokenEntity> findByToken(String token);

    void deleteByUserId(long userId);
}
