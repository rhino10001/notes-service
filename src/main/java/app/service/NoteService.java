package app.service;

import app.dto.NoteRequest;
import app.dto.NoteResponse;

import java.util.Collection;
import java.util.List;

public interface NoteService {

    List<NoteResponse> getNotes(Collection<String> tag);

    NoteResponse addNote(NoteRequest request);

    NoteResponse getNote(Long id);

    NoteResponse updateNote(Long id, NoteRequest request);

    void deleteNote(Long id);
}
