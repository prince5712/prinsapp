package prinsberwa.com.activities

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import prinsberwa.com.R
import prinsberwa.com.base.BaseActivity
import prinsberwa.com.fragments.HiddenSettingsPreferenceFragment
import prinsberwa.com.viewmodel.HiddenSettingsViewModel

class HiddenSettingsActivity : BaseActivity() {

    private lateinit var viewModel: HiddenSettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hidden_settings)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this)[HiddenSettingsViewModel::class.java]

        // Set up the toolbar (assuming BaseActivity has toolbar support)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.hidden_settings)

        // Add the preferences fragment
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HiddenSettingsPreferenceFragment())
                .commit()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}