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

package com.gcode.materialnotes.model

import cn.govast.vastadapter.interfaces.VAapClickEventListener
import cn.govast.vastadapter.interfaces.VAdpLongClickEventListener
import cn.govast.vastadapter.interfaces.VastBindAdapterItem
import com.gcode.materialnotes.R

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/7/1
// Description: 
// Documentation:

/**
 * 笔记Model
 *
 * @property content 笔记内容
 * @property time 笔记时间
 * @property tag 笔记标签
 * @property id 数据库编号
 * @property vbAapClickEventListener 点击事件
 * @property vbAdpLongClickEventListener 长点击事件
 */
class Jotting @JvmOverloads constructor(
    var content: String = "内容",
    var time: String = "时间",
    var tag: String = "默认",
    var id:Long = 0,
    override var vbAapClickEventListener: VAapClickEventListener? = null,
    override var vbAdpLongClickEventListener: VAdpLongClickEventListener? = null
): VastBindAdapterItem,java.io.Serializable {

    override fun getVBAdpItemType(): Int {
        return R.layout.recycler_note
    }

    override fun toString(): String {
        return "$content${time.substring(5, 16)} $id"
    }

}
