package com.dev.notenest.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dev.notenest.data.local.entity.NoteEntity

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes ORDER BY isPinned DESC, timestamp DESC")
    fun getAllNotes(): LiveData<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' ORDER BY isPinned DESC, timestamp DESC")
    fun searchNotes(query: String): LiveData<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE id = :noteId")
    suspend fun getNoteById(noteId: Int): NoteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity)

    @Update
    suspend fun updateNote(note: NoteEntity)

    @Delete
    suspend fun deleteNote(note: NoteEntity)

    @Query("DELETE FROM notes WHERE id = :noteId")
    suspend fun deleteNoteById(noteId: Int)

    @Query("SELECT * FROM notes ORDER BY priority DESC, timestamp DESC")
    fun getNotesSortedByPriority(): LiveData<List<NoteEntity>>

    @Query("SELECT * FROM notes ORDER BY title ASC")
    fun getNotesSortedAlphabetically(): LiveData<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE isCompleted = 1 ORDER BY timestamp DESC")
    fun getCompletedNotes(): LiveData<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE isCompleted = 0 ORDER BY isPinned DESC, timestamp DESC")
    fun getPendingNotes(): LiveData<List<NoteEntity>>
}
