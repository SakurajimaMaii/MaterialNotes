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