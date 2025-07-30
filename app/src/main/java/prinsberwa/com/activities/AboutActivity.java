package prinsberwa.com.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

import prinsberwa.com.R;
import prinsberwa.com.base.BaseActivity;

public class AboutActivity extends BaseActivity {

    private int tapCounter = 0; // Counter to track the number of taps on the app icon
    private static final int TAPS_REQUIRED = 3; // Number of taps required to trigger the action

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // Setting up the back button in the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("App Info");

        PackageManager pm = getPackageManager();
        String packageName = getPackageName();

        try {
            PackageInfo pInfo = pm.getPackageInfo(packageName, 0);
            String versionName = pInfo.versionName;
            int versionCode = pInfo.versionCode;

            ImageView appIcon = findViewById(R.id.app_icon);
            TextView appName = findViewById(R.id.app_name);
            TextView version = findViewById(R.id.version);
            TextView packageNameText = findViewById(R.id.package_name);
            TextView copyrightText = findViewById(R.id.copyright_text); // New

            // Set app icon
            appIcon.setImageResource(R.drawable.ic_logo_gradient); // Replace with your icon resource

            // Set app name
            CharSequence appLabel = pm.getApplicationLabel(pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA));
            appName.setText(appLabel);

            // Set version information
            version.setText("Version " + versionName + " (" + versionCode + ")");

            // Set package name
            packageNameText.setText("Package: " + packageName);

            // Set copyright text dynamically with the current year
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            String copyright = "Copyright © 2022-" + currentYear; // Update with current year
            copyrightText.setText(copyright);

            // Set a click listener for the app icon to detect taps
            appIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tapCounter++;
                    if (tapCounter >= TAPS_REQUIRED) {
                        // Reset counter and launch SettingsActivity
                        tapCounter = 0;
                        launchSettingsActivity();
                    }
                }
            });

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Launch SettingsActivity when 7 taps are detected
    private void launchSettingsActivity() {
        Intent intent = new Intent(this, HiddenSettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Go back when the back button on the action bar is pressed
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void viewLicenseClicked(View view) {
        // Launch LicensesActivity
        Intent intent = new Intent(this, LicenseActivity.class);
        startActivity(intent);
    }
}
