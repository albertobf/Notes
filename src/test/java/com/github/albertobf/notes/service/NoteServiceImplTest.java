package com.github.albertobf.notes.service;

import com.github.albertobf.notes.model.Note;
import com.github.albertobf.notes.model.User;
import com.github.albertobf.notes.repository.NoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NoteServiceImplTest {

    @InjectMocks
    private NoteServiceImpl noteService;
    @Mock
    private NoteRepository noteRepository;
    private Note note;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("albertobf");
        user.setId(5L);
        note = new Note();
        note.setUser(user);
        note.setId(5L);
        note.setContent("Test note");
    }

    @Test
    void whenSaveNote_thenReturnSavedNote() {
        //given
        when(noteRepository.save(any(Note.class))).thenReturn(note);
        //when
        Note savedNote = noteService.addNote(note);
        //then
        assertThat(savedNote).isNotNull();
        verify(noteRepository).save(eq(note));
    }

    @Test
    void whenGetNotesByExistingUser_thenReturnNotes() {
        //given
        List<Note> notes = Collections.singletonList(note);
        when(noteRepository.findByUser_Id(user.getId())).thenReturn(notes);
        //when
        List<Note> notesDb = noteService.getNotesByUserId(user.getId());
        //then
        verify(noteRepository).findByUser_Id(user.getId());
        assertThat(notesDb).isEqualTo(notes);
    }

    @Test
    void whenGetNotesByNotExistingUser_thenReturnEmptyList() {
        //given
        when(noteRepository.findByUser_Id(-1L)).thenReturn(Collections.emptyList());
        //when
        List<Note> notesDb = noteService.getNotesByUserId(-1L);
        //then
        verify(noteRepository).findByUser_Id(-1L);
        assertThat(notesDb).isEqualTo(Collections.emptyList());
    }

}