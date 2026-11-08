package app.service;

import app.dto.NoteRequest;
import app.dto.NoteResponse;
import app.exception.NoteNotFoundException;
import app.mapper.NoteMapper;
import app.model.entity.Note;
import app.repository.NoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NoteServiceImplTest {

    @Mock
    private NoteRepository noteRepository;
    private final NoteMapper noteMapper = Mappers.getMapper(NoteMapper.class);

    @InjectMocks
    private NoteServiceImpl noteService;

    private Note note;
    private NoteRequest request;

    @BeforeEach
    void setUp() {
        noteService = new NoteServiceImpl(noteMapper, noteRepository);
        note = Note.builder()
                .id(1L)
                .title("Title")
                .content("Content")
                .createdAt(OffsetDateTime.now())
                .tags(new HashSet<>(Set.of("tag1", "tag2")))
                .build();

        request = new NoteRequest("Title", "Content", Set.of("tag1", "tag2"));
    }

    // ---------------------------------------------------------------
    // getNotes
    // ---------------------------------------------------------------
    @Nested
    @DisplayName("getNotes")
    class GetNotes {

        @Test
        @DisplayName("без тегов -> вызывает findAll(), не вызывает findIdsByTags() и findAllById()")
        void whenTagsEmpty_thenCallsFindAllOnly() {
            when(noteRepository.findAll()).thenReturn(List.of(note));

            List<NoteResponse> result = noteService.getNotes(List.of());

            assertThat(result).hasSize(1);
            verify(noteRepository).findAll();
            verify(noteRepository, never()).findIdsByTags(anyCollection(), anyInt());
            verify(noteRepository, never()).findAllById(anyCollection());
        }

        @Test
        @DisplayName("null теги -> вызывает findAll(), не вызывает findIdsByTags() и findAllById()")
        void whenTagsNull_thenCallsFindAllOnly() {
            when(noteRepository.findAll()).thenReturn(List.of(note));

            List<NoteResponse> result = noteService.getNotes(null);

            assertThat(result).hasSize(1);
            verify(noteRepository).findAll();
            verify(noteRepository, never()).findIdsByTags(anyCollection(), anyInt());
            verify(noteRepository, never()).findAllById(anyCollection());
        }

        @Test
        @DisplayName("с тегами -> вызывает findIdsByTags() и findAllById(), не вызывает findAll()")
        void whenTagsPresent_thenCallsFindIdsByTagsAndFindAllById() {
            Collection<String> tags = List.of("tag1", "tag2");
            when(noteRepository.findIdsByTags(tags, tags.size())).thenReturn(List.of(1L));
            when(noteRepository.findAllById(List.of(1L))).thenReturn(List.of(note));

            List<NoteResponse> result = noteService.getNotes(tags);

            assertThat(result).hasSize(1);
            verify(noteRepository).findIdsByTags(tags, tags.size());
            verify(noteRepository).findAllById(List.of(1L));
            verify(noteRepository, never()).findAll();
        }
    }

    // ---------------------------------------------------------------
    // addNote
    // ---------------------------------------------------------------
    @Nested
    @DisplayName("addNote")
    class AddNote {

        @Test
        @DisplayName("сохраняет заметку и возвращает ответ")
        void whenSaved_thenReturnsResponse() {
            when(noteRepository.save(any(Note.class))).thenReturn(note);

            NoteResponse response = noteService.addNote(request);

            assertThat(response.id()).isEqualTo(1L);
            assertThat(response.title()).isEqualTo("Title");
            verify(noteRepository).save(any(Note.class));
        }
    }

    // ---------------------------------------------------------------
    // getNote
    // ---------------------------------------------------------------
    @Nested
    @DisplayName("getNote")
    class GetNote {

        @Test
        @DisplayName("находит заметку -> возвращает ответ, без исключений")
        void whenFound_thenReturnsResponse() {
            when(noteRepository.findById(1L)).thenReturn(Optional.of(note));

            NoteResponse response = noteService.getNote(1L);

            assertThat(response.id()).isEqualTo(1L);
            verify(noteRepository).findById(1L);
        }

        @Test
        @DisplayName("не находит -> бросает NoteNotFoundException")
        void whenNotFound_thenThrowsNoteNotFoundException() {
            when(noteRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> noteService.getNote(99L))
                    .isInstanceOf(NoteNotFoundException.class);

            verify(noteRepository).findById(99L);

        }
    }

    // ---------------------------------------------------------------
    // updateNote
    // ---------------------------------------------------------------
    @Nested
    @DisplayName("updateNote")
    class UpdateNote {

        @Test
        @DisplayName("находит заметку -> обновляет и сохраняет")
        void whenFound_thenUpdatesAndSaves() {
            when(noteRepository.findById(1L)).thenReturn(Optional.of(note));

            NoteResponse response = noteService.updateNote(1L, request);

            assertThat(response.title()).isEqualTo("Title");
            verify(noteRepository).findById(1L);
        }

        @Test
        @DisplayName("не находит -> бросает NoteNotFoundException и не вызывает save()")
        void whenNotFound_thenThrowsAndDoesNotSave() {
            when(noteRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> noteService.updateNote(99L, request))
                    .isInstanceOf(NoteNotFoundException.class);

            verify(noteRepository).findById(99L);
            verify(noteRepository, never()).save(any(Note.class));
        }
    }

    // ---------------------------------------------------------------
    // deleteNote
    // ---------------------------------------------------------------
    @Nested
    @DisplayName("deleteNote")
    class DeleteNote {

        @Test
        @DisplayName("находит -> удаляет")
        void whenExists_thenDeletes() {
            when(noteRepository.existsById(1L)).thenReturn(true);

            noteService.deleteNote(1L);

            verify(noteRepository).existsById(1L);
            verify(noteRepository).deleteById(1L);
        }

        @Test
        @DisplayName("не находит -> бросает NoteNotFoundException и не вызывает deleteById()")
        void whenNotExists_thenThrowsAndDoesNotDelete() {
            when(noteRepository.existsById(99L)).thenReturn(false);

            assertThatThrownBy(() -> noteService.deleteNote(99L))
                    .isInstanceOf(NoteNotFoundException.class);

            verify(noteRepository).existsById(99L);
            verify(noteRepository, never()).deleteById(anyLong());
        }
    }
}
