package com.dev.notenest.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val timestamp: Long = System.currentTimeMillis(),
    val priority: Int = 0,
    val isPinned: Boolean = false,
    val isCompleted: Boolean = false,
    val color: Int = 0
)
