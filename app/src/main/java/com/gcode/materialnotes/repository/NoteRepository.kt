/*
 *  Copyright 2022 VastGui
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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