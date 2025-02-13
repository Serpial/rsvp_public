package dev.hutchison.rsvp.service.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@SuppressWarnings("unused")
@Getter
@NoArgsConstructor
public class LoginRequest {
    /**
     * Set of words in the format of "sample-sample-sample"
     */
    @NotBlank(message = "wordSet is a required attribute")
    @Pattern(regexp = "^\\w{6}-\\w{6}-\\w{6}$")
    private String wordSet;
}
