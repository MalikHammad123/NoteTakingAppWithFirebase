package com.example.noteapp.model

import java.io.Serializable

data class Note(
    var id: String = "",
    val content: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val userId: String = ""
) : Serializable
