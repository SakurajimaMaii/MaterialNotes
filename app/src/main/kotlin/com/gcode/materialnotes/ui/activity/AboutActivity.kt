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
import cn.govast.vasttools.activity.VastVbActivity
import cn.govast.vasttools.utils.AppUtils
import cn.govast.vasttools.utils.IntentUtils
import cn.govast.vasttools.utils.ResUtils
import com.gcode.materialnotes.R
import com.gcode.materialnotes.databinding.ActivityAboutBinding


// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/7/2
// Description: 
// Documentation:

class AboutActivity : VastVbActivity<ActivityAboutBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 定义标签栏
        setSupportActionBar(getBinding().toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        getBinding().toolbar.setNavigationOnClickListener {
            finish()
        }

        getBinding().appIcon.apply {
            setCompoundDrawablesRelativeWithIntrinsicBounds(
                BitmapDrawable(
                    resources,
                    AppUtils.getAppBitmap()
                ), null, null, null
            )
            text = AppUtils.getAppName()
        }

        getBinding().appVersion.text = String.format(ResUtils.getString(R.string.zh_app_version),AppUtils.getVersionName())

        getBinding().appGithub.setOnClickListener {
            IntentUtils.openWebPage(getContext(), "https://github.com/SakurajimaMaii/MaterialNotes")
        }

        getBinding().authorGithub.setOnClickListener {
            IntentUtils.openWebPage(getContext(), "https://github.com/SakurajimaMaii")
        }

        getBinding().authorCSDN.setOnClickListener {
            IntentUtils.openWebPage(getContext(), "https://blog.csdn.net/weixin_43699716?type=blog")
        }

        getBinding().authorTwitter.setOnClickListener {
            IntentUtils.openWebPage(getContext(), "https://twitter.com/Sakuraj61377782")
        }

    }

}