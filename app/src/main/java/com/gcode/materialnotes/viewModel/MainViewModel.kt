package com.gcode.materialnotes.viewModel

import androidx.lifecycle.*
import com.gcode.materialnotes.db.Note
import com.gcode.materialnotes.db.NoteDatabase
import com.gcode.materialnotes.repository.NoteRepository
import kotlinx.coroutines.launch


// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/7/1
// Description: 
// Documentation:

class MainViewModel(val repository: NoteRepository):ViewModel() {

    private val noteDao = NoteDatabase.getDatabase().NoteDao()

    val notes:LiveData<List<Note>> = repository.notes.asLiveData()

    fun updateNote(note: Note){
        viewModelScope.launch {
            repository.updateNote(note)
        }
    }

    fun insertNote(note: Note){
        viewModelScope.launch {
            repository.insertNote(note)
        }
    }

    fun deleteNote(note: Note){
        viewModelScope.launch {
            noteDao.deleteNote(note)
        }
    }

    fun deleteAllNote(){
        viewModelScope.launch {
            repository.deleteAllNote()
        }
    }

}