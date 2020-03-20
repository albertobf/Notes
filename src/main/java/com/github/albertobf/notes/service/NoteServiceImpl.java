package com.github.albertobf.notes.service;

import com.github.albertobf.notes.model.Note;
import com.github.albertobf.notes.repository.NoteRepository;
import org.springframework.stereotype.Service;

@Service
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;

    public NoteServiceImpl(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    public Note addNote(Note note) {
        return noteRepository.save(note);
    }
}