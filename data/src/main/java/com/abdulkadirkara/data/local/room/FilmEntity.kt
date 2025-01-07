package com.abdulkadirkara.data.local.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "film_table")
data class FilmEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val category: String,
    val description: String,
    val director: String,
    val imagePath: String,
    val name: String,
    val price: Int,
    val rating: Double,
    val year: Int
)
