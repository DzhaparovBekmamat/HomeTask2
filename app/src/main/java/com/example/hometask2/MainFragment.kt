package com.example.hometask2

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.hometask2.databinding.FragmentMainBinding

@Suppress("DEPRECATION")
class MainFragment : Fragment(), NoteAdapter.IOnItem {
    private lateinit var recyclerView: RecyclerView
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var add: Button
    private lateinit var sort: Button
    private lateinit var textView: TextView
    private lateinit var editText: EditText
    private lateinit var binding: FragmentMainBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textView = binding.nothingFound
        recyclerView = binding.recyclerView
        add = binding.buttonAdd
        sort = binding.buttonSort
        noteAdapter = NoteAdapter(this)
        editText = binding.searchEditText
        binding.recyclerView.adapter = noteAdapter
        noteAdapter.setList((requireActivity() as MainActivity).list)
        requireActivity().supportFragmentManager.setFragmentResultListener(
            "note", this
        ) { _, result ->
            val note = result.getSerializable("model") as Note
            noteAdapter.addNote(note)
            checkItem()
        }
        requireActivity().supportFragmentManager.setFragmentResultListener(
            "editNote", this
        ) { _, result ->
            val note = result.getSerializable("edit") as Note
            val pos = result.getInt("pos")
            noteAdapter.change(pos, note)
            checkItem()
        }
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
            textView.visibility = View.VISIBLE
        } else {
            textView.visibility = View.GONE
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
        intent.type = "text/plain"
        val note = noteAdapter.getList()[position]
        val noteText = note.title + " " + note.description + " " + note.date
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