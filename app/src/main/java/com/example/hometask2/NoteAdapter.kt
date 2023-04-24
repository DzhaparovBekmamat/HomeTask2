package com.example.hometask2

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.hometask2.databinding.NoteListBinding

class NoteAdapter(private val listener: MainFragment) :
    RecyclerView.Adapter<NoteAdapter.ViewHolder>() {
    private val list: MutableList<Note> = ArrayList()
    private lateinit var binding: NoteListBinding
    fun getContext(position: Int, note: Note) {
        list[position] = note
        notifyItemChanged(position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addNote(note: Note) {
        list.add(note)
        notifyDataSetChanged()
    }

    fun delete(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    fun change(position: Int, note: Note) {
        list[position] = note
        notifyItemChanged(position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun sortByDate() {
        this.list.sortBy { note -> note.date }
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun sortByTitle() {
        this.list.sortBy { note -> note.title }
        notifyDataSetChanged()
    }

    interface IOnItem {
        fun delete(position: Int)
        fun share(position: Int)
        fun edit(position: Int, note: Note)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = NoteListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: MutableList<Note>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolder(binding: NoteListBinding) : RecyclerView.ViewHolder(binding.root) {
        private val imageView: ImageView = binding.imageViewNoteList
        private val title: TextView = binding.titleNoteList
        private val description: TextView = binding.descriptionNoteList
        private val date: TextView = binding.dateNoteList
        private val delete: Button = binding.buttonRemove
        private val editButton: Button = binding.buttonEdit
        private val share: Button = binding.buttonShare
        fun bind(position: Int) {
            title.text = list[position].title
            description.text = list[position].description
            date.text = list[position].date
            Glide.with(itemView).load(list[position].photoResource)
                .transform(CenterCrop(), RoundedCorners(25)).into(imageView)
            delete.setOnClickListener {
                listener.delete(adapterPosition)
            }
            editButton.setOnClickListener {
                listener.edit(position, list[position])
            }
            share.setOnClickListener {
                listener.share(adapterPosition)
            }
        }
    }
}

