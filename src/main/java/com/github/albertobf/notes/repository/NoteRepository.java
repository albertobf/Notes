package com.github.albertobf.notes.repository;

import com.github.albertobf.notes.model.Note;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends CrudRepository<Note, Long> {

    List<Note> findByUser_Id(Long userId);
    Optional<Note> findByIdAndUser_Id(Long noteId, Long userId);

}
