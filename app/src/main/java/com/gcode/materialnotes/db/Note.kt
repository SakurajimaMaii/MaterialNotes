package com.gcode.materialnotes.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/7/1
// Description: 
// Documentation:

/**
 * 数据库笔记类
 */
@Entity(tableName = "Notes")
data class Note(
    @ColumnInfo(name = "note_content") var content: String?,
    @ColumnInfo(name = "note_date") var date: String?,
    @ColumnInfo(name = "note_tag") var tag: String?,
){
    @PrimaryKey(autoGenerate = true) var id:Long = 0
}