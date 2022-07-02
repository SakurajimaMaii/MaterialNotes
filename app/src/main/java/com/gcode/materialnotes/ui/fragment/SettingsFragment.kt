package com.gcode.materialnotes.ui.fragment

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.gcode.materialnotes.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        requireActivity().theme.applyStyle(R.style.MaterialNotes_PreferenceScreen,false)
        setPreferencesFromResource(R.xml.preferences_setting, rootKey)
    }

}