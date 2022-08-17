package ru.gb.notes.ui.recycler

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.gb.notes.R
import ru.gb.notes.domain.Note
import ru.gb.notes.data.PopupMenuClick

class NotesAdapter : RecyclerView.Adapter<NoteHolder>() {
    private var notes: List<Note> = ArrayList()
    private lateinit var listener: PopupMenuClick
    fun setOnPopupMenuClick(listener: PopupMenuClick) {
        this.listener = listener
    }

    fun delete(all: List<Note>, position: Int) {
        notes = all
        notifyItemRemoved(position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setNotes(notes: List<Note>) {
        this.notes = notes
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.note_item, parent, false)
        return NoteHolder(view, listener)
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        holder.bind(notes[position])
    }

    override fun getItemCount(): Int {
        return notes.size
    }
}