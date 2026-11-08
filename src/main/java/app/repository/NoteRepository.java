package app.repository;

import app.model.entity.Note;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    @Override
    @EntityGraph(attributePaths = {"tags"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Note> findAll();

    @Query("SELECT n.id FROM Note n " +
           "JOIN n.tags t WHERE t IN :tags " +
           "GROUP BY n " +
           "HAVING COUNT(DISTINCT t) = :count")
    List<Long> findIdsByTags(Iterable<String> tags, int count);

    @Override
    @EntityGraph(attributePaths = {"tags"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Note> findAllById(Iterable<Long> ids);

    @Override
    @EntityGraph(attributePaths = {"tags"}, type = EntityGraph.EntityGraphType.FETCH)
    Optional<Note> findById(Long id);
}
