package com.gcode.materialnotes.ui.activity

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import com.gcode.materialnotes.*
import com.gcode.vastadapter.base.VastBindAdapter
import com.gcode.vastadapter.interfaces.VastBindAdapterItem
import com.gcode.vasttools.activity.VastVbActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.gcode.materialnotes.databinding.ActivityMainBinding
import com.gcode.materialnotes.db.Note
import com.gcode.materialnotes.db.NoteDao
import com.gcode.materialnotes.db.NoteDatabase
import com.gcode.materialnotes.model.Jotting
import com.gcode.materialnotes.ui.adapter.NoteAdapter
import com.gcode.materialnotes.viewModel.MainViewModel
import com.gcode.materialnotes.viewModel.MainViewModelFactory

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

    override fun initBeforeOnCreate() {
        super.initBeforeOnCreate()
        mSplashScreen = installSplashScreen()
    }

    override fun initView(savedInstanceState: Bundle?) {

        noteDao = NoteDatabase.getDatabase().NoteDao()

        //设置状态栏
        setSupportActionBar(mBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mBinding.toolbar.apply {
            setNavigationIcon(R.drawable.ic_menu)
            setNavigationOnClickListener {
                mBinding.drawerLayout.open()
            }
        }

        adapter = NoteAdapter(mContext, noteList)
        adapter.setOnItemClickListener(object : VastBindAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val curNote = adapter.getItem(position)
                val intent = Intent(mContext, EditActivity::class.java).apply {
                    putExtra(EditActivity.NOTE, curNote)
                    putExtra(EditActivity.MODE, NoteMode.UPDATE)
                    putExtra(EditActivity.TAG, 1)
                }
                editNote.launch(intent)
            }
        })
        adapter.setOnItemLongClickListener(object : VastBindAdapter.OnItemLongClickListener {
            override fun onItemLongClick(view: View, position: Int): Boolean {
                val jotting = noteList[position] as Jotting
                MaterialAlertDialogBuilder(mContext)
                    .setMessage("确认要删除吗？")
                    .setPositiveButton("是") { _: DialogInterface?, _: Int ->
                        val note = Note(jotting.content, jotting.time, jotting.tag)
                        noteDao.deleteNote(note)
                    }.setNegativeButton("否") { dialog: DialogInterface, _: Int -> dialog.dismiss() }
                    .create().show()
                return true
            }
        })

        mBinding.noteList.adapter = adapter
        mBinding.noteList.layoutManager = LinearLayoutManager(mContext)

        // 创建新笔记
        mBinding.addNote.setOnClickListener {
            val intent = Intent(mContext, EditActivity::class.java)
            intent.putExtra("mode", NoteMode.CREATE)
            editNote.launch(intent)
        }

        mBinding.navigationView.apply {
            menu.apply {
                add(0, SETTING_ACTIVITY, 100, "日常").setIcon(R.drawable.ic_setting)
            }
            setNavigationItemSelectedListener {
                when (it.itemId) {
                    SETTING_ACTIVITY -> {
                        val intent = Intent(mContext, SettingActivity::class.java)
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
            R.id.delete_all -> MaterialAlertDialogBuilder(mContext)
                .setMessage("是否删除所有笔记？")
                .setPositiveButton("是") { _, _ ->
                    mViewModel.deleteAllNote()
                }
                .setNegativeButton("否") { dialog, _ -> dialog.dismiss() }.create().show()
        }
        return super.onOptionsItemSelected(item)
    }

}