package prinsberwa.com.fragments

import android.content.SharedPreferences
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import prinsberwa.com.R
import prinsberwa.com.viewmodel.HiddenSettingsViewModel

class HiddenSettingsPreferenceFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var viewModel: HiddenSettingsViewModel
    private lateinit var screenshotPreference: SwitchPreferenceCompat

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.hidden_preferences, rootKey)

        viewModel = ViewModelProvider(requireActivity())[HiddenSettingsViewModel::class.java]

        screenshotPreference = findPreference("screenshot_enabled")!!

        // Initialize preference with value from ViewModel
        viewModel.isScreenshotEnabled.observe(this) { isEnabled ->
            // Only update UI if the value is different to avoid infinite loop
            if (screenshotPreference.isChecked != isEnabled) {
                screenshotPreference.isChecked = isEnabled
            }
        }
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == "screenshot_enabled") {
            sharedPreferences?.let {
                val isEnabled = it.getBoolean(key, false)
                viewModel.setScreenshotEnabled(isEnabled)
            }
        }
    }
}