package com.dev.notenest.ui.addnote

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dev.notenest.data.local.database.NoteDatabase
import com.dev.notenest.data.local.entity.NoteEntity
import com.dev.notenest.data.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddNoteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NoteRepository

    private val _saveResult = MutableLiveData<Boolean>()
    val saveResult: LiveData<Boolean> = _saveResult

    private val _note = MutableLiveData<NoteEntity?>()
    val note: LiveData<NoteEntity?> = _note

    init {
        val noteDao = NoteDatabase.getDatabase(application).noteDao()
        repository = NoteRepository(noteDao)
    }

    fun loadNote(noteId: Int) = viewModelScope.launch(Dispatchers.IO) {
        val noteEntity = repository.getNoteById(noteId)
        _note.postValue(noteEntity)
    }

    fun saveNote(title: String, description: String, priority: Int, isPinned: Boolean = false, noteId: Int = -1) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (noteId == -1) {
                    // Insert new note
                    val newNote = NoteEntity(
                        title = title,
                        description = description,
                        priority = priority,
                        isPinned = isPinned
                    )
                    repository.insert(newNote)
                } else {
                    // Update existing note
                    val existingNote = repository.getNoteById(noteId)
                    existingNote?.let {
                        val updatedNote = it.copy(
                            title = title,
                            description = description,
                            priority = priority,
                            isPinned = isPinned
                        )
                        repository.update(updatedNote)
                    }
                }
                _saveResult.postValue(true)
            } catch (e: Exception) {
                _saveResult.postValue(false)
            }
        }
    }

    fun deleteNote(noteId: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteById(noteId)
    }
}