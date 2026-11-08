package app.mapper;

import app.dto.NoteRequest;
import app.dto.NoteResponse;
import app.model.entity.Note;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

import java.time.OffsetDateTime;

@Mapper(componentModel = "spring", imports = OffsetDateTime.class)
public interface NoteMapper {

    NoteResponse toResponse(Note note);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(OffsetDateTime.now())")
    @Mapping(target = "tags", source = "tags", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    Note toEntity(NoteRequest request);
}