package com.example.noteapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.databinding.ItemNoteBinding
import com.example.noteapp.model.Note

class NotesAdapter(
    private val notes: MutableList<Note>,
    private val onEditClicked: (Note) -> Unit,
    private val onDeleteClicked: (Note) -> Unit
) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    class NoteViewHolder(val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.binding.tvContent.text = note.content
        holder.binding.tvTimeStart.text = note.startTime + " to " + note.endTime
        //holder.binding.tvTimeEnd.text = note.endTime + "" + note.startTime
        holder.binding.tvDate.text = note.startDate

        holder.binding.imgEdit.setOnClickListener {
            onEditClicked(note)
        }

        holder.binding.imgDelete.setOnClickListener {
            onDeleteClicked(note)
        }
    }

    override fun getItemCount(): Int = notes.size

    fun updateNotes(newNotes: List<Note>) {
        notes.clear()
        notes.addAll(newNotes)
        notifyDataSetChanged()
    }
}


