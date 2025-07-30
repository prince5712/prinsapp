package prinsberwa.com.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import prinsberwa.com.R
import prinsberwa.com.activities.LoginActivity
import prinsberwa.com.activities.SubscriptionActivity
import prinsberwa.com.viewmodel.SettingsViewModel
import java.util.Locale

class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var viewModel: SettingsViewModel

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

        // Setup preferences
        setupThemePreference()
        setupLanguagePreference()
        setupSubscriptionPreference()
        setupDemoPreferences()
        setupSignOutPreference()
        setupVersionPreference()
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences?.registerOnSharedPreferenceChangeListener(preferenceChangeListener)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener)
    }

    private val preferenceChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
        when (key) {
            "theme_mode_preference" -> {
                val themeMode = prefs.getString(key, "system") ?: "system"
                applyTheme(themeMode)
                showToast("Theme updated")
            }
            "language_preference" -> {
                val language = prefs.getString(key, "en") ?: "en"
                applyLanguage(language)
                requireActivity().recreate()
                showToast("Language updated")
            }
            // Demo preferences
            "firebase_analytics_preference" -> {
                val enabled = prefs.getBoolean(key, true)
                viewModel.setAnalyticsEnabled(enabled, requireContext())
                showToast(if (enabled) "Analytics enabled" else "Analytics disabled")
            }
            "in_app_updates_preference" -> {
                val enabled = prefs.getBoolean(key, true)
                viewModel.setAutoUpdatesEnabled(enabled, requireContext())
                showToast(if (enabled) "Auto updates enabled" else "Auto updates disabled")
            }
        }
    }

    private fun setupThemePreference() {
        val themePreference = findPreference<ListPreference>("theme_mode_preference")
        themePreference?.summaryProvider = ListPreference.SimpleSummaryProvider.getInstance()

        // Set initial value based on current theme
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val currentTheme = when (currentNightMode) {
            Configuration.UI_MODE_NIGHT_YES -> "dark"
            Configuration.UI_MODE_NIGHT_NO -> "light"
            else -> "system"
        }
        themePreference?.value = currentTheme
    }

    private fun applyTheme(themeMode: String) {
        when (themeMode) {
            "light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            "dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }

    private fun setupLanguagePreference() {
        val languagePreference = findPreference<ListPreference>("language_preference")
        languagePreference?.summaryProvider = ListPreference.SimpleSummaryProvider.getInstance()

        // Set initial value based on current locale
        val currentLocale = resources.configuration.locales[0].language
        languagePreference?.value = currentLocale
    }

    private fun applyLanguage(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        // Save to preferences for app startup
        viewModel.saveLanguagePreference(languageCode, requireContext())
    }

    private fun setupSubscriptionPreference() {
        val subscribePref = findPreference<Preference>("subscribe_preference")
        subscribePref?.setOnPreferenceClickListener {
            // Launch subscription activity
            val intent = Intent(requireContext(), SubscriptionActivity::class.java)
            startActivity(intent)
            true
        }
    }

    private fun setupDemoPreferences() {
        // These are demo preferences but with actual toggle functionality
        val analyticsPref = findPreference<SwitchPreferenceCompat>("firebase_analytics_preference")
        val updatesPref = findPreference<SwitchPreferenceCompat>("in_app_updates_preference")

        // Set initial states from ViewModel
        analyticsPref?.isChecked = viewModel.isAnalyticsEnabled(requireContext())
        updatesPref?.isChecked = viewModel.isAutoUpdatesEnabled(requireContext())
    }

    private fun setupSignOutPreference() {
        val signOutPref = findPreference<Preference>("sign_out_preference")
        signOutPref?.setOnPreferenceClickListener {
            signOut()
            true
        }
    }

    private fun signOut() {
        // Google Sign Out implementation
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        googleSignInClient.signOut().addOnCompleteListener(requireActivity()) {
            viewModel.clearUserData(requireContext())
            showToast("Signed out successfully")

            // Navigate to login activity and clear back stack
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun setupVersionPreference() {
        val versionPref = findPreference<Preference>("version_preference")
        val versionInfo = viewModel.getVersionInfo(requireContext())
        versionPref?.summary = versionInfo
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}