package com.gcode.materialnotes

import android.app.Application
import com.gcode.vasttools.helper.ContextHelper
import com.gcode.materialnotes.db.NoteDatabase
import com.gcode.materialnotes.repository.NoteRepository


// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/7/1
// Description: 
// Documentation:

class NoteApp:Application() {

    val repository by lazy { NoteRepository(NoteDatabase.getDatabase().NoteDao()) }

    override fun onCreate() {
        super.onCreate()
        ContextHelper.init(this)
    }

}