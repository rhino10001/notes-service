package app.dto;

import java.time.OffsetDateTime;
import java.util.Set;

public record NoteResponse(Long id,
                           String title,
                           String content,
                           OffsetDateTime createdAt,
                           Set<String> tags) {
}
