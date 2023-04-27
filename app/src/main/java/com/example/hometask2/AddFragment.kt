package com.example.hometask2

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.hometask2.databinding.FragmentAddBinding
import java.io.IOException
import java.util.*

@Suppress("DEPRECATION")
class AddFragment : Fragment() {
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var imageView: ImageView
    private var imageUrl: String? = null
    private lateinit var binding: FragmentAddBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.back
        val button: Button = binding.buttonAddFragment
        val title: EditText = binding.firstEditTextAddFragment
        val description: EditText = binding.secondEditTextAddFragment
        val date: EditText = binding.thirdEditTextAddFragmentDatePicker
        binding.thirdEditTextAddFragmentDatePicker.setOnClickListener {
            showDatePickerDialog()
        }
        imageView = binding.imageViewAddFragment
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
            binding.buttonAddFragment.setOnClickListener {
                val titleString = title.text.toString()
                val descriptionString = description.text.toString()
                val dateString = date.text.toString()
                val edit = Note(
                    photoResource = imageUrl,
                    title = titleString,
                    description = descriptionString,
                    date = dateString
                )
                (requireActivity() as MainActivity).list.add(edit)
                val pos = arguments?.getInt("position")
                if (pos != null) {
                    bundle.putInt("pos", pos)
                    requireActivity().supportFragmentManager.setFragmentResult("editNote", bundle)
                    requireActivity().supportFragmentManager.popBackStack()
                }
            }
        } else {
            binding.buttonAddFragment.setOnClickListener {
                val titleString = title.text.toString()
                val descriptionString = description.text.toString()
                val dateString = date.text.toString()
                val edit = Note(
                    photoResource = imageUrl,
                    title = titleString,
                    description = descriptionString,
                    date = dateString
                )
                App.db.getDao().addNote(edit)
                findNavController().navigateUp()
            }
        }
        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            requireContext(), { _, yearSelected, monthSelected, dayOfMonth ->
                val selectedDate = "$dayOfMonth/${monthSelected + 1}/$yearSelected"
                val date = binding.thirdEditTextAddFragmentDatePicker
                date.setText(selectedDate)
            }, year, month, day
        )
        datePickerDialog.show()
    }

    @SuppressLint("IntentReset")
    private fun pickImageFromGallery() {
        binding.imageViewAddFragment.setOnClickListener {
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
