package com.example.hometask2

import androidx.room.*

/**
 * Author: Dzhaparov Bekmamat
 */
@Dao
interface NoteDao {
    @Query("SELECT * FROM Note")
    fun getAllNote(): List<Note>

    @Insert
    fun addNote(model: Note)

    @Delete
    fun deleteNote(model: Note)

    @Update
    fun updateNote(model: Note)
}