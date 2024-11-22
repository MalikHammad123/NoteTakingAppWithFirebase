package com.example.noteapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.noteapp.databinding.ActivityCreateTaskBinding
import com.example.noteapp.model.Note



class CreateTaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateTaskBinding
    private var existingNote: Note? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check if editing an existing note
        existingNote = intent.getSerializableExtra("editNote") as? Note
        existingNote?.let {
            binding.etTaskName.setText(it.content.split(":")[0].trim())
            binding.etDescription.setText(it.content.split(":")[1].trim())
        }

        binding.btnCreateTask.setOnClickListener {
            val taskName = binding.etTaskName.text.toString()
            val description = binding.etDescription.text.toString()
            val timestamp = System.currentTimeMillis()

            if (taskName.isEmpty()) {
                Toast.makeText(this, "Task name cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val note = existingNote?.copy(
                content = "$taskName: $description",
                timestamp = timestamp
            ) ?: Note(
                id = null.toString(),
                content = "$taskName: $description",
                userId = "User123",
                timestamp = timestamp
            )

            val intent = Intent()
            intent.putExtra("taskNote", note)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}
