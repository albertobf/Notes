package com.github.albertobf.notes.service;

import com.github.albertobf.notes.model.Note;

import java.util.List;

public interface NoteService {

    Note addNote(Note note);
    List<Note> getNotesByUserId(Long userId);

}
