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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String index() {
        return "index";
    }

    @GetMapping(path = "add")
    public String addNoteForm(Model model) {
        model.addAttribute("note", new NoteDTO());
        return "addNote";
    }

    @PostMapping(path = "add")
    public String addNoteSubmit(@AuthenticationPrincipal AppUserDetails userDetails, @ModelAttribute NoteDTO noteDTO) {
        Note note = noteDTO.getNoteFromDTO();
        Optional<User> user = userService.findByUsername(userDetails.getUsername());
        user.ifPresent(user1 -> {
            note.setUser(user1);
            noteService.addNote(note);
        });
        return "index";
    }

}