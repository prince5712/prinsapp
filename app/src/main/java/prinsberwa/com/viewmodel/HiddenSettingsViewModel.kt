package prinsberwa.com.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class HiddenSettingsViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        private const val PREFS_NAME = "screenshot_prefs"
        private const val SCREENSHOT_KEY = "screenshot_enabled"
    }

    private val _isScreenshotEnabled = MutableLiveData<Boolean>()
    val isScreenshotEnabled: LiveData<Boolean> = _isScreenshotEnabled

    init {
        loadScreenshotPreference()
    }

    private fun loadScreenshotPreference() {
        val prefs = getApplication<Application>().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        _isScreenshotEnabled.value = prefs.getBoolean(SCREENSHOT_KEY, false)
    }

    fun setScreenshotEnabled(enabled: Boolean) {
        val prefs = getApplication<Application>().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(SCREENSHOT_KEY, enabled).apply()
        _isScreenshotEnabled.value = enabled
    }
}