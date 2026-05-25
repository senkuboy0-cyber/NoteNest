package com.dev.notenest

import android.app.Application
import com.dev.notenest.data.local.database.NoteDatabase
import com.dev.notenest.data.repository.NoteRepository

class NoteNestApp : Application() {
    val database by lazy { NoteDatabase.getDatabase(this) }
    val repository by lazy { NoteRepository(database.noteDao()) }
}