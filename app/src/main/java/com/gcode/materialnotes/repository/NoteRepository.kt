package com.gcode.materialnotes.repository

import android.os.AsyncTask
import com.gcode.materialnotes.db.Note
import com.gcode.materialnotes.db.NoteDao


// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/7/1
// Description: 
// Documentation:

class NoteRepository(private val noteDao: NoteDao) {

    val notes = noteDao.getAllNote()

    suspend fun updateNote(note: Note) = noteDao.updateNote(note)

    suspend fun insertNote(note: Note) = noteDao.insertNote(note)

    suspend fun deleteAllNote() = noteDao.deleteAllNote()

}