package com.example.noteapp.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.noteapp.databinding.ActivityCreateTaskBinding
import com.example.noteapp.model.Note
import java.util.Calendar
import java.util.UUID

/**
 * @author :Malik Hammad
 * Created: 23/11/2024
 */
class CreateTaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateTaskBinding
    private var existingNote: Note? = null
    private var selectedDate = ""
    private var startTime = ""
    private var endTime = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            existingNote = intent.getSerializableExtra("editNote") as? Note
            existingNote?.let {
                etTaskName.setText(it.content.split(":")[0].trim())
                etDescription.setText(it.description)
                tvDate.text = it.startDate
                tvStartTime.text = it.startTime
                tvEndTime.text = it.endTime
            }

            tvDate.setOnClickListener { showDatePicker() }
            tvStartTime.setOnClickListener { showTimePicker { time ->
                startTime = time
                tvStartTime.text = startTime
            } }
            tvEndTime.setOnClickListener { showTimePicker { time ->
                endTime = time
                tvEndTime.text = endTime
            } }

            btnCreateTask.setOnClickListener {
                val taskName = etTaskName.text.toString()
                val description = etDescription.text.toString()
                val timestamp = System.currentTimeMillis()

                if (taskName.isEmpty()) {
                    Toast.makeText(
                        this@CreateTaskActivity,
                        "Task name cannot be empty",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                val note = existingNote?.copy(
                    content = taskName,
                    description = description,
                    timestamp = timestamp,
                    startDate = selectedDate,
                    startTime = startTime,
                    endTime = endTime
                ) ?: Note(
                    id = UUID.randomUUID().toString(),
                    content = taskName,
                    description = description,
                    timestamp = timestamp,
                    startDate = selectedDate,
                    startTime = startTime,
                    endTime = endTime
                )

                val intent = Intent()
                intent.putExtra("taskNote", note)
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                selectedDate = "$dayOfMonth/${month + 1}/$year"
                binding.tvDate.text = selectedDate
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showTimePicker(onTimeSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                onTimeSelected(String.format("%02d:%02d", hourOfDay, minute))
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }
}
