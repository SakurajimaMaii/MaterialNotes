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

package com.gcode.materialnotes.ui.adapter

import android.content.Context
import android.text.TextUtils
import android.widget.Filter
import android.widget.Filterable
import com.gcode.materialnotes.BR
import com.gcode.materialnotes.model.Jotting
import com.gcode.vastadapter.base.VastBindAdapter
import com.gcode.vastadapter.interfaces.VastBindAdapterItem

class NoteAdapter(context: Context, private val notes: MutableList<VastBindAdapterItem>) :
    VastBindAdapter(notes,context), Filterable
{
    private val originNotes //用来备份原始数据
            : MutableList<VastBindAdapterItem> = notes

    private var mFilter: MyFilter? = null


    fun getItem(position: Int): Jotting {
        return dataSource[position] as Jotting
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun setVariableId(): Int {
        return BR.note
    }

    override fun getFilter(): Filter {
        //筛选器，搜索用到
        if (mFilter == null) {
            mFilter = MyFilter()
        }
        return mFilter!!
    }

    internal inner class MyFilter : Filter() {
        //我们在performFiltering(CharSequence charSequence)这个方法中定义过滤规则
        override fun performFiltering(charSequence: CharSequence): FilterResults {
            val result = FilterResults()
            val list: MutableList<VastBindAdapterItem>
            if (TextUtils.isEmpty(charSequence)) { //当过滤的关键字为空的时候，我们则显示所有的数据
                list = originNotes
            } else { //否则把符合条件的数据对象添加到集合中
                list = ArrayList()
                for (note in originNotes) {
                    if ((note as Jotting).content!!.contains(charSequence)) {
                        list.add(note)
                    }
                }
            }
            result.values = list //将得到的集合保存到FilterResults的value变量中
            result.count = list.size //将集合的大小保存到FilterResults的count变量中
            return result
        }

        //让适配器更新界面
        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            notes.clear()
            notes.addAll(results.values as MutableList<VastBindAdapterItem>)
            if (results.count > 0) {
                notifyDataSetChanged() //通知数据发生了改变
            }
        }
    }
}