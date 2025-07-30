plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("org.jetbrains.kotlin.android")
}

android {
    signingConfigs {
        getByName("debug") {
            storeFile = file("C:\\Users\\prins\\Desktop\\Prince Files\\PrinceKey.jks")
            storePassword = "PR7i9045"
            keyAlias = "PrinceKey"
            keyPassword = "PR9i9045"
        }
    }
    namespace = "prinsberwa.com"
    compileSdk = 36

    defaultConfig {
        applicationId = "prinsberwa.com"
        minSdk = 24
        //noinspection OldTargetApi
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }
    sourceSets {
        getByName("main") {
            assets {
                srcDirs("src\\main\\assets", "src\\main\\assets")
            }
        }
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.9.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.9.2")
    implementation("androidx.navigation:navigation-fragment-ktx:2.9.2")
    implementation("androidx.navigation:navigation-ui-ktx:2.9.2")
    implementation("androidx.preference:preference-ktx:1.2.1")
    implementation("androidx.activity:activity-ktx:1.10.1")
    implementation("com.google.firebase:firebase-auth:24.0.0")
    implementation(platform("com.google.firebase:firebase-bom:34.0.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.android.gms:play-services-auth:21.4.0")
    implementation("com.android.billingclient:billing:8.0.0")
    implementation ("com.squareup.okhttp3:okhttp:5.1.0")
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.16.0")
    //implementation ("com.github.Jamshid-M:IGRefreshLayout:1.0.3")
    implementation("androidx.core:core-ktx:1.16.0")
    //implementation ("com.squareup.picasso:picasso:2.71828")
    implementation ("com.google.code.gson:gson:2.13.1")
    implementation ("androidx.recyclerview:recyclerview:1.4.0")
    implementation ("androidx.cardview:cardview:1.0.0")
    implementation("androidx.activity:activity-ktx:1.10.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}