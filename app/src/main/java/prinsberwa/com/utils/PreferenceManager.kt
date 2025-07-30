package prinsberwa.com.utils

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager {

    companion object {
        private const val PREF_NAME = "prinsberwa_preferences"

        // Keys
        const val KEY_LANGUAGE = "app_language"
        const val KEY_THEME = "app_theme"
        const val KEY_AUTO_UPDATES = "auto_updates"
        const val KEY_ANALYTICS_ENABLED = "analytics_enabled"
        const val KEY_USER_LOGGED_IN = "user_logged_in"
    }

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveString(context: Context, key: String, value: String) {
        getPreferences(context).edit().putString(key, value).apply()
    }

    fun getString(context: Context, key: String, defaultValue: String): String {
        return getPreferences(context).getString(key, defaultValue) ?: defaultValue
    }

    fun saveBoolean(context: Context, key: String, value: Boolean) {
        getPreferences(context).edit().putBoolean(key, value).apply()
    }

    fun getBoolean(context: Context, key: String, defaultValue: Boolean): Boolean {
        return getPreferences(context).getBoolean(key, defaultValue)
    }

    fun clearUserData(context: Context) {
        val editor = getPreferences(context).edit()
        editor.putBoolean(KEY_USER_LOGGED_IN, false)
        // Preserve theme and language settings
        editor.remove("user_token")
        editor.apply()
    }
}