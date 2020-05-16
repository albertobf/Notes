package com.github.albertobf.notes.controller;

import com.github.albertobf.notes.bootstrap.IniUserDatabase;
import com.github.albertobf.notes.model.Note;
import com.github.albertobf.notes.service.NoteService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@WithMockUser(username = IniUserDatabase.USERNAME)
class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NoteService noteService;

    @Test
    @WithAnonymousUser
    public void whenAccessNotesAnonymous_thenRedirectLogin() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithUserDetails(IniUserDatabase.USERNAME)
    public void whenAccessIndexUserWithNotes_thenReturnUserNotes() throws Exception {
        Note note = new Note();
        note.setContent("Test Note");
        when(noteService.getNotesByUserId(anyLong())).thenReturn(Collections.singletonList(note));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("notes", hasSize(1)));

        verify(noteService).getNotesByUserId(anyLong());
    }

    @Test
    @WithUserDetails(IniUserDatabase.USERNAME)
    public void whenAccessIndexUserWithNoNotes_thenReturnEmptyNotes() throws Exception {
        when(noteService.getNotesByUserId(anyLong())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("notes", hasSize(0)));

        verify(noteService).getNotesByUserId(anyLong());
    }

    @Test
    public void whenAddNoteForm_thenReturnView() throws Exception{
        mockMvc.perform(get("/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("addNote"))
                .andExpect(model().attributeExists("noteDTO"));
    }

    @Test
    @WithUserDetails(IniUserDatabase.USERNAME)
    public void whenAddValidNoteSubmit_thenSaveNoteAndRedirectIndex() throws Exception {
        mockMvc
                .perform(post("/add")
                        .with(csrf())
                        .param("content", "Test Note"))
                .andExpect(model().hasNoErrors())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(noteService).addNote(ArgumentMatchers.any());
    }

    @Test
    @WithUserDetails(IniUserDatabase.USERNAME)
    public void whenAddEmptyNoteSubmit_thenReturnAddViewWithError() throws Exception {
        mockMvc
                .perform(post("/add")
                        .with(csrf())
                        .param("content", ""))
                .andExpect(model().errorCount(1))
                .andExpect(status().isOk())
                .andExpect(view().name("addNote"));

        verifyNoInteractions(noteService);
    }

    @Test
    @WithUserDetails(IniUserDatabase.USERNAME)
    public void whenEditNoteWithInvalidNumberId_thenReturnBadRequest() throws Exception {
        mockMvc.perform(get("/edit/{id}", "12sd"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(noteService);
    }

    @Test
    @WithUserDetails(IniUserDatabase.USERNAME)
    public void whenFormEditInvalidNote_thenReturnNotFound() throws Exception {
        when(noteService.getNoteByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/edit/{id}", 1L))
                .andExpect(status().isNotFound());

        verify(noteService).getNoteByIdAndUserId(anyLong(), anyLong());
    }

    @Test
    @WithUserDetails(IniUserDatabase.USERNAME)
    public void whenFormEditValidNote_thenReturnTheNote() throws Exception {
        Note note = new Note();
        note.setContent("Testing edit note");
        when(noteService.getNoteByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.of(note));

        mockMvc.perform(get("/edit/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("editNote"))
                .andExpect(model().attributeExists("note"));

        verify(noteService).getNoteByIdAndUserId(anyLong(), anyLong());
    }

    @Test
    @WithUserDetails(IniUserDatabase.USERNAME)
    public void whenSubmitValidNote_thenSaveAndRedirectIndex() throws Exception {
        Note note = new Note();
        note.setContent("Testing edit note");
        when(noteService.getNoteById(anyLong())).thenReturn(Optional.of(note));

        mockMvc
                .perform(post("/edit/{id}", 1L)
                        .with(csrf())
                        .param("content", "Test Note"))
                .andExpect(model().hasNoErrors())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(noteService).updateNote(any());
    }

    @Test
    @WithUserDetails(IniUserDatabase.USERNAME)
    public void whenSubmitInvalidNote_thenReturnNotFound() throws Exception {
        when(noteService.getNoteById(anyLong())).thenReturn(Optional.empty());

        mockMvc
                .perform(post("/edit/{id}", 1L)
                        .with(csrf())
                        .param("content", "Test Note"))
                .andExpect(status().isNotFound());

        verify(noteService, never()).updateNote(any());
    }

}