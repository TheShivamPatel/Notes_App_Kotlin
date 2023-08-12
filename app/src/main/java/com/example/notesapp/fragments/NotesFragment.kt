package com.example.notesapp.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notesapp.MainActivity
import com.example.notesapp.R
import com.example.notesapp.adapter.RvAdapter
import com.example.notesapp.databinding.FragmentNotesBinding
import com.example.notesapp.utils.SwipeToDelete
import com.example.notesapp.utils.hidekeyboard
import com.example.notesapp.viewmodel.MainViewModel
import com.google.android.material.transition.MaterialElevationScale
import java.util.concurrent.TimeUnit

class NotesFragment : Fragment(R.layout.fragment_notes) {

    private lateinit var noteBinding : FragmentNotesBinding
    private lateinit var rvAdapter: RvAdapter
    private val viewModel : MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        exitTransition = MaterialElevationScale(false).apply {
            duration = 350
        }
        enterTransition= MaterialElevationScale(true).apply {
            duration = 350
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noteBinding = FragmentNotesBinding.bind(view)
        val activity = activity as MainActivity
        requireView().hidekeyboard()

        recyclerViewDisplay()

        swipeToDelete(noteBinding.recyclerView)

        noteBinding.newNoteBtn.setOnClickListener{
            findNavController().navigate(R.id.action_notesFragment_to_createOrEditFragment)
        }


        noteBinding.search.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                noteBinding.noData.isVisible = false
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (s.toString().isNotEmpty()){
                    val text = s.toString()
                    val query = "%$text%"
                    if (query.isNotEmpty()){
                        viewModel.searchNote(query).observe(viewLifecycleOwner){
                            rvAdapter.submitList(it)
                        }
                    }
                    else{
                        observeDataChanges()
                    }
                }
                else observeDataChanges()
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallBack = object : SwipeToDelete(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.absoluteAdapterPosition
                val note = rvAdapter.currentList[position]
                val actionBtnTapped = false
                viewModel.deleteNote(note)
                noteBinding.search.apply {
                    hidekeyboard()
                    clearFocus()
                }
                if(noteBinding.search.text.isEmpty()){
                    observeDataChanges()
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallBack)
        itemTouchHelper.attachToRecyclerView(recyclerView)

    }

    private fun observeDataChanges() {
        viewModel.getAllNotes().observe(viewLifecycleOwner){list->
            noteBinding.noData.isVisible = list.isEmpty()
            rvAdapter.submitList(list   )
        }
    }

    private fun recyclerViewDisplay() {

        when(resources.configuration.orientation){

            Configuration.ORIENTATION_PORTRAIT -> setUpRecyclerView(2)
            
            Configuration.ORIENTATION_LANDSCAPE-> setUpRecyclerView(3)

        }

    }

    private fun setUpRecyclerView(spanCont: Int) {

        noteBinding.recyclerView.apply {

            layoutManager = StaggeredGridLayoutManager(spanCont,StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            rvAdapter=RvAdapter()
            adapter = rvAdapter

            rvAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            postponeEnterTransition(300L, TimeUnit.MILLISECONDS)
            viewTreeObserver.addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }

        }

        observeDataChanges()

    }

}