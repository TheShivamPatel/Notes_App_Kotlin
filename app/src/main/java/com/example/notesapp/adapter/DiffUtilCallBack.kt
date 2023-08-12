package com.example.notesapp.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.notesapp.model.Note

class DiffUtilCallBack : DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem == newItem
    }
}