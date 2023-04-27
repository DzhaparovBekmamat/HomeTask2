package com.example.hometask2

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.hometask2.databinding.NoteListBinding

class NoteAdapter(private val listener: MainFragment) :
    RecyclerView.Adapter<NoteAdapter.ViewHolder>() {
    private var list: MutableList<Note> = ArrayList()

    @SuppressLint("NotifyDataSetChanged")
    fun addNote(list: ArrayList<Note>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun getList() = list

    fun delete(position: Int) {
        App.db.getDao().deleteNote(list.removeAt(position))
        notifyItemRemoved(position)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        NoteListBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    @SuppressLint("ClickableViewAccessibility")
    inner class ViewHolder(private val binding: NoteListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: Note) {
            binding.titleNoteList.text = model.title
            binding.descriptionNoteList.text = model.description
            binding.dateNoteList.text = model.date
            Glide.with(itemView).load(model.photoResource)
                .transform(CenterCrop(), RoundedCorners(25)).into(binding.imageViewNoteList)
            binding.buttonRemove.setOnClickListener {
                listener.delete(adapterPosition)
            }
            binding.buttonEdit.setOnClickListener {
                listener.edit(adapterPosition, model)
            }
            binding.buttonShare.setOnClickListener {
                listener.share(adapterPosition)
            }
        }
    }
}


