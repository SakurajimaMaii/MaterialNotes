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

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import com.gcode.materialnotes.R
import com.gcode.materialnotes.databinding.ActivityAboutBinding
import com.gcode.vasttools.activity.VastVbActivity
import com.gcode.vasttools.utils.AppUtils
import com.gcode.vasttools.utils.IntentUtils
import com.gcode.vasttools.utils.ResUtils


// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/7/2
// Description: 
// Documentation:

class AboutActivity : VastVbActivity<ActivityAboutBinding>() {

    override fun initView(savedInstanceState: Bundle?) {

        // 定义标签栏
        setSupportActionBar(mBinding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        mBinding.toolbar.setNavigationOnClickListener {
            finish()
        }

        mBinding.appIcon.apply {
            setCompoundDrawablesRelativeWithIntrinsicBounds(
                BitmapDrawable(
                    resources,
                    AppUtils.getAppBitmap()
                ), null, null, null
            )
            text = AppUtils.getAppName()
        }

        mBinding.appVersion.text = String.format(ResUtils.getString(R.string.zh_app_version),AppUtils.getVersionName())

        mBinding.appGithub.setOnClickListener {
            IntentUtils.openWebPage(mContext, "https://github.com/SakurajimaMaii/MaterialNotes")
        }

        mBinding.authorGithub.setOnClickListener {
            IntentUtils.openWebPage(mContext, "https://github.com/SakurajimaMaii")
        }

        mBinding.authorCSDN.setOnClickListener {
            IntentUtils.openWebPage(mContext, "https://blog.csdn.net/weixin_43699716?type=blog")
        }

        mBinding.authorTwitter.setOnClickListener {
            IntentUtils.openWebPage(mContext, "https://twitter.com/Sakuraj61377782")
        }

    }

}