package prinsberwa.com.activities;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import prinsberwa.com.R;
import prinsberwa.com.base.BaseActivity;
import prinsberwa.com.utils.UserManager;

public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 9001;

    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth firebaseAuth;
    private CardView btnGoogleSignInCard;
    private View btnGoogleSignIn;
    private boolean isSigningIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeViews();
        configureGoogleSignIn();
        setupClickListeners();
        animateEntry();

        // Check if user is already signed in
        if (firebaseAuth.getCurrentUser() != null) {
            // User is already signed in, go directly to MainActivity
            new Handler(Looper.getMainLooper()).postDelayed(this::startMainActivity, 1000);
        }
    }

    private void initializeViews() {
        btnGoogleSignInCard = findViewById(R.id.btnGoogleSignInCard);
        btnGoogleSignIn = findViewById(R.id.btnGoogleSignIn);
    }

    private void configureGoogleSignIn() {
        // Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void setupClickListeners() {
        // Set up click listeners for both the custom card and the invisible Google button
        View.OnClickListener signInClickListener = v -> {
            if (!isSigningIn) {
                signInWithGoogle();
            }
        };

        if (btnGoogleSignInCard != null) {
            btnGoogleSignInCard.setOnClickListener(signInClickListener);
        }

        if (btnGoogleSignIn != null) {
            btnGoogleSignIn.setOnClickListener(signInClickListener);
        } else {
            // Fallback to original button if custom card not found
            findViewById(R.id.btnGoogleSignIn).setOnClickListener(signInClickListener);
        }
    }

    private void animateEntry() {
        // Animate the login card entrance
        View cardContainer = findViewById(R.id.cardContainer);
        if (cardContainer != null) {
            cardContainer.setAlpha(0f);
            cardContainer.setTranslationY(100f);

            ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(cardContainer, "alpha", 0f, 1f);
            ObjectAnimator translateAnimator = ObjectAnimator.ofFloat(cardContainer, "translationY", 100f, 0f);

            alphaAnimator.setDuration(600);
            translateAnimator.setDuration(600);
            alphaAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            translateAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

            alphaAnimator.setStartDelay(200);
            translateAnimator.setStartDelay(200);

            alphaAnimator.start();
            translateAnimator.start();
        }
    }

    private void signInWithGoogle() {
        if (isSigningIn) {
            return;
        }

        isSigningIn = true;
        animateButtonPress();

        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void animateButtonPress() {
        if (btnGoogleSignInCard != null) {
            // Scale animation for button press feedback
            ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(btnGoogleSignInCard, "scaleX", 1f, 0.95f);
            ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(btnGoogleSignInCard, "scaleY", 1f, 0.95f);
            ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(btnGoogleSignInCard, "scaleX", 0.95f, 1f);
            ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(btnGoogleSignInCard, "scaleY", 0.95f, 1f);

            scaleDownX.setDuration(100);
            scaleDownY.setDuration(100);
            scaleUpX.setDuration(100);
            scaleUpY.setDuration(100);

            scaleDownX.start();
            scaleDownY.start();

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                scaleUpX.start();
                scaleUpY.start();
            }, 100);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        isSigningIn = false;

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    firebaseAuthWithGoogle(account.getIdToken());
                }
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
                showErrorMessage("Google Sign-In Failed. Please try again.");
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        // Save user data to UserManager
                        if (user != null) {
                            UserManager.saveUserInfo(this, user);
                            showWelcomeMessage(user.getDisplayName());
                            // Delay navigation to show welcome message
                            new Handler(Looper.getMainLooper()).postDelayed(this::startMainActivity, 1500);
                        }
                    } else {
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        showErrorMessage("Authentication Failed. Please try again.");
                    }
                });
    }

    private void showWelcomeMessage(String displayName) {
        String welcomeText = displayName != null ?
                "Welcome back, " + displayName.split(" ")[0] + "!" :
                "Welcome back!";
        Toast.makeText(this, welcomeText, Toast.LENGTH_SHORT).show();
    }

    private void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

        // Add transition animation
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isSigningIn = false;
    }

    @Override
    public void onBackPressed() {
        // Prevent going back to previous activities after successful login check
        if (firebaseAuth.getCurrentUser() != null) {
            startMainActivity();
        } else {
            super.onBackPressed();
        }
    }
}