package com.example.hometask2

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class NoteAdapter(private val listener: MainFragment) :
    RecyclerView.Adapter<NoteAdapter.ViewHolder>() {
    private val list: MutableList<Note> = ArrayList()

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

    fun sortByDate() {
        this.list.sortBy { note -> note.date }
        notifyDataSetChanged()
    }

    interface IOnItem {
        fun delete(position: Int)
        fun share(position: Int)
        fun edit(position: Int, note: Note)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_list, parent, false)
        return ViewHolder(view)
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

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageView: ImageView = view.findViewById(R.id.image_view_note_list)
        private val title: TextView = view.findViewById(R.id.title_note_list)
        private val description: TextView = view.findViewById(R.id.description_note_list)
        private val date: TextView = view.findViewById(R.id.date_note_list)
        private val delete: Button = view.findViewById(R.id.button_remove)
        private val editButton: Button = view.findViewById(R.id.button_edit)
        private val share: Button = view.findViewById(R.id.button_share)
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

