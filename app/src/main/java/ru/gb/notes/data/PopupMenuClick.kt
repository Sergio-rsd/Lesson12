package ru.gb.notes.data

import ru.gb.notes.domain.Note

interface PopupMenuClick {
    fun click(command: Int, note: Note?, position: Int)
}