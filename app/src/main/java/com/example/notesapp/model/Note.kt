package com.example.notesapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Notes")
data class Note(

    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val title : String,
    val content : String,
    val date : String

) : Serializable
