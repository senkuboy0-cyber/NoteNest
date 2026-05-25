package com.dev.notenest.data.model

import com.dev.notenest.data.local.entity.NoteEntity

data class Note(
    val id: Int = 0,
    val title: String,
    val description: String,
    val timestamp: Long = System.currentTimeMillis(),
    val priority: Int = 0,
    val isPinned: Boolean = false,
    val isCompleted: Boolean = false,
    val color: Int = 0
) {
    fun toNoteEntity(): NoteEntity {
        return NoteEntity(
            id = id,
            title = title,
            description = description,
            timestamp = timestamp,
            priority = priority,
            isPinned = isPinned,
            isCompleted = isCompleted,
            color = color
        )
    }
}