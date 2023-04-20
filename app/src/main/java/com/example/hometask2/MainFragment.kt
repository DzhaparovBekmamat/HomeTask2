package com.example.hometask2

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

@Suppress("DEPRECATION")
class MainFragment : Fragment(), NoteAdapter.IOnItem {
    private lateinit var recyclerView: RecyclerView
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var add: Button
    private lateinit var sort: Button
    private lateinit var textView: TextView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textView = view.findViewById(R.id.nothing_found)
        recyclerView = view.findViewById(R.id.recyclerView)
        add = view.findViewById(R.id.button_add)
        sort = view.findViewById(R.id.button_sort)
        noteAdapter = NoteAdapter(this)
        recyclerView.adapter = noteAdapter
        noteAdapter.setList((requireActivity() as MainActivity).list)
        requireActivity().supportFragmentManager.setFragmentResultListener(
            "note", this
        ) { _, result ->
            val note = result.getSerializable("model") as Note
            noteAdapter.addNote(note)
        }
        requireActivity().supportFragmentManager.setFragmentResultListener(
            "editNote", this
        ) { _, result ->
            val note = result.getSerializable("edit") as Note
            val pos = result.getInt("pos")
            noteAdapter.change(pos, note)
        }
        sort.setOnClickListener {
            val editText = getSearchText()
            val addFragment = AddFragment.newInstance(editText)
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.main_fragment_container, addFragment)
                .addToBackStack(null).commit()
        }
        add.setOnClickListener {
            val addFragment = AddFragment()
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.main_fragment_container, addFragment)
                .addToBackStack(null).commit()
        }
        val itemCount = recyclerView.adapter?.itemCount ?: 0
        if (itemCount == 0) {
            textView.visibility = View.VISIBLE
        } else {
            textView.visibility = View.GONE
        }
    }

    private fun getSearchText(): String {
        val searchEditText: EditText = requireView().findViewById(R.id.search_edit_text)
        return searchEditText.text.toString()
    }

    override fun delete(position: Int) {
        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setTitle("Эскертүү!")
        alertDialog.setMessage("Чын эле жок кылгыңыз келеби?")
        alertDialog.setPositiveButton("Өчүрүү") { _, _ -> noteAdapter.delete(position) }
        alertDialog.setNegativeButton("Токтотуу", null)
        alertDialog.show()
    }

    override fun share(position: Int) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        val noteText = "Эскертүүнүн тексти"
        intent.putExtra(Intent.EXTRA_TEXT, noteText)
        startActivity(Intent.createChooser(intent, "Эскертүүнү бөлүшүү"))
    }

    @SuppressLint("CommitTransaction")
    override fun edit(position: Int, note: Note) {
        val bundle = Bundle()
        bundle.putSerializable("editNote", note)
        bundle.putInt("position", position)
        val addFragment = AddFragment()
        addFragment.arguments = bundle
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_fragment_container, addFragment).addToBackStack(null).commit()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }
}