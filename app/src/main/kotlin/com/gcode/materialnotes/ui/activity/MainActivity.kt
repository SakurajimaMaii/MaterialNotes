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

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import cn.govast.vastadapter.adapter.VastBindAdapter
import cn.govast.vastadapter.interfaces.VastBindAdapterItem
import cn.govast.vasttools.activity.VastVbActivity
import cn.govast.vasttools.utils.ToastUtils
import com.gcode.materialnotes.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.gcode.materialnotes.databinding.ActivityMainBinding
import com.gcode.materialnotes.db.Note
import com.gcode.materialnotes.db.NoteDao
import com.gcode.materialnotes.db.NoteDatabase
import com.gcode.materialnotes.model.Jotting
import com.gcode.materialnotes.ui.adapter.NoteAdapter
import com.gcode.materialnotes.viewModel.MainViewModel
import com.gcode.materialnotes.viewModel.MainViewModelFactory
import com.google.android.material.textfield.TextInputEditText

class MainActivity : VastVbActivity<ActivityMainBinding>() {

    companion object {
        private const val SETTING_ACTIVITY: Int = 100
    }

    // 笔记数据
    private val noteList: MutableList<VastBindAdapterItem> = ArrayList()
    private lateinit var adapter: NoteAdapter

    // ViewModel
    private val mViewModel: MainViewModel by viewModels {
        MainViewModelFactory((application as NoteApp).repository)
    }

    // 数据库
    private lateinit var noteDao: NoteDao

    // 更新笔记
    private val editNote =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                if (intent != null) {
                    when (intent.getSerializableExtra(EditActivity.MODE) as NoteMode) {
                        NoteMode.UPDATE -> {
                            val jotting = intent.getSerializableExtra(EditActivity.NOTE) as Jotting
                            val note = Note(jotting.content, jotting.time, jotting.tag)
                            mViewModel.updateNote(note)
                        }
                        NoteMode.CREATE -> {
                            val jotting = intent.getSerializableExtra(EditActivity.NOTE) as Jotting
                            val note = Note(jotting.content, jotting.time, jotting.tag)
                            mViewModel.insertNote(note)
                        }
                        NoteMode.REMOVE -> {
                            val jotting = intent.getSerializableExtra(EditActivity.NOTE) as Jotting
                            val note = Note(jotting.content, jotting.time, jotting.tag)
                            mViewModel.deleteNote(note)
                        }
                        else -> {}
                    }
                }
            }
        }

    // 启动页面
    private lateinit var mSplashScreen: SplashScreen

    override fun onCreate(savedInstanceState: Bundle?) {
        mSplashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        noteDao = NoteDatabase.getDatabase().NoteDao()
        //设置状态栏
        setSupportActionBar(getBinding().toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getBinding().toolbar.apply {
            setNavigationIcon(R.drawable.ic_menu)
            setNavigationOnClickListener {
                getBinding().drawerLayout.open()
            }
        }

        adapter = NoteAdapter(getContext(), noteList)
        adapter.setOnItemClickListener(object : VastBindAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val curNote = adapter.getItem(position)
                val intent = Intent(getContext(), EditActivity::class.java).apply {
                    putExtra(EditActivity.NOTE, curNote)
                    putExtra(EditActivity.MODE, NoteMode.UPDATE)
                    putExtra(EditActivity.TAG, 1)
                }
                editNote.launch(intent)
                overridePendingTransition(R.anim.anim_activity_scale_in,R.anim.anim_activity_scale_out)
            }
        })
        adapter.setOnItemLongClickListener(object : VastBindAdapter.OnItemLongClickListener {
            override fun onItemLongClick(view: View, position: Int): Boolean {
                val jotting = noteList[position] as Jotting
                MaterialAlertDialogBuilder(getContext())
                    .setMessage("确认要删除吗？")
                    .setPositiveButton("是") { _: DialogInterface?, _: Int ->
                        val note = Note(jotting.content, jotting.time, jotting.tag)
                        noteDao.deleteNote(note)
                    }.setNegativeButton("否") { dialog: DialogInterface, _: Int -> dialog.dismiss() }
                    .create().show()
                return true
            }
        })

        getBinding().noteList.adapter = adapter
        getBinding().noteList.layoutManager = LinearLayoutManager(getContext())

        // 创建新笔记
        getBinding().addNote.setOnClickListener {
            MaterialAlertDialogBuilder(getContext()).apply {
                setIcon(R.drawable.ic_app)
                setTitle("创建项目")
                setNegativeButton("取消",null)
                setItems(
                    arrayOf("笔记","文件夹"),
                    DialogInterface.OnClickListener { _, which ->
                        when(which){
                            0->{
                                val intent = Intent(context, EditActivity::class.java).apply {
                                    putExtra("mode", NoteMode.CREATE)
                                }
                                editNote.launch(intent)
                            }
                            1->{
                                setView(LayoutInflater.from(context).inflate(R.layout.preference_dialog_edittext,null))
                                setNegativeButton("取消",null )
                                setPositiveButton("确定") { _, _ ->
                                    val edit = findViewById<TextInputEditText>(android.R.id.edit)
                                    ToastUtils.showShortMsg(edit.text.toString())
                                }
                            }
                        }
                    }
                )
            }.show()

        }

        getBinding().navigationView.apply {
            setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.app_setting -> {
                        val intent = Intent(context, SettingActivity::class.java)
                        startActivity(intent)
                    }
                    R.id.app_about -> {
                        val intent = Intent(context, AboutActivity::class.java)
                        startActivity(intent)
                    }
                }
                return@setNavigationItemSelectedListener true
            }
        }


        mViewModel.notes.observe(this) {
            if (noteList.size > 0) {
                noteList.clear()
            }
            for (note: Note in it) {
                noteList.add(Jotting(note.content ?: "", note.date ?: "", note.tag ?: "", note.id))
            }
            adapter.notifyDataSetChanged()
        }

    }

    // 添加导航栏menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        // 创建“搜索”的监听事件
        val mSearch = menu.findItem(R.id.search)
        val mSearchView = mSearch.actionView as SearchView
        mSearchView.queryHint = "Search"
        mSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_all -> MaterialAlertDialogBuilder(getContext())
                .setMessage("是否删除所有笔记？")
                .setPositiveButton("是") { _, _ ->
                    mViewModel.deleteAllNote()
                }
                .setNegativeButton("否") { dialog, _ -> dialog.dismiss() }.create().show()
        }
        return super.onOptionsItemSelected(item)
    }

}