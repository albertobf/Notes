package com.github.albertobf.notes.controller;

import com.github.albertobf.notes.model.Note;
import com.github.albertobf.notes.model.User;
import com.github.albertobf.notes.model.dto.NoteDTO;
import com.github.albertobf.notes.security.AppUserDetails;
import com.github.albertobf.notes.service.NoteService;
import com.github.albertobf.notes.service.UserService;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
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
    public String addNoteForm(NoteDTO note) {
        return "addNote";
    }

    @PostMapping(path = "add")
    public String addNoteSubmit(@AuthenticationPrincipal AppUserDetails userDetails,
                                @Valid NoteDTO noteDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "addNote";
        }

        Note note = noteDTO.getNoteFromDTO();
        Optional<User> user = userService.findByUsername(userDetails.getUsername());
        user.ifPresent(user1 -> {
            note.setUser(user1);
            noteService.addNote(note);
        });
        return "redirect:/";
    }

    @GetMapping(path = "edit/{id}")
    public String editNoteForm(@PathVariable Long id,
                               @AuthenticationPrincipal AppUserDetails userDetails, Model model) throws NotFoundException {
        Optional<Note> noteOptional = noteService.getNoteByIdAndUserId(id, userDetails.getUserId());
        Note note = noteOptional.orElseThrow(() -> new NotFoundException("Note not found. Note ID is " + id));
        model.addAttribute("note", new NoteDTO(note));
        return "editNote";
    }

    @PostMapping(path = "edit/{id}")
    public ModelAndView editNoteSubmit(@ModelAttribute NoteDTO noteDTO, @PathVariable Long id) throws NotFoundException {
        Optional<Note> noteOptional = noteService.getNoteById(id);
        Note note = noteOptional.orElseThrow(() -> new NotFoundException("Note not found. Note ID is " + id));
        note.setUpdatedOn(LocalDateTime.now());
        note.setContent(noteDTO.getContent());
        noteService.updateNote(note);
        return new ModelAndView("redirect:/");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handleNotFound(Exception exception) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("404error");
        modelAndView.addObject("exception", exception.getMessage());
        return modelAndView;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NumberFormatException.class)
    public ModelAndView handleNumberFormat(Exception exception) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("400error");
        modelAndView.addObject("exception", exception.getMessage());
        return modelAndView;
    }

}