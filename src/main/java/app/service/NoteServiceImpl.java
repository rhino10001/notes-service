package app.service;

import app.dto.NoteRequest;
import app.dto.NoteResponse;
import app.exception.NoteNotFoundException;
import app.mapper.NoteMapper;
import app.model.entity.Note;
import app.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final NoteMapper noteMapper;
    private final NoteRepository noteRepository;

    @Override
    @Transactional(readOnly = true)
    public List<NoteResponse> getNotes(Collection<String> tags) {
        List<Note> found = CollectionUtils.isEmpty(tags)
                ? noteRepository.findAll()
                : noteRepository.findAllById(noteRepository.findIdsByTags(tags, tags.size()));
        return found
                .stream()
                .map(noteMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public NoteResponse addNote(NoteRequest request) {
        Note saved = noteRepository.save(noteMapper.toEntity(request));
        return noteMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public NoteResponse getNote(Long id) {
        return noteRepository.findById(id)
                .map(noteMapper::toResponse)
                .orElseThrow(() -> new NoteNotFoundException(id));
    }

    @Override
    @Transactional
    public NoteResponse updateNote(Long id, NoteRequest request) {
        Note updated = noteRepository.findById(id).orElseThrow(() -> new NoteNotFoundException(id));
        updated.setTitle(request.title());
        updated.setContent(request.content());
        updated.setTags(new HashSet<>(request.tags()));
        return noteMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteNote(Long id) {
        if (!noteRepository.existsById(id)) {
            throw new NoteNotFoundException(id);
        }
        noteRepository.deleteById(id);
    }
}
