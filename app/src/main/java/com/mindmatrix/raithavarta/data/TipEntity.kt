package com.mindmatrix.raithavarta.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tips")
data class TipEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: String, // Paddy, Areca nut, Coconut, Tomato
    val instruction: String, // 2-sentence instruction
    val tipImage: String, // Resource name or URL
    val isSuccessStory: Boolean = false,
    val farmerName: String? = null,
    val kannadaInstruction: String
)
