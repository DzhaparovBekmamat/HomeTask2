package com.example.hometask2

import android.app.Application

class App : Application() {

    companion object {
        lateinit var db: NoteDataBase
    }

    override fun onCreate() {
        super.onCreate()
        db = NoteDataBase(this)
    }
}