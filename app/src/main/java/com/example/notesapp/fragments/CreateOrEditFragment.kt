package com.example.notesapp.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.notesapp.MainActivity
import com.example.notesapp.R
import com.example.notesapp.databinding.FragmentCreateOrEditBinding
import com.example.notesapp.model.Note
import com.example.notesapp.utils.hidekeyboard
import com.example.notesapp.viewmodel.MainViewModel
import com.google.android.material.transition.MaterialContainerTransform
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

class CreateOrEditFragment : Fragment(R.layout.fragment_create_or_edit) {


    private lateinit var contentBinding : FragmentCreateOrEditBinding
    private var note : Note?= null

    private val viewModel : MainViewModel by activityViewModels()
    private val currentDate = SimpleDateFormat.getDateInstance().format(Date())
    private val job = CoroutineScope(Dispatchers.Main)
    private lateinit var result: String
    private val args : CreateOrEditFragmentArgs by navArgs()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val animation = MaterialContainerTransform().apply{
            drawingViewId  = R.id.createOrEditFragment
            scrimColor = Color.TRANSPARENT
            duration=300L
        }
        sharedElementEnterTransition=animation
        sharedElementReturnTransition=animation

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contentBinding = FragmentCreateOrEditBinding.bind(view)

        val activity = activity as MainActivity


        contentBinding.backBtn.setOnClickListener {
            requireView().hidekeyboard()
            findNavController().popBackStack()
        }

        contentBinding.saveBtn.setOnClickListener {
            saveNotes()
        }

        // opens with existing note item
        setUpNote()

    }

    private fun setUpNote() {
        val note = args.note
        val title = contentBinding.titleEdt
        val content = contentBinding.contentEdt

        if(note != null){
            title.setText(note.title)
            content.setText(note.content)
            contentBinding.apply {
                job.launch {
                    delay(10)
                }
            }
        }
    }

    private fun saveNotes() {

        if (contentBinding.titleEdt.text.isEmpty() || contentBinding.contentEdt.text.isEmpty()){
            Toast.makeText(activity , "Something is Empty", Toast.LENGTH_SHORT).show()
        }
        else{
            note=args.note

            when(note){
                null -> {
                    viewModel.saveNote(
                        Note(
                            0,
                            contentBinding.titleEdt.text.toString(),
                            contentBinding.contentEdt.text.toString(),
                            currentDate
                        )
                    )

                    result = "Note Saved"
                    setFragmentResult("Key" ,
                    bundleOf("bundleKey" to result)
                    )

                    findNavController().navigate(R.id.action_createOrEditFragment_to_notesFragment)
                }
                else ->
                {
                    //update our note
                    updateNote()
                    findNavController().popBackStack()
                }
            }

        }

    }

    private fun updateNote() {
        if (note!=null){
            viewModel.updateNote(
                Note(note!!.id,
                contentBinding.titleEdt.text.toString(),
                contentBinding.contentEdt.text.toString(),
                currentDate)
            )
        }
    }

}