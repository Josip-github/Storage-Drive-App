package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.repository.NoteMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public void createNewNote(Note note){
        noteMapper.insert(note);
    }

    public List<Note> getAllNotesFromThisUser(Integer id){
        return noteMapper.getNotesFromThisUser(id);
    }

    public void deleteNote(Integer noteId){
        this.noteMapper.deleteNote(noteId);
    }

    public void updateNote(Note note){
        this.noteMapper.updateNote(note);
    }
}
