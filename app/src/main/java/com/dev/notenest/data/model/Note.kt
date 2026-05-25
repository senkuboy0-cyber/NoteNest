package com.dev.notenest.data.model

data class Note(
    val id: Int = 0,
    val title: String,
    val description: String,
    val timestamp: Long = System.currentTimeMillis(),
    val priority: Int = 0,
    val isPinned: Boolean = false,
    val isCompleted: Boolean = false,
    val color: Int = 0
)
