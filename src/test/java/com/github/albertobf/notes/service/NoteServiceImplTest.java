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

}