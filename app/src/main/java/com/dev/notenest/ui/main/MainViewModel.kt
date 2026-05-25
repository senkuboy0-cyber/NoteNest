package com.dev.notenest.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.dev.notenest.data.local.database.NoteDatabase
import com.dev.notenest.data.local.entity.NoteEntity
import com.dev.notenest.data.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NoteRepository

    private val _sortType = MutableLiveData(SortType.DATE)
    val sortType: LiveData<SortType> = _sortType

    private val _searchQuery = MutableLiveData("")
    val searchQuery: LiveData<String> = _searchQuery

    val notes: LiveData<List<NoteEntity>>

    init {
        val noteDao = NoteDatabase.getDatabase(application).noteDao()
        repository = NoteRepository(noteDao)

        notes = _searchQuery.switchMap { query ->
            if (query.isNullOrBlank()) {
                when (_sortType.value) {
                    SortType.PRIORITY -> repository.getNotesSortedByPriority()
                    SortType.ALPHABETICAL -> repository.getNotesSortedAlphabetically()
                    else -> repository.allNotes
                }
            } else {
                repository.searchNotes(query)
            }
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSortType(type: SortType) {
        _sortType.value = type
    }

    fun deleteNote(note: NoteEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(note)
    }

    fun togglePin(note: NoteEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(note.copy(isPinned = !note.isPinned))
    }

    fun toggleComplete(note: NoteEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(note.copy(isCompleted = !note.isCompleted))
    }

    enum class SortType {
        DATE, PRIORITY, ALPHABETICAL
    }
}
