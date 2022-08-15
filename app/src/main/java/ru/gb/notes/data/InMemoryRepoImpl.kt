package ru.gb.notes.data

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import ru.gb.notes.data.SharedPref.write

//class InMemoryRepoImpl private constructor() : Repo {
class InMemoryRepoImpl : Repo {
    private var notes = ArrayList<Note?>()
    private var counter = 0

    override fun readPref(notesString: String?): List<Note?> {
        val type = object : TypeToken<List<Note?>?>() {}.type
        notes = GsonBuilder().create().fromJson(notesString, type)
        counter = readCounter(notes)
        return notes
    }

    override fun writePref(notes: List<Note?>?) {
        val listNotes = gson.toJson(notes)
        write(NOTES_SAVE, listNotes)
    }

    override fun create(note: Note?): Int {
        val id = counter++
        note!!.id = id
        notes.add(note)
        return id
    }

    override fun read(id: Int): Note? {
        for (i in notes.indices) {
            if (notes[i]!!.id == id) return notes[i]
        }
        return null
    }

    override fun update(note: Note?) {
        for (i in notes.indices) {
            if (notes[i]!!.id == note!!.id) {
                notes[i] = note
                break
            }
        }
    }

    override fun delete(id: Int) {
        for (i in notes.indices) {
            if (notes[i]!!.id == id) {
                notes.removeAt(i)
                break
            }
        }
        for ((j, singleNote) in notes.withIndex()) {
            singleNote!!.id = j
            //            Log.d(TAG, "Note id: " + notes.get(j).getId() );
        }
    }

    override val all: List<Note?>
        get() = notes

    override fun readCounter(notes: List<Note?>?): Int {
        if (notes == null) {
            this.notes = ArrayList()
            counter = 0
        } else {
            counter = if (notes.isEmpty()) {
                0
            } else {
//                counter = notes.get(notes.size() - 1).getId();
                notes.size - 1
            }
        }
        return counter
    }

    companion object {
        private const val NOTES_SAVE = "NOTES_SAVE"

        //    public static final String TAG = "happy";
        private val gson = Gson()

        // singleton для базы
        private var repo: InMemoryRepoImpl? = null
        val instance: Repo?
            get() {
                if (repo == null) {
                    repo = InMemoryRepoImpl()
                }
                return repo
            }
    }
}