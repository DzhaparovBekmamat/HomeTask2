package com.example.hometask2

import androidx.room.Entity
import java.io.Serializable
import androidx.room.PrimaryKey

/**
 * Author: Dzhaparov Bekmamat
 */
@Entity
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    var photoResource: String?,
    var title: String?,
    var description: String?,
    var date: String?
) : Serializable