package com.github.albertobf.notes.model.dto;

import com.github.albertobf.notes.model.Note;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class NoteDTO {

    private Long id;
    @NotNull
    @Size(min=1, max=255)
    private String content;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;

    public NoteDTO() {

    }

    public NoteDTO(Note note) {
        this.id = note.getId();
        this.content = note.getContent();
        this.createdOn = note.getCreatedOn();
        this.updatedOn = note.getUpdatedOn();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Note getNoteFromDTO() {
        return new Note(
                content
        );
    }

}