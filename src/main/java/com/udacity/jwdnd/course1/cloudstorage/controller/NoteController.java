package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class NoteController {

    private NoteService noteService;
    private UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @ModelAttribute("note")
    public Note getNote(){
        return new Note();
    }

    @PostMapping("/add-note")
    public String addNewNoteOrUpdate(@ModelAttribute(value = "newNote") Note note, Model model, Authentication authentication){
        User user = userService.getUser(authentication.getPrincipal().toString());
        note.setUserId(user.getUserId());

        if(note.getNoteId() != null){
            try {
                noteService.updateNote(note);
                model.addAttribute("successMessage", "Your note was edited succesfully.");
                return "result";
            } catch (Exception e){
                model.addAttribute("errorMessage", "Something went wrong with the editing. Please try one more time.");
                return "result";
            }
        } else {
            try {
                Note newNote = new Note(note.getNoteTitle(), note.getNoteDescription(), user.getUserId());
                noteService.createNewNote(newNote);
                model.addAttribute("successMessage", "The note added successfully!");
                return "result";
            } catch (Exception e){
                model.addAttribute("errorMessage", "Something went wrong with adding the note... Please try again!");
                return "result";
            }
        }
    }

    @GetMapping("/note-delete/{noteId}")
    public String deleteNote(@PathVariable Integer noteId, Authentication authentication, Model model){
        User user = userService.getUser(authentication.getPrincipal().toString());
        model.addAttribute("errorMessage", false);
        model.addAttribute("successMessage", false);

        try {
            noteService.deleteNote(noteId);
            List<Note> notes = noteService.getAllNotesFromThisUser(user.getUserId());
            model.addAttribute("successMessage", "You've successfully deleted the note.");
        } catch(Exception e){
            e.printStackTrace();
            model.addAttribute("errorMessage", e.getMessage());
        }

        return "result";
    }
}
