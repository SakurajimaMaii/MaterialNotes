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

package com.gcode.materialnotes.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import cn.govast.vasttools.activity.VastVbActivity
import cn.govast.vasttools.utils.DateUtils
import cn.govast.vasttools.utils.FileUtils
import cn.govast.vasttools.utils.ToastUtils
import com.gcode.materialnotes.NoteMode
import com.gcode.materialnotes.R
import com.gcode.materialnotes.databinding.ActivityEditBinding
import com.gcode.materialnotes.model.Jotting
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter

class EditActivity : VastVbActivity<ActivityEditBinding>() {

    companion object{
        const val TAG = "tag"
        const val NOTE = "note"
        const val MODE = "mode"
    }

    /**
     * 初始Note的数据
     */
    private var originJotting: Jotting = Jotting()

    /**
     * 文章在数据库中的id，如果是新建笔记则id为0
     */
    private var id: Long = 0

    /**
     * [EditActivity]打开模式，默认为[NoteMode.DEFAULT]
     */
    private var openMode = NoteMode.DEFAULT
    private val tag = "默认"
    private val tagChange = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 设置页面时间
        getBinding().noteTime.text = DateUtils.getCurrentTime()

        // 定义标签栏
        setSupportActionBar(getBinding().toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //定义标签栏返回事件
        getBinding().toolbar.setNavigationOnClickListener {
            autoSetMessage()
            setResult(RESULT_OK, intent)
            finish()
        }

        // 获取EditActivity的开启方式
        openMode = intent.getSerializableExtra(MODE) as NoteMode

        // 更新笔记
        if (openMode == NoteMode.UPDATE) {
            originJotting = intent.getSerializableExtra(NOTE) as Jotting
            getBinding().noteInfo.apply {
                setText(originJotting.content)
                setSelection(originJotting.content.length)
            }
        }

        getBinding().bottomNav.navBottomSave.setOnClickListener {
            val strInfo = getBinding().noteInfo.text.toString()
            save(strInfo)
        }

        getBinding().bottomNav.navBottomShare.setOnClickListener {
            shareMsg() //分享
        }

        getBinding().bottomNav.navBottomReset.setOnClickListener {
            var fos: FileOutputStream? = null
            getBinding().noteInfo.hint = ""
            try {
                fos = openFileOutput("txt", MODE_PRIVATE)
                val text = ""
                fos.write(text.toByteArray())
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                try {
                    if (fos != null) {
                        fos.flush()
                        Toast.makeText(this@EditActivity, "清空成功！", Toast.LENGTH_SHORT).show()
                        fos.close()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_edit, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //监听 被点击元素的事件
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete -> MaterialAlertDialogBuilder(getContext())
                .setMessage("是否删除此笔记？")
                .setPositiveButton("是") { _, _ ->
                    // 创建新笔记的情况
                    if (openMode == NoteMode.CREATE) {
                        intent.putExtra(MODE, NoteMode.DEFAULT)
                        setResult(RESULT_OK, intent)
                    }
                    // 删除笔记
                    else {
                        intent.putExtra(MODE, NoteMode.REMOVE)
                        intent.putExtra(NOTE, originJotting)
                        setResult(RESULT_OK, intent)
                    }
                    finish()
                }
                .setNegativeButton("否") { dialog, _ -> dialog.dismiss() }.create().show()
        }
        return super.onOptionsItemSelected(item)
    }

    //点击返回键，传回edit_info参数
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            return true
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            autoSetMessage()
            setResult(RESULT_OK, intent)
            finish()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0,R.anim.anim_activity_scale_out)
    }

    //intent 传参到MainActivity
    private fun autoSetMessage() {
        if (openMode == NoteMode.CREATE) {
            //判断笔记是否为空，若为空，则不新增笔记
            if (getBinding().noteInfo.text.toString().isEmpty()) {
                intent.putExtra(MODE, NoteMode.DEFAULT)
            } else {
                intent.apply {
                    putExtra(
                        NOTE, Jotting(
                            getBinding().noteInfo.text.toString(),
                        DateUtils.getCurrentTime(),
                        tag, id
                    )
                    )
                    putExtra(MODE, NoteMode.CREATE)
                }
            }
        } else {
            //判断笔记是否被修改，或者标签是否更换，否则不更新笔记
            if (getBinding().noteInfo.text.toString() == originJotting.content && !tagChange) {
                intent.putExtra(MODE, NoteMode.DEFAULT)
            } else {
                intent.apply {
                    putExtra(MODE, NoteMode.UPDATE)
                    putExtra(NOTE,originJotting.apply {
                        this.content = getBinding().noteInfo.text.toString()
                        this.time = DateUtils.getCurrentTime()
                    })
                }
            }
        }
    }

    //存储标题、文本
    private fun save(text: String) {

        val saveFileName: String = if (text.length >= 9) {
            DateUtils.getCurrentTime() + text.substring(0, 9)
        } else {
            DateUtils.getCurrentTime() + text
        }

        val file = File(FileUtils.appInternalFilesDir().path,saveFileName)
        FileUtils.saveFile(file)
        FileUtils.writeFile(file,object : FileUtils.WriteEventListener {
            override fun writeEvent(fileWriter: FileWriter) {
                fileWriter.write(text)
            }
        })
    }

    //设置“分享”函数
    private fun shareMsg() {
        val text = getBinding().noteInfo.text.toString() //转化为字符串
        if (text != "") {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain" // 纯文本
            intent.putExtra(Intent.EXTRA_TEXT, text) //分享内容
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(Intent.createChooser(intent, "分享文本")) //弹框标题
        } else {
            ToastUtils.showShortMsg("没有内容")
        }
    }
}