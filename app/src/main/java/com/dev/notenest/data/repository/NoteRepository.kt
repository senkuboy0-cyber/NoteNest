package com.dev.notenest.data.repository

import androidx.lifecycle.LiveData
import com.dev.notenest.data.local.dao.NoteDao
import com.dev.notenest.data.local.entity.NoteEntity

class NoteRepository(private val noteDao: NoteDao) {

    val allNotes: LiveData<List<NoteEntity>> = noteDao.getAllNotes()

    fun searchNotes(query: String): LiveData<List<NoteEntity>> {
        return noteDao.searchNotes(query)
    }

    suspend fun getNoteById(noteId: Int): NoteEntity? {
        return noteDao.getNoteById(noteId)
    }

    suspend fun insert(note: NoteEntity) {
        noteDao.insertNote(note)
    }

    suspend fun update(note: NoteEntity) {
        noteDao.updateNote(note)
    }

    suspend fun delete(note: NoteEntity) {
        noteDao.deleteNote(note)
    }

    suspend fun deleteById(noteId: Int) {
        noteDao.deleteNoteById(noteId)
    }

    fun getNotesSortedByPriority(): LiveData<List<NoteEntity>> {
        return noteDao.getNotesSortedByPriority()
    }

    fun getNotesSortedAlphabetically(): LiveData<List<NoteEntity>> {
        return noteDao.getNotesSortedAlphabetically()
    }

    fun getCompletedNotes(): LiveData<List<NoteEntity>> {
        return noteDao.getCompletedNotes()
    }

    fun getPendingNotes(): LiveData<List<NoteEntity>> {
        return noteDao.getPendingNotes()
    }
}
