package com.dev.notenest

import android.app.Application
import androidx.room.Room
import com.dev.notenest.data.local.database.NoteDatabase

class NoteNestApp : Application() {
    lateinit var database: NoteDatabase

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            NoteDatabase::class.java,
            "notenest_database"
        ).build()
    }
}
