package prinsberwa.com.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.Date;

import prinsberwa.com.R;
import prinsberwa.com.base.BaseActivity;

public class SplashActivity extends BaseActivity {

    // Define your expiry date here
    private static final int EXPIRY_YEAR = 2026;
    private static final int EXPIRY_MONTH = Calendar.JANUARY; // Months are 0-based
    private static final int EXPIRY_DAY = 31;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Delay for a short period before checking expiry and login status
        new Handler().postDelayed(() -> {
            if (isAppExpired()) {
                // Redirect to ExpiryActivity if the app is expired
                Intent intent = new Intent(SplashActivity.this, ExpiryActivity.class);
                startActivity(intent);
            } else {
                // Check login status
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();

                if (currentUser == null) {
                    // Redirect to LoginActivity if the user is not logged in
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    // Redirect to MainActivity if the user is logged in
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
            finish(); // Finish SplashActivity to prevent going back
        }, 2000); // 2000 milliseconds = 2 seconds
    }

    /**
     * Checks if the app is expired based on the defined expiry date.
     */
    private boolean isAppExpired() {
        Calendar expiryCalendar = Calendar.getInstance();
        expiryCalendar.set(EXPIRY_YEAR, EXPIRY_MONTH, EXPIRY_DAY, 0, 0, 0);
        Date expiryDate = expiryCalendar.getTime();

        Calendar currentCalendar = Calendar.getInstance();
        Date currentDate = currentCalendar.getTime();

        return currentDate.after(expiryDate);
    }
}
