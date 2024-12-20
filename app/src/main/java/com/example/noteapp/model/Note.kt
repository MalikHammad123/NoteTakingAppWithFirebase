package com.example.noteapp.model

import java.io.Serializable

data class Note(
    var id: String = "",
    val content: String = "",
    val description: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val startDate: String = "",
    val startTime: String = "",
    val endTime: String = ""
) : Serializable

