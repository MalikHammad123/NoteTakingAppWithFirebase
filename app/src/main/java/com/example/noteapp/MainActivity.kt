package com.example.noteapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteapp.activities.CreateTaskActivity
import com.example.noteapp.databinding.ActivityMainBinding
import com.example.noteapp.model.Note
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration




class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var noteAdapter: NotesAdapter
    private val notesList = mutableListOf<Note>()

    companion object {
        private const val REQUEST_CODE_CREATE_TASK = 1
        private const val REQUEST_CODE_EDIT_TASK = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance()

        // Initialize RecyclerView
        noteAdapter = NotesAdapter(notesList,
            onEditClicked = { note ->
                val intent = Intent(this, CreateTaskActivity::class.java)
                intent.putExtra("editNote", note)
                startActivityForResult(intent, REQUEST_CODE_EDIT_TASK)
            },
            onDeleteClicked = { note ->
                deleteNoteFromFirestore(note)
            }
        )
        binding.notesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = noteAdapter
        }

        // Load existing notes from Firestore
        fetchNotesFromFirestore()

        // Handle addNoteButton click
        binding.addNoteButton.setOnClickListener {
            val intent = Intent(this, CreateTaskActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_CREATE_TASK)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val updatedNote = data?.getSerializableExtra("taskNote") as? Note
            when (requestCode) {
                REQUEST_CODE_CREATE_TASK -> {
                    updatedNote?.let {
                        saveNoteToFirestore(it)
                    }
                }
                REQUEST_CODE_EDIT_TASK -> {
                    updatedNote?.let {
                        updateNoteInFirestore(it)
                    }
                }
            }
        }
    }

    private fun saveNoteToFirestore(note: Note) {
        firestore.collection("notes").add(note)
            .addOnSuccessListener {
                fetchNotesFromFirestore() // Refresh notes
            }
    }

    private fun updateNoteInFirestore(note: Note) {
        firestore.collection("notes").document(note.id!!)
            .set(note)
            .addOnSuccessListener {
                fetchNotesFromFirestore() // Refresh notes
            }
    }

    private fun deleteNoteFromFirestore(note: Note) {
        firestore.collection("notes").document(note.id!!)
            .delete()
            .addOnSuccessListener {
                fetchNotesFromFirestore() // Refresh notes
            }
    }

    private fun fetchNotesFromFirestore() {
        firestore.collection("notes")
            .get()
            .addOnSuccessListener { snapshot ->
                val updatedNotes = snapshot.documents.mapNotNull { it.toObject(Note::class.java)
                    ?.apply { id = it.id } }
                notesList.clear()
                notesList.addAll(updatedNotes)
                noteAdapter.updateNotes(updatedNotes)
            }
    }
}
