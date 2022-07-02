package com.gcode.materialnotes.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gcode.materialnotes.R
import com.gcode.materialnotes.databinding.ActivitySettingBinding
import com.gcode.vasttools.activity.VastVbActivity

class SettingActivity : VastVbActivity<ActivitySettingBinding>() {

    override fun initView(savedInstanceState: Bundle?) {

        // 定义标签栏
        setSupportActionBar(mBinding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        mBinding.toolbar.setNavigationOnClickListener {
            finish()
        }

    }

}