package com.example.hometask2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hometask2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val list: MutableList<Note> = ArrayList()
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
