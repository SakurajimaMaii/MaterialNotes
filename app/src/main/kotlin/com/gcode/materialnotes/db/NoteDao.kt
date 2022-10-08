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

package com.gcode.materialnotes.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/7/1
// Description: 
// Documentation:
@Dao
interface NoteDao {

    /**
     * 向数据库插入一条笔记
     */
    @Insert
    suspend fun insertNote(note: Note):Long

    /**
     * 更新数据库中的笔记
     */
    @Update
    suspend fun updateNote(vararg note: Note)

    /**
     * 删除笔记
     */
    @Delete
    fun deleteNote(note: Note)

    /**
     * 删除所有笔记
     */
    @Query("delete from Notes")
    suspend fun deleteAllNote()

    /**
     * 根据[id]查询笔记
     */
    @Query("select * from Notes where id = :id")
    fun searchNoteById(id:Long): Note

    /**
     * 获取所有笔记
     */
    @Query("select * from Notes")
    fun getAllNote(): Flow<List<Note>>

}