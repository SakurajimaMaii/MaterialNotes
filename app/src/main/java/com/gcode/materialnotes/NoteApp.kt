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