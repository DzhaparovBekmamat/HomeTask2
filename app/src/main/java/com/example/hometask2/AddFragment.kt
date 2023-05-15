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
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.hometask2.databinding.FragmentAddBinding
import java.io.IOException
import java.util.*

@Suppress("DEPRECATION")
class AddFragment : Fragment() {
    private val pick_image_request = 1
    private lateinit var imageView: ImageView
    private var imageUrl: String? = null
    private lateinit var binding: FragmentAddBinding
    private var isAllFieldsChecked: Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.thirdEditTextAddFragmentDatePicker.setOnClickListener {
            showDatePickerDialog()
        }
        imageView = binding.imageViewAddFragment
        pickImageFromGallery()
        if (arguments != null && arguments?.getSerializable("searchText") != null) {
            val text = arguments?.getString("searchText")
            binding.firstEditTextAddFragment.setText(text)
            binding.buttonAddFragment.text = "Редактировать"
        }
        if (arguments != null && arguments?.getSerializable("editNote") != null) {
            binding.buttonAddFragment.text = "Редактировать"
            val note = arguments?.getSerializable("editNote") as Note
            binding.firstEditTextAddFragment.setText(note.title)
            binding.secondEditTextAddFragment.setText(note.description)
            binding.thirdEditTextAddFragmentDatePicker.setText(note.date)
            binding.buttonAddFragment.setOnClickListener {
                val titleString = binding.firstEditTextAddFragment.text.toString()
                val descriptionString = binding.secondEditTextAddFragment.text.toString()
                val dateString = binding.thirdEditTextAddFragmentDatePicker.text.toString()
                val edit = Note(
                    photoResource = imageUrl,
                    title = titleString,
                    description = descriptionString,
                    date = dateString
                )
                isAllFieldsChecked = checkAllFields()
                if (isAllFieldsChecked) {
                    App.db.getDao().addNote(edit)
                    findNavController().navigateUp()
                }
            }
        } else {
            binding.buttonAddFragment.setOnClickListener {
                val titleString = binding.firstEditTextAddFragment.text.toString()
                val descriptionString = binding.secondEditTextAddFragment.text.toString()
                val dateString = binding.thirdEditTextAddFragmentDatePicker.text.toString()
                val edit = Note(
                    photoResource = imageUrl,
                    title = titleString,
                    description = descriptionString,
                    date = dateString
                )
                isAllFieldsChecked = checkAllFields()
                if (isAllFieldsChecked) {
                    App.db.getDao().addNote(edit)
                    findNavController().navigateUp()
                }
            }
        }
        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun checkAllFields(): Boolean {
        if (binding.firstEditTextAddFragment.length() == 0) {
            binding.firstEditTextAddFragment.error = "Это поле обязательно к заполнению"
            return false
        }
        if (binding.secondEditTextAddFragment.length() == 0) {
            binding.secondEditTextAddFragment.error = "Это поле обязательно к заполнению"
            return false
        }
        if (binding.thirdEditTextAddFragmentDatePicker.length() == 0) {
            binding.thirdEditTextAddFragmentDatePicker.error = "Это поле обязательно к заполнению"
            return false
        }
        if (binding.imageViewAddFragment.drawable == null) {
            binding.error.visibility = View.VISIBLE
            return false
        }
        return true
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
            startActivityForResult(intent, pick_image_request)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        imageUrl = data?.data.toString()
        imageView.setImageURI(data?.data)
        if (requestCode == pick_image_request && resultCode == RESULT_OK && data?.data != null) {
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(
                    requireActivity().contentResolver, data.data
                )
                imageView.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
                imageUrl = data.data.toString()
            }
        }
    }
}
