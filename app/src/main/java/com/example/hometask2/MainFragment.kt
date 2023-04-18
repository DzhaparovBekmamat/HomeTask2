package com.example.hometask2

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView

@Suppress("DEPRECATION")
class MainFragment : Fragment(), NoteAdapter.IOnItem {
    private lateinit var recyclerView: RecyclerView
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var add: Button
    private lateinit var sort: Button
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        add = view.findViewById(R.id.button_add)
        sort = view.findViewById(R.id.button_sort)
        recyclerView = view.findViewById(R.id.recyclerView)
        noteAdapter = NoteAdapter(this)
        recyclerView.adapter = noteAdapter
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
        add.setOnClickListener {
            val addFragment = AddFragment()
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.main_fragment_container, addFragment)
                .addToBackStack(null).commit()
        }
        sort.setOnClickListener {
            val editText = getSearchText()
            val addFragment = AddFragment.newInstance(editText)
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.main_fragment_container, addFragment)
                .addToBackStack(null).commit()
        }
    }

    private fun getSearchText(): String {
        val searchEditText: EditText = requireView().findViewById(R.id.search_edit_text)
        return searchEditText.text.toString()
    }

    override fun delete(position: Int) {
        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setTitle("Предупреждение!")
        alertDialog.setMessage("Вы уверены что хотите удалить?")
        alertDialog.setPositiveButton("УДАЛИТЬ") { _, _ -> noteAdapter.delete(position) }
        alertDialog.setNegativeButton("ОТМЕНА", null)
        alertDialog.show()
    }

    override fun share(position: Int) {
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
