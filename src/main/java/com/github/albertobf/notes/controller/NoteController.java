package com.github.albertobf.notes.controller;

import com.github.albertobf.notes.model.Note;
import com.github.albertobf.notes.model.User;
import com.github.albertobf.notes.model.dto.NoteDTO;
import com.github.albertobf.notes.security.AppUserDetails;
import com.github.albertobf.notes.service.NoteService;
import com.github.albertobf.notes.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class NoteController {

    private final NoteService noteService;
    private final UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @GetMapping
    public String index(Model model, @AuthenticationPrincipal AppUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        model.addAttribute("notes", noteService.getNotesByUserId(userId));
        return "index";
    }

    @GetMapping(path = "add")
    public String addNoteForm(Model model) {
        model.addAttribute("note", new NoteDTO());
        return "addNote";
    }

    @PostMapping(path = "add")
    public ModelAndView addNoteSubmit(@AuthenticationPrincipal AppUserDetails userDetails,
                                      @ModelAttribute NoteDTO noteDTO) {
        Note note = noteDTO.getNoteFromDTO();
        Optional<User> user = userService.findByUsername(userDetails.getUsername());
        user.ifPresent(user1 -> {
            note.setUser(user1);
            noteService.addNote(note);
        });
        return new ModelAndView("redirect:/");
    }

    @GetMapping(path = "edit/{id}")
    public String editNoteForm(@PathVariable Long id,
                               @AuthenticationPrincipal AppUserDetails userDetails, Model model) {
        Optional<Note> noteOptional = noteService.getNoteByIdAndUserId(id, userDetails.getUserId());
        Note note = noteOptional.orElseThrow(() -> new RuntimeException("Note not found"));
        model.addAttribute("note", new NoteDTO(note));
        return "editNote";
    }

    @PostMapping(path = "edit/{id}")
    public ModelAndView editNoteSubmit(@ModelAttribute NoteDTO noteDTO, @PathVariable Long id) {
        Optional<Note> noteOptional = noteService.getNoteById(id);
        Note note = noteOptional.orElseThrow(() -> new RuntimeException("Note not found"));
        note.setUpdatedOn(LocalDateTime.now());
        note.setContent(noteDTO.getContent());
        noteService.updateNote(note);
        return new ModelAndView("redirect:/");
    }

}