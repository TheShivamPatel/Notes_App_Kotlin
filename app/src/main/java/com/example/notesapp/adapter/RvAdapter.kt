package com.example.notesapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.R
import com.example.notesapp.fragments.NotesFragmentDirections
import com.example.notesapp.model.Note
import com.example.notesapp.utils.hidekeyboard

class RvAdapter : ListAdapter<Note , RvAdapter.viewHolder>(DiffUtilCallBack()) {


    inner class viewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val sample_title : TextView = itemView.findViewById(R.id.sample_title)
        val sample_content : TextView = itemView.findViewById(R.id.sample_content)
        val sample_date : TextView = itemView.findViewById(R.id.sample_date)
        val cardView : CardView = itemView.findViewById(R.id.sample_layout_card)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        return viewHolder(LayoutInflater.from(parent.context).inflate(R.layout.sample_layout , parent , false))
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        getItem(position).let { note ->
            holder.apply {
                cardView.transitionName="recyclerView_${note.id}"
                sample_title.text = note.title
                sample_content.text = note.content
                sample_date.text = note.date

                cardView.setOnClickListener {
                    val action = NotesFragmentDirections.actionNotesFragmentToCreateOrEditFragment().setNote(note)
                    val extras = FragmentNavigatorExtras(cardView to "recyclerView_${note.id}" )
                    it.hidekeyboard()
                    Navigation.findNavController(it).navigate(action,extras)
                }
            }
        }
    }

}