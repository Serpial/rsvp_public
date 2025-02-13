package dev.hutchison.rsvp.service.services;

import dev.hutchison.rsvp.service.entity.GuestEntity;
import dev.hutchison.rsvp.service.entity.GuestResponseEntity;
import dev.hutchison.rsvp.service.model.RsvpResponse;
import dev.hutchison.rsvp.service.repository.GuestRepository;
import dev.hutchison.rsvp.service.repository.GuestResponseRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class ResponseService {
    private static final Logger logger = LoggerFactory.getLogger(ResponseService.class);

    private final GuestResponseRepository guestResponseRepository;
    private final GuestRepository guestRepository;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    public List<RsvpResponse> getResponsesForGuest(GuestEntity guestEntity) {
        List<RsvpResponse> guestResponses = new ArrayList<>();
        String[] names = guestEntity.getNames().toArray(new String[0]);
        for (int i = 0; i < names.length; i++) {
            Optional<GuestResponseEntity> maybeResponse = guestResponseRepository.findByUserIdAndIndividualId(guestEntity.getId(), i);
            if (maybeResponse.isEmpty()) {
                continue;
            }

            GuestResponseEntity guestResponseEntity = maybeResponse.get();
            String updated = formatDate(guestResponseEntity.getTimeSent(), dateTimeFormatter);

            guestResponses.add(new RsvpResponse(
                    i,
                    names[i],
                    guestResponseEntity.isConfirmed(),
                    guestResponseEntity.getMenuType(),
                    updated));
        }

        return guestResponses;
    }

    public boolean trySaveResponse(GuestResponseEntity guestResponseEntity) {
        if (!isValidResponse(guestResponseEntity)) {
            return false;
        }

        guestResponseRepository.save(guestResponseEntity);
        return true;
    }

    public boolean trySaveResponses(Stream<GuestResponseEntity> guestResponseEntities) {
        List<GuestResponseEntity> validResponseEntities = guestResponseEntities.filter(this::isValidResponse).toList();
        if (validResponseEntities.isEmpty()) {
            return false;
        }

        guestResponseRepository.saveAll(validResponseEntities);
        return true;
    }

    private boolean isValidResponse(GuestResponseEntity guestResponseEntity) {
        Optional<GuestEntity> maybeGuest = guestRepository.findById(guestResponseEntity.getUserId());
        if (maybeGuest.isEmpty()) {
            logger.error("Could not retrieve guest with ID: {}", guestResponseEntity.getUserId());
            return false;
        }

        GuestEntity guest = maybeGuest.get();
        int numberOfIndividuals = guest.getNames().size();
        long individualId = guestResponseEntity.getIndividualId();
        if (individualId < 0 || individualId > numberOfIndividuals) {
            logger.error("Response for Guest: \"{}\" is invalid [Individual index: {}]", guest.getId(), guestResponseEntity.getIndividualId());
            return false;
        }

        return true;
    }

    public static String formatDate(Date date, DateTimeFormatter formatter) {
        if (date == null) {
            return "";
        }

        ZonedDateTime zonedDateTime = date.toInstant().atZone(ZoneId.systemDefault());
        return formatter.format(zonedDateTime);
    }
}
