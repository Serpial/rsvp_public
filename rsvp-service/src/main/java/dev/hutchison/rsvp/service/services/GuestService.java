package dev.hutchison.rsvp.service.services;

import dev.hutchison.rsvp.service.entity.GuestEntity;
import dev.hutchison.rsvp.service.repository.GuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GuestService {
    private final GuestRepository guestRepository;

    public Optional<GuestEntity> findById(long id) {
        try {
            if (id < 0) {
                return Optional.empty();
            }

            return guestRepository.findById(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<GuestEntity> findByWordSet(String wordSet) {
        try {
            if (null == wordSet || wordSet.isEmpty()) {
                return Optional.empty();
            }

            if (20 != wordSet.length()) {
                return Optional.empty();
            }

            return guestRepository.findByWordSet(wordSet.toLowerCase());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
