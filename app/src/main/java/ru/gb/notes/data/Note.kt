package ru.gb.notes.data

import java.io.Serializable

data class Note(
    var id: Int?,
    var title: String,
    var description: String,
    var interest: String,
    var dataPerformance: String?
) : Serializable