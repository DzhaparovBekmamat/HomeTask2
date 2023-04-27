package com.example.hometask2

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Note::class], version = 1)
abstract class NoteDataBase : RoomDatabase() {
    abstract fun getDao(): NoteDao

    companion object {
        private var instance: NoteDataBase? = null
        private val lock = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(lock) {
            instance ?: buildDatabase(context).also { noteDatabase ->
                instance = noteDatabase
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context,
                NoteDataBase::class.java,
                "DB"
            ).allowMainThreadQueries().build()
    }
}