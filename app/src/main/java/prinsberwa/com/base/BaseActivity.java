package prinsberwa.com.base;

import android.content.SharedPreferences;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import prinsberwa.com.R;

public class BaseActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "screenshot_prefs";
    private static final String SCREENSHOT_KEY = "screenshot_enabled";
    // Flag to determine if we should enforce portrait mode
    protected boolean enforcePortraitMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Apply portrait mode if needed - can be overridden by child activities
        applyOrientationSettings();

        // Apply FLAG_SECURE based on screenshot preference
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isScreenshotEnabled = prefs.getBoolean(SCREENSHOT_KEY, false);
        if (!isScreenshotEnabled) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        // Set status bar and navigation bar colors based on the current theme mode
        updateSystemBarsColors();

        // Handle back press using OnBackPressedDispatcher
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Perform the back press action
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }

    /**
     * Apply orientation settings based on device type and configuration
     */
    protected void applyOrientationSettings() {
        // Check if this is a tablet
        boolean isTablet = (getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;

        if (enforcePortraitMode && !isTablet) {
            // Force portrait mode only for phones
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if (isTablet) {
            // For tablets, we can use sensor orientation to allow both portrait and landscape
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Update colors when configuration changes (e.g., dark mode toggle)
        updateSystemBarsColors();
    }

    private void updateSystemBarsColors() {
        // Check if the current mode is dark or light
        boolean isDarkMode = (getResources().getConfiguration().uiMode &
                Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Get window instance
            View decorView = getWindow().getDecorView();

            if (isDarkMode) {
                // Dark mode: Set status bar and navigation bar to black
                getWindow().setStatusBarColor(Color.BLACK);
                getWindow().setNavigationBarColor(Color.BLACK);

                // Set light status bar OFF (dark icons)
                decorView.setSystemUiVisibility(decorView.getSystemUiVisibility()
                        & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

                // For Android 8.0+, set light navigation bar OFF (dark icons)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    decorView.setSystemUiVisibility(decorView.getSystemUiVisibility()
                            & ~View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
                }
            } else {
                // Light mode: Set status bar and navigation bar to white
                getWindow().setStatusBarColor(Color.WHITE);
                getWindow().setNavigationBarColor(Color.WHITE);

                // Set light status bar ON (dark icons)
                decorView.setSystemUiVisibility(decorView.getSystemUiVisibility()
                        | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

                // For Android 8.0+, set light navigation bar ON (dark icons)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    decorView.setSystemUiVisibility(decorView.getSystemUiVisibility()
                            | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
                }
            }
        }
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    /**
     * Method to handle orientation config changes
     * Override in child activities that need to preserve state during orientation changes
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Child activities can extend this to save additional state
    }
}