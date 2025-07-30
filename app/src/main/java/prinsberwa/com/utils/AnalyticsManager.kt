package prinsberwa.com.utils

import com.google.firebase.analytics.FirebaseAnalytics

class AnalyticsManager {

    private var enabled = true
    private var firebaseAnalytics: FirebaseAnalytics? = null

    /**
     * Initialize Firebase Analytics
     */
    fun initialize(firebaseAnalytics: FirebaseAnalytics) {
        this.firebaseAnalytics = firebaseAnalytics
    }

    /**
     * Check if analytics collection is enabled
     */
    fun isEnabled(): Boolean {
        return enabled
    }

    /**
     * Enable or disable analytics collection
     */
    fun setEnabled(enabled: Boolean) {
        this.enabled = enabled
        firebaseAnalytics?.setAnalyticsCollectionEnabled(enabled)
    }

    /**
     * Log an event if analytics is enabled
     */
    fun logEvent(eventName: String, params: Map<String, Any>? = null) {
        if (!enabled || firebaseAnalytics == null) return

        val bundle = android.os.Bundle()
        params?.forEach { (key, value) ->
            when (value) {
                is String -> bundle.putString(key, value)
                is Int -> bundle.putInt(key, value)
                is Long -> bundle.putLong(key, value)
                is Double -> bundle.putDouble(key, value)
                is Boolean -> bundle.putBoolean(key, value)
            }
        }

        firebaseAnalytics?.logEvent(eventName, bundle)
    }
}