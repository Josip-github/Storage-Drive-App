package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class NoteController {

    private NoteService noteService;
    private UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }



    @PostMapping("/add-note")
    public String addNewNote(@ModelAttribute("note") Note note, Model model, Authentication authentication){
        User user = userService.getUser(authentication.getPrincipal().toString());
        Note newNote = new Note(note.getNoteTitle(), note.getNoteDescription(), user.getUserId());
        noteService.createNewNote(newNote);
        List<Note> notes = noteService.getAllNotesFromThisUser(user.getUserId());
        model.addAttribute("notes", notes);
        return "home";
    }
}
