package app.exception;

public class NoteNotFoundException extends RuntimeException {

    public NoteNotFoundException(Long id) {
        super("Note with id=%d does not exists".formatted(id));
    }
}
