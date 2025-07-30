package prinsberwa.com.viewmodel

import android.content.Context
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModel
import prinsberwa.com.utils.AnalyticsManager
import prinsberwa.com.utils.PreferenceManager

class SettingsViewModel : ViewModel() {

    private val preferenceManager = PreferenceManager()
    private val analyticsManager = AnalyticsManager()

    /**
     * Get the current app version information
     */
    fun getVersionInfo(context: Context): String {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            "Version ${packageInfo.versionName} (${packageInfo.versionCode})"
        } catch (e: PackageManager.NameNotFoundException) {
            "Unknown version"
        }
    }

    /**
     * Save the selected language preference
     */
    fun saveLanguagePreference(languageCode: String, context: Context) {
        preferenceManager.saveString(context, PreferenceManager.KEY_LANGUAGE, languageCode)
    }

    /**
     * Check if analytics is enabled
     */
    fun isAnalyticsEnabled(context: Context): Boolean {
        return preferenceManager.getBoolean(context, PreferenceManager.KEY_ANALYTICS_ENABLED, true)
    }

    /**
     * Enable or disable analytics collection
     */
    fun setAnalyticsEnabled(enabled: Boolean, context: Context) {
        preferenceManager.saveBoolean(context, PreferenceManager.KEY_ANALYTICS_ENABLED, enabled)
        analyticsManager.setEnabled(enabled)
    }

    /**
     * Check if auto updates are enabled
     */
    fun isAutoUpdatesEnabled(context: Context): Boolean {
        return preferenceManager.getBoolean(context, PreferenceManager.KEY_AUTO_UPDATES, true)
    }

    /**
     * Enable or disable auto updates
     */
    fun setAutoUpdatesEnabled(enabled: Boolean, context: Context) {
        preferenceManager.saveBoolean(context, PreferenceManager.KEY_AUTO_UPDATES, enabled)
    }

    /**
     * Clear all user data when signing out
     */
    fun clearUserData(context: Context) {
        preferenceManager.clearUserData(context)
    }
}