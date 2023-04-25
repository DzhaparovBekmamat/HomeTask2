package com.example.hometask2

import java.io.Serializable
import androidx.room.PrimaryKey

/**
 * Author: Dzhaparov Bekmamat
 */
data class Note(
    @PrimaryKey(autoGenerate = true) var photoResource: String?,
    var title: String?,
    var description: String?,
    var date: String?
) : Serializable