package com.example.hometask2

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import java.io.IOException

@Suppress("DEPRECATION")
class AddFragment : Fragment() {
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var imageView: ImageView
    private var imageUrl: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val back: Button = view.findViewById(R.id.back)
        val button: Button = view.findViewById(R.id.button_add_fragment)
        val title: EditText = view.findViewById(R.id.first_edit_text_add_fragment)
        val description: EditText = view.findViewById(R.id.second_edit_text_add_fragment)
        val date: EditText = view.findViewById(R.id.third_edit_text_add_fragment)
        imageView = view.findViewById(R.id.image_view_add_fragment)
        pickImageFromGallery()
        val bundle = Bundle()
        if (arguments != null && arguments?.getSerializable("searchText") != null) {
            val text = arguments?.getString("searchText")
            title.setText(text)
            button.text = "Түзөтүү"
        }
        if (arguments != null && arguments?.getSerializable("editNote") != null) {
            button.text = "Edit"
            val note = arguments?.getSerializable("editNote") as Note
            title.setText(note.title)
            description.setText(note.description)
            date.setText(note.date)
            button.setOnClickListener {
                val titleString = title.text.toString()
                val descriptionString = description.text.toString()
                val dateString = date.text.toString()
                val edit = Note(imageUrl, titleString, descriptionString, dateString)
                (requireActivity() as MainActivity).list.add(note)
                bundle.putSerializable("edit", edit)
                val pos = arguments?.getInt("position")
                if (pos != null) {
                    bundle.putInt("pos", pos)
                    requireActivity().supportFragmentManager.setFragmentResult("editNote", bundle)
                    requireActivity().supportFragmentManager.popBackStack()
                }
            }
        } else {
            button.setOnClickListener {
                val titleString = title.text.toString()
                val descriptionString = description.text.toString()
                val dateString = date.text.toString()
                val edit = Note(imageUrl, titleString, descriptionString, dateString)
                bundle.putSerializable("model", edit)
                requireActivity().supportFragmentManager.setFragmentResult("note", bundle)
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
        back.setOnClickListener {
            findNavController().navigate(R.id.mainFragment)
        }
    }

    @SuppressLint("IntentReset")
    private fun pickImageFromGallery() {
        imageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        imageUrl = data?.data.toString()
        imageView.setImageURI(data?.data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data?.data != null) {
            try {
                val bitmap =
                    MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, data.data)
                imageView.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
                imageUrl = data.data.toString()
            }
        }
    }
}
