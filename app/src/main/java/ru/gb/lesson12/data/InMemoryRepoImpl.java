package ru.gb.lesson12.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class InMemoryRepoImpl implements Repo {

    private static final String NOTES_SAVE = "NOTES_SAVE";
    private ArrayList<Note> notes = new ArrayList<>();
    private int counter = 0;

    private static Gson gson = new Gson();

    // singleton для базы
    private static InMemoryRepoImpl repo;

    public static Repo getInstance() {
        if (repo == null) {
            repo = new InMemoryRepoImpl();
        }
        return repo;
    }

    @Override
    public List<Note> readPref(String notesString) {
        Type type =  new TypeToken<List<Note>>() {}.getType();
        notes = new GsonBuilder().create().fromJson(notesString, type);
        return notes;
    }

    @Override
    public void writePref(List<Note> notes) {

        String listNotes = gson.toJson(notes);
        SharedPref.write(NOTES_SAVE, listNotes);

    }

    private InMemoryRepoImpl() {

    }

    @Override
    public int create(Note note) {
        int id = counter++;
        note.setId(id);
        notes.add(note);
        return id;
    }

    @Override
    public Note read(int id) {
        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).getId() == id)
                return notes.get(i);
        }
        return null;
    }

    @Override
    public void update(Note note) {
        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).getId() == note.getId()) {
                notes.set(i, note);
                break;
            }
        }
    }

    @Override
    public void delete(int id) {
        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).getId() == id) {
                notes.remove(i);
                break;
            }
        }
    }

    @Override
    public List<Note> getAll() {
        return notes;
    }
}
