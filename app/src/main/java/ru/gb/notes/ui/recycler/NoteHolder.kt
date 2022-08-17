package ru.gb.notes.ui.recycler

import ru.gb.notes.data.PopupMenuClick
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import android.annotation.SuppressLint
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import ru.gb.notes.R
import ru.gb.notes.domain.Note

class NoteHolder(itemView: View, private val listener: PopupMenuClick) :
    RecyclerView.ViewHolder(itemView), PopupMenu.OnMenuItemClickListener {
    private val id: TextView? = null
    private val title: TextView
    private val description: TextView
    private val interest: TextView
    private val dataPerformance: TextView
    var note: Note? = null
        private set
    private val noteMenu: ImageView
    private val popupMenu: PopupMenu

    @SuppressLint("SetTextI18n")
    fun bind(note: Note) {
        this.note = note
        title.text = note.title
        description.text = note.description
        interest.text = note.interest
        dataPerformance.text = note.dataPerformance
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.context_modify -> {
//                listener.click(R.id.context_modify, note, adapterPosition)
                listener.click(R.id.context_modify, note, bindingAdapterPosition)
                true
            }
            R.id.context_delete -> {
//                listener.click(R.id.context_delete, note, adapterPosition)
                listener.click(R.id.context_delete, note, bindingAdapterPosition)
                true
            }
            else -> false
        }
    }

    init {

//        id = itemView.findViewById(R.id.note_id);
        title = itemView.findViewById(R.id.note_title)
        description = itemView.findViewById(R.id.note_description)
        interest = itemView.findViewById(R.id.interest_currency)
        dataPerformance = itemView.findViewById(R.id.data_stamp)
        noteMenu = itemView.findViewById(R.id.note_menu)
        popupMenu = PopupMenu(itemView.context, noteMenu)
        popupMenu.inflate(R.menu.context)
        noteMenu.setOnClickListener { popupMenu.show() }
        popupMenu.setOnMenuItemClickListener(this)
    }
}