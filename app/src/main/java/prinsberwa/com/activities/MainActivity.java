package prinsberwa.com.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import prinsberwa.com.R;
import prinsberwa.com.base.BaseActivity;
import prinsberwa.com.databinding.ActivityMainBinding;
import prinsberwa.com.utils.UserManager;

public class MainActivity extends BaseActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private static final String PREF_NAME = "MyAppPrefs";
    private static final String PREF_FIRST_TIME = "isFirstTime";
    private Handler handler = new Handler();
    private static final int CHECK_INTERVAL = 3000;

    // User profile elements in navigation header
    private TextView userNameTextView;
    private TextView userEmailTextView;
    private ImageView userProfileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Get header view to update user info
        View headerView = navigationView.getHeaderView(0);
        userNameTextView = headerView.findViewById(R.id.username);
        userEmailTextView = headerView.findViewById(R.id.useremail);
        userProfileImageView = headerView.findViewById(R.id.userprofile);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_profile)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean(PREF_FIRST_TIME, true)) {
            showToast(getString(R.string.welcome_message));
            showNoticeDialog();
            sharedPreferences.edit().putBoolean(PREF_FIRST_TIME, false).apply();
        }

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                navController.navigate(R.id.nav_home);
            } else if (id == R.id.nav_gallery) {
                navController.navigate(R.id.nav_gallery);
            } else if (id == R.id.nav_slideshow) {
                navController.navigate(R.id.nav_slideshow);
            } else if (id == R.id.nav_profile) {
                navController.navigate(R.id.nav_profile);
            } else if (id == R.id.nav_share) {
                shareContent();
            } else if (id == R.id.nav_exit) {
                confirmExit();
            }
            drawer.closeDrawer(GravityCompat.START);
            return true;
        });

        // Check authentication and display user information
        checkUserAuthentication();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is still authenticated when activity starts/resumes
        checkUserAuthentication();
    }

    private void checkUserAuthentication() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            // User is not logged in, redirect to LoginActivity
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Update UI with user information
            updateUserProfileUI(user);
        }
    }

    private void updateUserProfileUI(FirebaseUser user) {
        if (user != null) {
            // Set user display name
            if (user.getDisplayName() != null && !user.getDisplayName().isEmpty()) {
                userNameTextView.setText(user.getDisplayName());
            } else {
                userNameTextView.setText(R.string.nav_header_title);
            }

            // Set user email
            if (user.getEmail() != null && !user.getEmail().isEmpty()) {
                userEmailTextView.setText(user.getEmail());
            } else {
                userEmailTextView.setText(R.string.nav_header_subtitle);
            }

            // Load profile image if available
            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .placeholder(R.drawable.p_svg_logo)
                        .error(R.drawable.p_svg_logo)
                        .into(userProfileImageView);
            } else {
                userProfileImageView.setImageResource(R.drawable.p_svg_logo);
            }
        } else {
            // Set default values when no user data is available
            userNameTextView.setText(R.string.nav_header_title);
            userEmailTextView.setText(R.string.nav_header_subtitle);
            userProfileImageView.setImageResource(R.drawable.p_svg_logo);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_show_app_info) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        } else if (id == R.id.action_changelog) {
            showChangelogDialog();
            return true;
        } else if (id == R.id.action_update) {
            startActivity(new Intent(this, ExpiryActivity.class));
            return true;
        } else if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareContent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_content_message));
        if (shareIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_via_title)));
        } else {
            Toast.makeText(this, getString(R.string.no_app_to_handle), Toast.LENGTH_SHORT).show();
        }
    }

    private void showChangelogDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialogStyle);
        builder.setTitle(getString(R.string.changelog_title))
                .setMessage(getString(R.string.changelog_message))
                .setPositiveButton(getString(R.string.ok), (dialog, id) -> dialog.dismiss());
        builder.create().show();
    }

    private void confirmExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialogStyle);
        builder.setMessage(getString(R.string.exit_confirmation))
                .setPositiveButton(getString(R.string.yes), (dialog, id) -> finishAffinity())
                .setNegativeButton(getString(R.string.no), (dialog, id) -> dialog.dismiss());
        builder.create().show();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showNoticeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialogStyle);
        builder.setTitle(getString(R.string.notice_title))
                .setMessage(getString(R.string.notice_message))
                .setPositiveButton(getString(R.string.ok), (dialogInterface, i) -> {
                })
                .show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }
}