package com.github.albertobf.notes.service;

import com.github.albertobf.notes.model.Note;

import java.util.List;
import java.util.Optional;

public interface NoteService {

    Note addNote(Note note);
    List<Note> getNotesByUserId(Long userId);
    Optional<Note> getNoteByIdAndUserId(Long noteId, Long userId);
    Note updateNote(Note note);
    Optional<Note> getNoteById(Long noteId);
}
