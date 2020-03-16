package com.github.albertobf.notes.repository;

import com.github.albertobf.notes.model.Note;
import com.github.albertobf.notes.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
    public void whenFindNotesByUserId_thenReturnNotes() {
        //given
        Note note = new Note();
        note.setContent("Test note!");
        note.setCreatedOn(LocalDate.now());
        note.setUser(userFromDb);
        entityManager.persistAndFlush(note);

        //when
        List<Note> notes = noteRepository.findByUser_Id(userFromDb.getId());

        //then
        assertThat(notes.get(0).getContent()).isEqualTo(note.getContent());
    }

}