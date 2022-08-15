package ru.gb.notes.data

interface PopupMenuClick {
    fun click(command: Int, note: Note?, position: Int)
}