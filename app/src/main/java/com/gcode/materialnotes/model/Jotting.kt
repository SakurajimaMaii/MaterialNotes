package com.gcode.materialnotes.model

import com.gcode.materialnotes.R
import com.gcode.vastadapter.interfaces.VAapClickEventListener
import com.gcode.vastadapter.interfaces.VAdpLongClickEventListener
import com.gcode.vastadapter.interfaces.VastBindAdapterItem

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
):VastBindAdapterItem,java.io.Serializable {

    override fun getVBAdpItemType(): Int {
        return R.layout.recycler_note
    }

    override fun toString(): String {
        return "$content${time.substring(5, 16)} $id"
    }

}
