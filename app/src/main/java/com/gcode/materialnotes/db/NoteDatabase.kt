package com.gcode.materialnotes.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gcode.vasttools.helper.ContextHelper


// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/7/1
// Description: 
// Documentation:

@Database(entities = [Note::class],version = 1)
abstract class NoteDatabase:RoomDatabase() {

    abstract fun NoteDao(): NoteDao

    companion object{
        private var instance: NoteDatabase? = null

        @Synchronized
        fun getDatabase(): NoteDatabase {
            instance?.let {
                return it
            }
            return Room.databaseBuilder(ContextHelper.getAppContext(),
            NoteDatabase::class.java,"notes").build().apply {
                instance = this
            }
        }
    }

}