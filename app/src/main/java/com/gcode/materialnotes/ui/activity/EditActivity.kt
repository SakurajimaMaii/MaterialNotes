package com.gcode.materialnotes.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.gcode.materialnotes.model.Jotting
import com.gcode.materialnotes.NoteMode
import com.gcode.materialnotes.R
import com.gcode.vasttools.activity.VastVbActivity
import com.gcode.vasttools.utils.DateUtils
import com.gcode.vasttools.utils.ToastUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.gcode.materialnotes.databinding.ActivityEditBinding
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

    override fun initView(savedInstanceState: Bundle?) {

        // 设置页面时间
        mBinding.noteTime.text = DateUtils.currentTime

        // 定义标签栏
        setSupportActionBar(mBinding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //定义标签栏返回事件
        mBinding.toolbar.setNavigationOnClickListener {
            autoSetMessage()
            setResult(RESULT_OK, intent)
            finish()
        }

        // 获取EditActivity的开启方式
        openMode = intent.getSerializableExtra(MODE) as NoteMode

        // 更新笔记
        if (openMode == NoteMode.UPDATE) {
            originJotting = intent.getSerializableExtra(NOTE) as Jotting
            mBinding.noteInfo.apply {
                setText(originJotting.content)
                setSelection(originJotting.content.length)
            }
        }

        //设置保存的监听事件
        mBinding.saveNote.setOnClickListener {
            val strInfo = mBinding.noteInfo.text.toString()
            save(strInfo)
        }

        //设置分享的监听事件
        mBinding.shareNote.setOnClickListener {
            shareMsg() //分享
        }

        //设置重置的监听事件
        mBinding.resetNote.setOnClickListener {
            var fos: FileOutputStream? = null
            mBinding.noteInfo.hint = ""
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
            R.id.delete -> MaterialAlertDialogBuilder(mContext)
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

    //intent 传参到MainActivity
    private fun autoSetMessage() {
        if (openMode == NoteMode.CREATE) {
            //判断笔记是否为空，若为空，则不新增笔记
            if (mBinding.noteInfo.text.toString().isEmpty()) {
                intent.putExtra(MODE, NoteMode.DEFAULT)
            } else {
                intent.apply {
                    putExtra(
                        NOTE, Jotting(
                        mBinding.noteInfo.text.toString(),
                        DateUtils.currentTime,
                        tag, id
                    )
                    )
                    putExtra(MODE, NoteMode.CREATE)
                }
            }
        } else {
            //判断笔记是否被修改，或者标签是否更换，否则不更新笔记
            if (mBinding.noteInfo.text.toString() == originJotting.content && !tagChange) {
                intent.putExtra(MODE, NoteMode.DEFAULT)
            } else {
                intent.apply {
                    putExtra(MODE, NoteMode.UPDATE)
                    putExtra(NOTE,originJotting.apply {
                        this.content = mBinding.noteInfo.text.toString()
                        this.time = DateUtils.currentTime
                    })
                }
            }
        }
    }

    //存储标题、文本
    private fun save(text: String) {

        val saveFileName: String = if (text.length >= 9) {
            DateUtils.currentTime + text.substring(0, 9)
        } else {
            DateUtils.currentTime + text
        }

        com.gcode.vasttools.utils.FileUtils.saveFile(
            com.gcode.vasttools.utils.FileUtils.appInternalFilesDir().path,
            saveFileName,
            object :com.gcode.vasttools.utils.FileUtils.WriteEventListener{
                override fun writeEvent(fileWriter: FileWriter) {
                    fileWriter.write(text)
                }
            })

    }

    //设置“分享”函数
    private fun shareMsg() {
        val text = mBinding.noteInfo.text.toString() //转化为字符串
        if (text != "") {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain" // 纯文本
            intent.putExtra(Intent.EXTRA_TEXT, text) //分享内容
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(Intent.createChooser(intent, "分享文本")) //弹框标题
        } else {
            ToastUtils.showShortMsg(mContext,"没有内容")
        }
    }
}