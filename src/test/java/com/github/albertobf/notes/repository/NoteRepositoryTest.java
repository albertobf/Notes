package com.github.albertobf.notes.repository;

import com.github.albertobf.notes.model.Note;
import com.github.albertobf.notes.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class NoteRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private NoteRepository noteRepository;

    private User userFromDb;

    @BeforeEach
    public void setUp() {
        User user = new User();
        user.setName("Alberto");
        user.setPassword("Password");
        user.setUsername("albertobf");
        userFromDb = entityManager.persistAndFlush(user);
    }

    @Test
    public void whenFindNotesByExistingUserId_thenReturnNotes() {
        //given
        Note note = new Note();
        note.setContent("Test note!");
        note.setCreatedOn(LocalDateTime.now());
        note.setUser(userFromDb);
        entityManager.persistAndFlush(note);

        //when
        List<Note> notes = noteRepository.findByUser_Id(userFromDb.getId());

        //then
        assertThat(notes.get(0).getContent()).isEqualTo(note.getContent());
    }

    @Test
    public void whenFindNotesByNotExistingUserId_thenReturnNotes() {
        //given
        Note note = new Note();
        note.setContent("Test note!");
        note.setCreatedOn(LocalDateTime.now());
        note.setUser(userFromDb);
        entityManager.persistAndFlush(note);

        //when
        List<Note> notes = noteRepository.findByUser_Id(-1L);

        //then
        assertThat(notes).isEqualTo(Collections.emptyList());
    }

    @Test
    public void whenFindNoteByValidIdAndUserId_thenReturnNote() {
        //given
        Note note = new Note();
        note.setContent("Test note!");
        note.setCreatedOn(LocalDateTime.now());
        note.setUser(userFromDb);
        entityManager.persistAndFlush(note);

        //when
        Optional<Note> noteDB = noteRepository.findByIdAndUser_Id(note.getId(), userFromDb.getId());

        //then
        assertTrue(noteDB.isPresent());
        assertThat(noteDB.get()).isEqualTo(note);
    }

    @Test
    public void whenFindNoteByInvalidIdAndUserId_thenReturnNote() {
        //given
        Note note = new Note();
        note.setContent("Test note!");
        note.setCreatedOn(LocalDateTime.now());
        note.setUser(userFromDb);
        entityManager.persistAndFlush(note);

        //when
        Optional<Note> noteDB = noteRepository.findByIdAndUser_Id(note.getId(), -1L);

        //then
        assertFalse(noteDB.isPresent());
    }

}