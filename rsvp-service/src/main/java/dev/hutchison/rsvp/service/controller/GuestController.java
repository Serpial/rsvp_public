package dev.hutchison.rsvp.service.controller;

import dev.hutchison.rsvp.service.entity.GuestEntity;
import dev.hutchison.rsvp.service.entity.GuestResponseEntity;
import dev.hutchison.rsvp.service.model.NamesResponse;
import dev.hutchison.rsvp.service.model.RsvpRequest;
import dev.hutchison.rsvp.service.model.RsvpResponse;
import dev.hutchison.rsvp.service.model.WordSetResponse;
import dev.hutchison.rsvp.service.security.UserPrincipal;
import dev.hutchison.rsvp.service.services.GuestService;
import dev.hutchison.rsvp.service.services.ResponseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@SuppressWarnings("unused")
@RestController
@RequestMapping("/api/guest")
@RequiredArgsConstructor
public class GuestController {
    private static final Logger logger = LoggerFactory.getLogger(GuestController.class);

    private final GuestService guestService;
    private final ResponseService responseService;

    @GetMapping("/word-set")
    public ResponseEntity<WordSetResponse> wordSet(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Optional<GuestEntity> maybeGuest = guestService.findById(userPrincipal.getUserId());
        if (maybeGuest.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        GuestEntity guest = maybeGuest.get();
        String wordSet = guest.getWordSet();
        return ResponseEntity.ok(new WordSetResponse(wordSet));
    }

    @GetMapping("/names")
    public ResponseEntity<NamesResponse> names(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Optional<GuestEntity> maybeGuest = guestService.findById(userPrincipal.getUserId());
        if (maybeGuest.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        GuestEntity guest = maybeGuest.get();
        String[] names = guest.getNames().toArray(new String[0]);
        return ResponseEntity.ok(new NamesResponse(names));
    }

    @GetMapping("/responses")
    public ResponseEntity<List<RsvpResponse>> getAll(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Optional<GuestEntity> maybeGuest = guestService.findById(userPrincipal.getUserId());
        if (maybeGuest.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        List<RsvpResponse> responsesForGuest = responseService.getResponsesForGuest(maybeGuest.get());
        return ResponseEntity.ok(responsesForGuest);
    }

    private static GuestResponseEntity mapToGuestResponseEntity(long userId, Date date, RsvpRequest rsvpRequest) {
        if (!rsvpRequest.isConfirmed()) {
            return GuestResponseEntity.builder()
                    .userId(userId)
                    .confirmed(rsvpRequest.isConfirmed())
                    .individualId(rsvpRequest.getIndividualId())
                    .timeSent(date)
                    .build();
        }

        return GuestResponseEntity.builder()
                .userId(userId)
                .confirmed(rsvpRequest.isConfirmed())
                .individualId(rsvpRequest.getIndividualId())
                .menuType(rsvpRequest.getMenuType())
                .timeSent(date)
                .build();
    }

    @PostMapping("/response")
    public ResponseEntity<?> saveResponse(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody RsvpRequest rsvpRequest) {

        final Date date = new Date();
        GuestResponseEntity guestResponseEntity = mapToGuestResponseEntity(userPrincipal.getUserId(), date, rsvpRequest);
        return responseService.trySaveResponse(guestResponseEntity)
                ? ResponseEntity.status(HttpStatus.ACCEPTED).build()
                : ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    @PostMapping("/responses")
    public ResponseEntity<?> saveResponses(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody RsvpRequest[] rsvpRequests) {

        if (rsvpRequests.length == 0) {
            logger.error("No responses provided [User:{}]", userPrincipal.getUserId());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        final Date date = new Date();
        Stream<GuestResponseEntity> guestResponseEntities = Arrays.stream(rsvpRequests)
                .map(rsvpRequest -> mapToGuestResponseEntity(userPrincipal.getUserId(), date, rsvpRequest));

        return responseService.trySaveResponses(guestResponseEntities)
                ? ResponseEntity.status(HttpStatus.ACCEPTED).build()
                : ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }
}
