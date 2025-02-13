package dev.hutchison.rsvp.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WordSetResponse(@JsonProperty("word-set") String wordSet) {
}
