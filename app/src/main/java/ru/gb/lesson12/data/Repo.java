package ru.gb.lesson12.data;

import java.util.ArrayList;
import java.util.List;

public interface Repo {
    // CRUD
    // ------
    // Create
    // Read
    // Update
    // Delete
    int  create(Note note);
    Note read(int id);
    void update(Note note);
    void delete(int id);
    void writePref(List<Note> notes);
    List<Note> readPref(String notesString);
    List<Note> getAll();
    int readCounter(List<Note> notes);
}
