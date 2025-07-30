# prinsapp

## Project Description

This Android application, "prinsapp" (with the application ID `prinsberwa.com`), appears to be a mobile app with a variety of features, likely including:

*   **User Authentication:** Integration with Firebase Authentication (`com.google.firebase:firebase-auth`) and Google Sign-In (`com.google.android.gms:play-services-auth`) suggests users can sign up and log in.
*   **Analytics:** Firebase Analytics (`com.google.firebase:firebase-analytics`) is included for tracking user behavior and app usage.
*   **In-App Purchases:** The Google Play Billing Library (`com.android.billingclient:billing`) indicates functionality for in-app purchases or subscriptions.
*   **Modern UI:** Uses Material Components (`com.google.android.material:material`) and Jetpack libraries like ViewModel, LiveData, Navigation, and ViewBinding for a modern and maintainable UI.
*   **Image Loading:** Utilizes Glide (`com.github.bumptech.glide:glide`) for efficient image loading and display.
*   **Networking:** Includes OkHttp (`com.squareup.okhttp3:okhttp`) for making network requests.
*   **Data Serialization:** Uses Gson (`com.google.code.gson:gson`) for JSON parsing.
*   **UI Components:** Employs RecyclerView and CardView for displaying lists of data.

## Tech Stack & Dependencies

*   **Language:** Kotlin
*   **Build Tool:** Gradle
*   **Core Android Jetpack:**
    *   `androidx.appcompat:appcompat`
    *   `androidx.core:core-ktx`
    *   `androidx.constraintlayout:constraintlayout`
    *   `androidx.lifecycle:lifecycle-livedata-ktx`
    *   `androidx.lifecycle:lifecycle-viewmodel-ktx`
    *   `androidx.navigation:navigation-fragment-ktx`
    *   `androidx.navigation:navigation-ui-ktx`
    *   `androidx.activity:activity-ktx`
    *   `androidx.preference:preference-ktx`
    *   `androidx.recyclerview:recyclerview`
    *   `androidx.cardview:cardview`
*   **UI:**
    *   `com.google.android.material:material`
    *   ViewBinding enabled
*   **Firebase:**
    *   `com.google.firebase:firebase-bom` (Bill of Materials)
    *   `com.google.firebase:firebase-auth`
    *   `com.google.firebase:firebase-analytics`
    *   `com.google.gms.google-services` (Gradle plugin)
*   **Google Play Services:**
    *   `com.google.android.gms:play-services-auth`
*   **Billing:**
    *   `com.android.billingclient:billing`
*   **Networking & Data:**
    *   `com.squareup.okhttp3:okhttp`
    *   `com.google.code.gson:gson`
*   **Image Loading:**
    *   `com.github.bumptech.glide:glide`
*   **Testing:**
    *   `junit:junit`
    *   `androidx.test.ext:junit`
    *   `androidx.test.espresso:espresso-core`

## Build Configuration

*   **`compileSdk`:** 36
*   **`minSdk`:** 24
*   **`targetSdk`:** 35
*   **`versionCode`:** 1
*   **`versionName`:** "1.0"
*   **Signing:**
    *   Debug builds are signed using a keystore located at `C:\Users\prins\Desktop\Prince Files\PrinceKey.jks` with alias `PrinceKey`. **Note: This path is specific to the development environment and should be secured and likely parameterized for production builds.**
    *   Release builds also use this debug signing configuration. **This is not recommended for production. Release builds should use a separate, secure release keystore.**
*   **ProGuard:** Enabled for release builds (`isMinifyEnabled = true`) using default ProGuard rules and `proguard-rules.pro`.
*   **Java Version:** Uses Java 11.
*   **ViewBinding:** Enabled.
    
