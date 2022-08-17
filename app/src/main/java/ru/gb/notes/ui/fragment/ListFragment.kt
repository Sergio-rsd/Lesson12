package ru.gb.notes.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.gb.notes.R
import ru.gb.notes.data.InMemoryRepoImpl
import ru.gb.notes.domain.Note
import ru.gb.notes.data.PopupMenuClick
import ru.gb.notes.ui.recycler.NoteHolder
import ru.gb.notes.ui.recycler.NotesAdapter

class ListFragment : Fragment() {
    private val repository = InMemoryRepoImpl.instance
    private val adapter = NotesAdapter()
    private lateinit var listAdapter: RecyclerView
    private var note: Note? = null
    private var prefs: SharedPreferences? = null

    interface RecyclerController {
        fun connect()
    }

    private var recyclerController: RecyclerController? = null
    override fun onAttach(context: Context) {
        recyclerController = context as RecyclerController
        prefs = PreferenceManager.getDefaultSharedPreferences(context)
//        prefs = context.getSharedPreferences(context)

        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recycle_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter.setOnPopupMenuClick(requireContext() as PopupMenuClick)
        listAdapter = view.findViewById(R.id.list_notes)
        listAdapter.adapter = adapter
        listAdapter.layoutManager = LinearLayoutManager(requireContext())
        adapter.setNotes(repository?.all as List<Note>)

        // Helper
        val helper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val swipeFlag = ItemTouchHelper.START or ItemTouchHelper.END
                return makeMovementFlags(0, swipeFlag)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                val position = viewHolder.adapterPosition
                val position = viewHolder.bindingAdapterPosition
                val holder = viewHolder as NoteHolder
                val note = holder.note
                note!!.id?.let { repository.delete(it) }
                adapter.delete(repository.all as List<Note>, position)
            }
        })
        helper.attachToRecyclerView(listAdapter)
    }

    fun passData(note: Note?): Note? {
        this.note = note
        return note
    }

    fun delete(note: Note, position: Int) {
        note.id?.let { repository?.delete(it) }
        adapter.delete(repository?.all as List<Note>, position)
    }

    fun update(note: Note?) {
        repository?.update(note)
        adapter.setNotes(repository?.all as List<Note>)
    }

    fun create(note: Note?) {
        repository?.create(note)
        adapter.setNotes(repository?.all as List<Note>)
    }
/*
    companion object {
        const val NOTES_SAVE = "NOTES_SAVE"
    }
    */
}