package com.example.notesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.notesapp.databinding.ActivityMainBinding
import com.example.notesapp.db.NotesDatabase
import com.example.notesapp.repository.NotesRepository
import com.example.notesapp.viewmodel.MainViewModel
import com.example.notesapp.viewmodel.MainViewModelFactory
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        try {
            val dao = NotesDatabase.getDatabase(this)
            val repository = NotesRepository(dao)
            viewModel = ViewModelProvider(this, MainViewModelFactory(repository))[MainViewModel::class.java]
        }
        catch (e:Exception){
            Log.d("TAG" , "!!!!ERROR $e ")
        }

    }
}