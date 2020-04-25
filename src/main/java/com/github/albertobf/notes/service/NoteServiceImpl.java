package com.github.albertobf.notes.service;

import com.github.albertobf.notes.model.Note;
import com.github.albertobf.notes.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    @Override
    public List<Note> getNotesByUserId(Long userId) {
        return noteRepository.findByUser_Id(userId);
    }

    @Override
    public Optional<Note> getNoteByIdAndUserId(Long noteId, Long userId) {
        return noteRepository.findByIdAndUser_Id(noteId, userId);
    }

    @Override
    public Note updateNote(Note note) {
        return noteRepository.save(note);
    }

    @Override
    public Optional<Note> getNoteById(Long noteId) {
        return noteRepository.findById(noteId);
    }
}