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
//    private static SharedPreferences prefs;

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
//        notes = gson.fromJson(notesString, new TypeToken<List<Note>>() {}.getType());
//        notes = gson.fromJson(notesString, new TypeToken<List<Note>>() {}.getType());
        Type type =  new TypeToken<List<Note>>() {}.getType();
        notes = new GsonBuilder().create().fromJson(notesString, type);

/*        StringBuilder builder = new StringBuilder();
        for (Note n : notes) {
            builder.append(n.getId());
            builder.append(n.getTitle());
            builder.append(n.getDescription());
            builder.append(n.getInterest());
            builder.append(n.getDataPerformance());

        }*/
//        notesString

        return notes;
    }

    @Override
    public void writePref(List<Note> notes) {

        String listNotes = gson.toJson(notes);
        SharedPref.write(NOTES_SAVE, listNotes);
//        prefs
//                .edit()
//                .putString(NOTES_SAVE, listNotes)
//                .apply();
    }

    private InMemoryRepoImpl() {

 /*
        create(new Note("Title 1", "Description 1"));
        create(new Note("Title 2", "Description 2"));
        create(new Note("Title 3", "Description 3"));
        create(new Note("Title 4", "Description 4"));
        create(new Note("Title 5", "Description 5"));
        create(new Note("Title 6", "Description 6"));
        create(new Note("Title 7", "Description 7"));
        create(new Note("Title 8", "Description 8"));
        create(new Note("Title 9", "Description 9"));
        create(new Note("Title 10", "Description 10"));
        create(new Note("Title 11", "Description 11"));
        create(new Note("Title 12", "Description 12"));
        create(new Note("Title 13", "Description 13"));
        create(new Note("Title 14", "Description 14"));
        create(new Note("Title 15", "Description 15"));
        create(new Note("Title 16", "Description 16"));
  */

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
