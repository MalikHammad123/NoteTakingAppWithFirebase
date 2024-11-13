package com.example.noteapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

data class Note(
    val id: String = "",
    val content: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val userId: String = ""  // New field to store the author's user ID
)


class MainActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var notesAdapter: NotesAdapter
    private lateinit var notesListener: ListenerRegistration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // RecyclerView setup
        val recyclerView = findViewById<RecyclerView>(R.id.notesRecyclerView)
        notesAdapter = NotesAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = notesAdapter

        // Add Note button
        findViewById<Button>(R.id.addNoteButton).setOnClickListener {
            val noteContent = findViewById<EditText>(R.id.noteEditText).text.toString()
            addNoteToFirestore(noteContent)
        }

        // Load notes from Firestore
        loadNotes()
    }

    private fun addNoteToFirestore(content: String) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            Toast.makeText(this, "User not authenticated.", Toast.LENGTH_SHORT).show()
            return
        }

        val newNote = hashMapOf(
            "content" to content,
            "timestamp" to System.currentTimeMillis(),
            "userId" to currentUser.uid  // Save the note with the user's ID
        )

        db.collection("notes")
            .add(newNote)
            .addOnSuccessListener {
                Toast.makeText(this, "Note added!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error adding note.", Toast.LENGTH_SHORT).show()
            }
    }


    private fun loadNotes() {
        notesListener = db.collection("notes")
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, e ->
                if (e != null || snapshot == null) return@addSnapshotListener

                val notesList = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Note::class.java)?.copy(id = doc.id)
                }
                notesAdapter.submitList(notesList)
            }
    }


    override fun onDestroy() {
        super.onDestroy()
        notesListener.remove()
    }
}
