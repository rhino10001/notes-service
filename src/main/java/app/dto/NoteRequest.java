package app.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Collection;

public record NoteRequest(@NotBlank(message = "can not be empty") String title,
                          String content,
                          Collection<String> tags) {
}
