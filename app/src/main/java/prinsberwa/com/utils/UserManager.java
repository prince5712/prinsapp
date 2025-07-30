package prinsberwa.com.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.firebase.auth.FirebaseUser;

public class UserManager {
    private static final String PREF_NAME = "UserPrefs";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_PHOTO_URL = "user_photo_url";
    private static final String KEY_USER_ID = "user_id";

    /**
     * Save Firebase user information to SharedPreferences
     * This can be useful if you need to access user data when Firebase
     * might not be immediately available
     */
    public static void saveUserInfo(Context context, FirebaseUser user) {
        if (context == null || user == null) return;

        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(KEY_USER_ID, user.getUid());
        editor.putString(KEY_USER_NAME, user.getDisplayName());
        editor.putString(KEY_USER_EMAIL, user.getEmail());

        if (user.getPhotoUrl() != null) {
            editor.putString(KEY_USER_PHOTO_URL, user.getPhotoUrl().toString());
        }

        editor.apply();
    }

    /**
     * Get user display name from SharedPreferences
     */
    public static String getUserName(Context context) {
        if (context == null) return "";
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(KEY_USER_NAME, "");
    }

    /**
     * Get user email from SharedPreferences
     */
    public static String getUserEmail(Context context) {
        if (context == null) return "";
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(KEY_USER_EMAIL, "");
    }

    /**
     * Get user photo URL from SharedPreferences
     */
    public static String getUserPhotoUrl(Context context) {
        if (context == null) return "";
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(KEY_USER_PHOTO_URL, "");
    }

    /**
     * Get user ID from SharedPreferences
     */
    public static String getUserId(Context context) {
        if (context == null) return "";
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(KEY_USER_ID, "");
    }

    /**
     * Clear all user data from SharedPreferences
     */
    public static void clearUserData(Context context) {
        if (context == null) return;
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
}