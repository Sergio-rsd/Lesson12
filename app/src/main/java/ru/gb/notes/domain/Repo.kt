package ru.gb.notes.domain

interface Repo {
    // CRUD
    // ------
    // Create
    // Read
    // Update
    // Delete
    fun create(note: Note?): Int
    fun read(id: Int): Note?
    fun update(note: Note?)
    fun delete(id: Int)
    fun writePref(notes: List<Note?>?)
    fun readPref(notesString: String?): List<Note?>?
    val all: List<Note?>?
    fun readCounter(notes: List<Note?>?): Int
}