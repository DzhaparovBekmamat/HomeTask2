package com.example.hometask2

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.hometask2.databinding.FragmentMainBinding

@Suppress("DEPRECATION")
class MainFragment : Fragment(), NoteAdapter.IOnItem {
    private lateinit var binding: FragmentMainBinding
    private val noteAdapter by lazy { NoteAdapter(this) }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = noteAdapter
        noteAdapter.addNote(App.db.getDao().getAllNote() as ArrayList<Note>)
        binding.buttonSort.setOnClickListener {
            val alertDialog = AlertDialog.Builder(requireContext(), R.style.MyAlertDialogStyle)
            alertDialog.setTitle("Эскертүү !")
            alertDialog.setMessage("Алардын бирин тандан !")
            alertDialog.setPositiveButton("Аталышы") { _, _ ->
                noteAdapter.sortByTitle()
            }
            alertDialog.setNegativeButton(
                "Датасы"
            ) { _, _ ->
                noteAdapter.sortByDate()
            }
            alertDialog.show()
        }
        binding.buttonAdd.setOnClickListener {
            findNavController().navigate(R.id.addFragment)
        }
        checkItem()
    }

    private fun checkItem() {
        val itemCount = noteAdapter.itemCount
        if (itemCount == 0) {
            binding.nothingFound.visibility = View.VISIBLE
        } else {
            binding.nothingFound.visibility = View.GONE
        }
    }

    override fun delete(position: Int) {
        val alertDialog = AlertDialog.Builder(requireContext(), R.style.MyAlertDialogStyle)
        alertDialog.setTitle("Эскертүү!")
        alertDialog.setMessage("Чын эле жок кылгыңыз келеби?")
        alertDialog.setPositiveButton("Өчүрүү") { _, _ ->
            noteAdapter.delete(position)
            checkItem()
        }
        alertDialog.setNegativeButton("Токтотуу", null)
        alertDialog.show()
    }

    override fun share(position: Int) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/png"
        val note = noteAdapter.getList()[position]
        val noteText = note.title + "\n" + note.description + "\n" + note.date
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(note.photoResource))
        intent.putExtra(Intent.EXTRA_TEXT, noteText)
        startActivity(Intent.createChooser(intent, "Эскертүү менен бөлүшүү"))
    }

    @SuppressLint("CommitTransaction")
    override fun edit(position: Int, note: Note) {
        val bundle = Bundle()
        bundle.putSerializable("editNote", note)
        bundle.putInt("position", position)
        findNavController().navigate(R.id.addFragment, bundle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }
}