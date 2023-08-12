package com.example.notesapp.repository

import androidx.lifecycle.LiveData
import androidx.room.Query
import com.example.notesapp.db.NoteDao
import com.example.notesapp.db.NotesDatabase
import com.example.notesapp.model.Note

class NotesRepository(private val db: NotesDatabase) {

    fun getNotes() = db.getNotesDao().getAllNotes()

    fun searchNotes(query: String) = db.getNotesDao().searchNote(query)

    suspend fun updateNote(note: Note) = db.getNotesDao().updateNote(note)

    suspend fun deleteNote(note: Note) = db.getNotesDao().deleteNote(note)

    suspend fun addNote(note: Note) = db.getNotesDao().addNote(note)





}