package app.controller;

import app.dto.NoteRequest;
import app.dto.NoteResponse;
import app.service.NoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @GetMapping
    public ResponseEntity<List<NoteResponse>> getAll(@RequestParam(name = "tag", required = false) Set<String> tags) {
        return ResponseEntity
                .ok()
                .body(noteService.getNotes(tags));
    }

    @PostMapping
    public ResponseEntity<NoteResponse> addNote(@Valid @RequestBody NoteRequest noteRequest) {
        NoteResponse added = noteService.addNote(noteRequest);
        return ResponseEntity
                .created(URI.create("/notes/" + added.id()))
                .body(added);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteResponse> getNote(@PathVariable Long id) {
        return ResponseEntity
                .ok()
                .body(noteService.getNote(id));
    }


    @PutMapping("/{id}")
    public ResponseEntity<NoteResponse> updateNote(@PathVariable Long id,
                                                   @Valid @RequestBody NoteRequest request) {
        return ResponseEntity
                .ok()
                .body(noteService.updateNote(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        noteService.deleteNote(id);
        return ResponseEntity
                .noContent()
                .build();
    }
}
